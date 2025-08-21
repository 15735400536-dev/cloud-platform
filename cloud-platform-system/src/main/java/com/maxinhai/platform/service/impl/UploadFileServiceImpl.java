package com.maxinhai.platform.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.UploadFileQueryDTO;
import com.maxinhai.platform.enums.FileType;
import com.maxinhai.platform.mapper.UploadFileMapper;
import com.maxinhai.platform.po.UploadFile;
import com.maxinhai.platform.service.UploadFileService;
import com.maxinhai.platform.vo.UploadFileVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName：FileServiceImpl
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 14:13
 * @Description: 上传文件业务层
 */
@Slf4j
@Service
public class UploadFileServiceImpl extends ServiceImpl<UploadFileMapper, UploadFile>
        implements UploadFileService {

    @javax.annotation.Resource
    private UploadFileMapper uploadFileMapper;

    private final Path fileStorageLocation;

    @Value("${file.upload.path}")
    private String uploadPath;

    public UploadFileServiceImpl(@Value("${file.upload.path}") String uploadPath) {
        this.fileStorageLocation = Paths.get(uploadPath)
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("无法创建文件上传目录: " + ex.getMessage());
        }
    }

    @Override
    public Page<UploadFileVO> searchByPage(UploadFileQueryDTO param) {
        Page<UploadFileVO> pageResult = uploadFileMapper.selectJoinPage(param.getPage(), UploadFileVO.class,
                new MPJLambdaWrapper<UploadFile>()
                        .like(StrUtil.isNotBlank(param.getFileName()), UploadFile::getFileName, param.getFileName())
                        .like(StrUtil.isNotBlank(param.getFileType()), UploadFile::getFileType, param.getFileType())
                        .orderByDesc(UploadFile::getCreateTime));
        return pageResult;
    }

    @Override
    public void remove(String[] ids) {
        // 删除文件，释放空间

        // 删除文件上传记录
    }

    /**
     * 上传文件
     */
    @Override
    public String uploadFile(MultipartFile file, FileType fileType) throws IOException {
        // 获取文件名并清理
        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        // 生成唯一文件名，避免覆盖
        String fileName = generateUniqueFileName(originalFilename);

        // 创建文件类型对应的子目录
        String typeDir = fileType.getCode();
        Path typePath = this.fileStorageLocation.resolve(typeDir);
        if (!Files.exists(typePath)) {
            Files.createDirectories(typePath);
        }

        // 复制文件到目标位置
        Path targetLocation = typePath.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // 返回相对路径
        return typeDir + "/" + fileName;
    }

    /**
     * 生成唯一文件名
     */
    private String generateUniqueFileName(String originalFilename) {
        String fileExtension = StringUtils.getFilenameExtension(originalFilename);
        String baseName = StringUtils.stripFilenameExtension(originalFilename);

        // 使用时间戳和随机数确保唯一性
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String random = String.valueOf(new Random().nextInt(1000));

        if (fileExtension != null) {
            return baseName + "_" + timestamp + "_" + random + "." + fileExtension;
        } else {
            return baseName + "_" + timestamp + "_" + random;
        }
    }

    /**
     * 加载文件作为资源
     */
    @Override
    public Resource loadFileAsResource(String fileName) {
        try {
            // 处理包含类型目录的文件名
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new FileSystemResource(filePath);

            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("文件不存在: " + fileName);
            }
        } catch (Exception ex) {
            throw new RuntimeException("文件不存在: " + fileName, ex);
        }
    }

    /**
     * 获取文件列表
     */
    @Override
    public List<Map<String, Object>> getFileList() {
        List<Map<String, Object>> fileList = new ArrayList<>();

        // 遍历所有类型目录
        File rootDir = new File(uploadPath);
        File[] typeDirs = rootDir.listFiles(File::isDirectory);

        if (typeDirs != null) {
            for (File typeDir : typeDirs) {
                File[] files = typeDir.listFiles(File::isFile);
                if (files != null) {
                    for (File file : files) {
                        Map<String, Object> fileInfo = new HashMap<>();
                        fileInfo.put("name", file.getName());
                        fileInfo.put("typeDir", typeDir.getName());
                        fileInfo.put("size", formatFileSize(file.length()));
                        fileInfo.put("path", typeDir.getName() + "/" + file.getName());
                        fileInfo.put("lastModified", new Date(file.lastModified()));
                        fileList.add(fileInfo);
                    }
                }
            }
        }

        // 按修改时间排序，最新的在前面
        fileList.sort((f1, f2) -> {
            Date d1 = (Date) f1.get("lastModified");
            Date d2 = (Date) f2.get("lastModified");
            return d2.compareTo(d1);
        });

        return fileList;
    }

    /**
     * 格式化文件大小
     */
    private String formatFileSize(long size) {
        if (size <= 0) return "0 B";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return String.format("%.2f %s", size / Math.pow(1024, digitGroups), units[digitGroups]);
    }

    /**
     * 删除文件
     */
    @Override
    public boolean deleteFile(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            return Files.deleteIfExists(filePath);
        } catch (Exception ex) {
            throw new RuntimeException("删除文件失败: " + fileName, ex);
        }
    }

}
