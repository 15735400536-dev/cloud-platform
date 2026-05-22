package com.maxinhai.platform.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

/**
 * 文件下载工具类
 * 支持：resources内部文件、服务器外部绝对路径文件
 * 支持格式：Excel、PDF、Word、常见图片
 */
public class FileDownloadUtils {

    // 常用文件MIME类型常量
    public static final String CONTENT_EXCEL = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final String CONTENT_PDF = "application/pdf";
    public static final String CONTENT_WORD = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    public static final String CONTENT_PNG = "image/png";
    public static final String CONTENT_JPG = "image/jpeg";
    public static final String CONTENT_GIF = "image/gif";
    public static final String CONTENT_JSON   = "application/json";

    //========== Excel 下载 ==========
    public static void downloadExcelFromResource(HttpServletResponse response, String resourcePath, String downloadFileName) throws IOException {
        downloadFromResource(response, resourcePath, downloadFileName, CONTENT_EXCEL);
    }

    public static void downloadExcelFromServer(HttpServletResponse response, String serverAbsolutePath, String downloadFileName) throws IOException {
        downloadFromServer(response, serverAbsolutePath, downloadFileName, CONTENT_EXCEL);
    }

    //========== PDF 下载 ==========
    public static void downloadPdfFromResource(HttpServletResponse response, String resourcePath, String downloadFileName) throws IOException {
        downloadFromResource(response, resourcePath, downloadFileName, CONTENT_PDF);
    }

    public static void downloadPdfFromServer(HttpServletResponse response, String serverAbsolutePath, String downloadFileName) throws IOException {
        downloadFromServer(response, serverAbsolutePath, downloadFileName, CONTENT_PDF);
    }

    //========== Word 下载 ==========
    public static void downloadWordFromResource(HttpServletResponse response, String resourcePath, String downloadFileName) throws IOException {
        downloadFromResource(response, resourcePath, downloadFileName, CONTENT_WORD);
    }

    public static void downloadWordFromServer(HttpServletResponse response, String serverAbsolutePath, String downloadFileName) throws IOException {
        downloadFromServer(response, serverAbsolutePath, downloadFileName, CONTENT_WORD);
    }

    //========== 图片下载 ==========
    public static void downloadImgFromResource(HttpServletResponse response, String resourcePath, String downloadFileName, String imgType) throws IOException {
        downloadFromResource(response, resourcePath, downloadFileName, imgType);
    }

    public static void downloadImgFromServer(HttpServletResponse response, String serverAbsolutePath, String downloadFileName, String imgType) throws IOException {
        downloadFromServer(response, serverAbsolutePath, downloadFileName, imgType);
    }

    // ========== JSON 新增 ==========
    public static void downloadJsonFromResource(HttpServletResponse response, String resourcePath, String fileName) throws IOException {
        downloadFromResource(response, resourcePath, fileName, CONTENT_JSON);
    }
    public static void downloadJsonFromServer(HttpServletResponse response, String serverPath, String fileName) throws IOException {
        downloadFromServer(response, serverPath, fileName, CONTENT_JSON);
    }

    //========== 通用底层方法 ==========
    public static void downloadFromResource(HttpServletResponse response, String resourcePath, String downloadFileName, String contentType) throws IOException {
        Resource resource = new ClassPathResource(resourcePath);
        download(response, resource, downloadFileName, contentType);
    }

    public static void downloadFromServer(HttpServletResponse response, String serverAbsolutePath, String downloadFileName, String contentType) throws IOException {
        Resource resource = new FileSystemResource(serverAbsolutePath);
        download(response, resource, downloadFileName, contentType);
    }

    /**
     * 统一文件输出逻辑
     */
    private static void download(HttpServletResponse response, Resource resource, String downloadFileName, String contentType) throws IOException {
        if (!resource.exists()) {
            throw new IOException("目标文件不存在");
        }
        try (InputStream in = resource.getInputStream();
             ServletOutputStream out = response.getOutputStream()) {

            response.setContentType(contentType);
            response.setCharacterEncoding("UTF-8");
            String encodeName = URLEncoder.encode(downloadFileName, "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + encodeName);

            byte[] buffer = new byte[4096];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        }
    }

}
