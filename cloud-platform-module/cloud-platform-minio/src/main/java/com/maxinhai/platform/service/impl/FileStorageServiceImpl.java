package com.maxinhai.platform.service.impl;

import com.maxinhai.platform.config.MinIOConfig;
import com.maxinhai.platform.exception.FileStorageException;
import com.maxinhai.platform.po.FileStorage;
import com.maxinhai.platform.service.FileStorageService;
import io.minio.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName：FileStorageServiceImpl
 * @Author: XinHai.Ma
 * @Date: 2025/9/12 11:03
 * @Description: 文件存储业务层
 */
@Slf4j
@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Resource
    private MinioClient minioClient;

    @Resource
    private MinIOConfig minIOConfig;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${file.prefix}")
    private String filePrefix;

    @Value("${file.allowed-types}")
    private String allowedTypes;

    // Redis中文件信息的key前缀
    private static final String FILE_INFO_KEY_PREFIX = "file:info:";

    @Override
    public FileStorage uploadFile(MultipartFile file, String uploader) {
        try {
            // 1. 检查文件是否为空
            if (file.isEmpty()) {
                throw new FileStorageException("上传文件不能为空");
            }

            // 2. 检查文件类型是否允许
            String contentType = file.getContentType();
            if (!isAllowedType(contentType)) {
                throw new FileStorageException("不支持的文件类型: " + contentType);
            }

            // 3. 检查并创建存储桶
            checkAndCreateBucket();

            // 4. 准备文件信息
            String originalFilename = file.getOriginalFilename();
            String suffix = FilenameUtils.getExtension(originalFilename);
            String fileId = UUID.randomUUID().toString().replaceAll("-", "");
            String fileName = fileId + "." + suffix;
            String objectName = filePrefix + fileName; // 存储在MinIO中的路径

            // 5. 上传文件到MinIO
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minIOConfig.getBucketName())
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(contentType)
                            .build()
            );

            // 6. 构建文件信息
            FileStorage fileStorage = FileStorage.builder()
                    .id(fileId)
                    .fileName(fileName)
                    .originalName(originalFilename)
                    .suffix(suffix)
                    .contentType(contentType)
                    .fileSize(file.getSize())
                    .filePath(objectName)
                    .uploadTime(LocalDateTime.now())
                    .uploader(uploader)
                    .url(getFileUrl(objectName))
                    .build();

            // 7. 缓存文件信息到Redis（有效期7天）
            redisTemplate.opsForValue().set(
                    FILE_INFO_KEY_PREFIX + fileId,
                    fileStorage,
                    7,
                    TimeUnit.DAYS
            );

            log.info("文件上传成功: {}", fileStorage);
            return fileStorage;
        } catch (Exception e) {
            log.error("文件上传失败", e);
            throw new FileStorageException("文件上传失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void downloadFile(String fileId, HttpServletResponse response) {
        try {
            // 1. 获取文件信息
            FileStorage fileStorage = getFileInfo(fileId);
            if (fileStorage == null) {
                throw new FileStorageException("文件不存在或已过期");
            }

            // 2. 设置响应头
            response.setContentType(fileStorage.getContentType());
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(fileStorage.getOriginalName().getBytes("UTF-8"), "ISO8859-1"));
            response.setContentLengthLong(fileStorage.getFileSize());

            // 3. 从MinIO获取文件并写入响应流
            try (InputStream in = getFileInputStream(fileId);
                 OutputStream out = response.getOutputStream()) {

                byte[] buffer = new byte[1024 * 4];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                out.flush();
            }

            log.info("文件下载成功: {}", fileId);
        } catch (IOException e) {
            log.error("文件下载失败", e);
            throw new FileStorageException("文件下载失败: " + e.getMessage(), e);
        }
    }

    @Override
    public FileStorage getFileInfo(String fileId) {
        // 1. 先从Redis获取
        FileStorage fileStorage = (FileStorage) redisTemplate.opsForValue().get(FILE_INFO_KEY_PREFIX + fileId);

        // 2. 如果Redis中没有，可能是缓存过期，可以从数据库获取（这里简化处理，实际项目可能需要持久化到数据库）
        if (fileStorage == null) {
            log.warn("文件信息缓存不存在或已过期: {}", fileId);
            return null;
        }

        return fileStorage;
    }

    @Override
    public boolean deleteFile(String fileId) {
        try {
            // 1. 获取文件信息
            FileStorage fileStorage = getFileInfo(fileId);
            if (fileStorage == null) {
                throw new FileStorageException("文件不存在或已过期");
            }

            // 2. 从MinIO删除文件
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(minIOConfig.getBucketName())
                            .object(fileStorage.getFilePath())
                            .build()
            );

            // 3. 从Redis删除缓存
            redisTemplate.delete(FILE_INFO_KEY_PREFIX + fileId);

            log.info("文件删除成功: {}", fileId);
            return true;
        } catch (Exception e) {
            log.error("文件删除失败", e);
            throw new FileStorageException("文件删除失败: " + e.getMessage(), e);
        }
    }

    @Override
    public InputStream getFileInputStream(String fileId) {
        try {
            FileStorage fileStorage = getFileInfo(fileId);
            if (fileStorage == null) {
                throw new FileStorageException("文件不存在或已过期");
            }

            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(minIOConfig.getBucketName())
                            .object(fileStorage.getFilePath())
                            .build()
            );
        } catch (Exception e) {
            log.error("获取文件输入流失败", e);
            throw new FileStorageException("获取文件失败: " + e.getMessage(), e);
        }
    }

    /**
     * 检查并创建存储桶
     */
    private void checkAndCreateBucket() throws Exception {
        boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(minIOConfig.getBucketName())
                .build());

        if (!bucketExists) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(minIOConfig.getBucketName())
                    .build());
            log.info("创建存储桶成功: {}", minIOConfig.getBucketName());
        }
    }

    /**
     * 获取文件访问URL
     */
    private String getFileUrl(String objectName) throws Exception {
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(minIOConfig.getBucketName())
                        .object(objectName)
                        .expiry(minIOConfig.getUrlExpire())
                        .build()
        );
    }

    /**
     * 检查文件类型是否允许上传
     */
    private boolean isAllowedType(String contentType) {
        if (StringUtils.isEmpty(contentType) || StringUtils.isEmpty(allowedTypes)) {
            return false;
        }

        String[] allowedTypeArray = allowedTypes.split(",");
        for (String type : allowedTypeArray) {
            if (type.trim().equals(contentType)) {
                return true;
            }
        }
        return false;
    }

}
