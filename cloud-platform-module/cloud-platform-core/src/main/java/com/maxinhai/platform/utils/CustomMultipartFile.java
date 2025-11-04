package com.maxinhai.platform.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 自定义上传文件实现类（非测试场景，极少用）
 *
 * @Desc 如果在非测试环境中需要手动创建MultipartFile（不推荐，通常无需这么做），可自定义类实现MultipartFile接口，并重写所有方法。
 */
public class CustomMultipartFile implements MultipartFile {

    private final String name;          // 请求参数名
    private final String originalFilename; // 原始文件名
    private final String contentType;   // MIME类型
    private final byte[] content;       // 文件内容

    // 构造方法
    public CustomMultipartFile(String name, String originalFilename, String contentType, byte[] content) {
        this.name = name;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.content = content;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOriginalFilename() {
        return originalFilename;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return content == null || content.length == 0;
    }

    @Override
    public long getSize() {
        return content == null ? 0 : content.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return content == null ? new byte[0] : content.clone();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(content == null ? new byte[0] : content);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        Files.write(dest.toPath(), content);
    }

    // Java 1.7+ 支持Path参数的transferTo（可选实现）
    @Override
    public void transferTo(Path dest) throws IOException, IllegalStateException {
        Files.write(dest, content);
    }

    /**
     * 根据文件路径获取字节数组
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static byte[] getByte(String filePath) throws IOException {
        // 1. 解析路径为Path对象
        Path path = Paths.get(filePath);

        // 验证文件是否存在且为普通文件
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("文件不存在: " + filePath);
        }
        if (!Files.isRegularFile(path)) {
            throw new IllegalArgumentException("路径不是文件: " + filePath);
        }

        return Files.readAllBytes(path); // 文件字节内容
    }

    /**
     * 根据文件路径获取内容类型
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String getContentType(String filePath) throws IOException {
        // 1. 解析路径为Path对象
        Path path = Paths.get(filePath);

        // 验证文件是否存在且为普通文件
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("文件不存在: " + filePath);
        }
        if (!Files.isRegularFile(path)) {
            throw new IllegalArgumentException("路径不是文件: " + filePath);
        }

        return getContentType(path); // 文件MIME类型
    }

    /**
     * 获取文件的MIME类型（contentType）
     * 优先通过Files.probeContentType探测，失败则根据后缀手动映射
     */
    private static String getContentType(Path path) throws IOException {
        // 1. 尝试通过Java NIO的探针获取MIME类型
        String contentType = Files.probeContentType(path);
        if (contentType != null) {
            return contentType;
        }

        // 2. 若探针失败，根据文件后缀手动映射（针对常见图片类型）
        String fileName = path.getFileName().toString().toLowerCase();
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".gif")) {
            return "image/gif";
        } else if (fileName.endsWith(".bmp")) {
            return "image/bmp";
        } else {
            // 其他类型可继续扩展
            return "application/octet-stream"; // 默认二进制流
        }
    }

}
