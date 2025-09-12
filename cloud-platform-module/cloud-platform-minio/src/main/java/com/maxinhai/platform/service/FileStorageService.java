package com.maxinhai.platform.service;

import com.maxinhai.platform.po.FileStorage;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

public interface FileStorageService {

    /**
     * 上传文件
     * @param file 上传的文件
     * @param uploader 上传者
     * @return 文件信息
     */
    FileStorage uploadFile(MultipartFile file, String uploader);

    /**
     * 下载文件
     * @param fileId 文件ID
     * @param response 响应对象
     */
    void downloadFile(String fileId, HttpServletResponse response);

    /**
     * 获取文件信息
     * @param fileId 文件ID
     * @return 文件信息
     */
    FileStorage getFileInfo(String fileId);

    /**
     * 删除文件
     * @param fileId 文件ID
     * @return 是否删除成功
     */
    boolean deleteFile(String fileId);

    /**
     * 获取文件输入流
     * @param fileId 文件ID
     * @return 输入流
     */
    InputStream getFileInputStream(String fileId);

}
