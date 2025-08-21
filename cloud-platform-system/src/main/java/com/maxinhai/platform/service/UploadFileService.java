package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.UploadFileQueryDTO;
import com.maxinhai.platform.enums.FileType;
import com.maxinhai.platform.po.UploadFile;
import com.maxinhai.platform.vo.UploadFileVO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface UploadFileService extends IService<UploadFile> {

    Page<UploadFileVO> searchByPage(UploadFileQueryDTO param);

    void remove(String[] ids);

    /**
     * 上传文件
     * @param file 上传的文件
     * @param fileType 文件类型
     * @return 保存的文件路径
     * @throws IOException 异常
     */
    String uploadFile(MultipartFile file, FileType fileType) throws IOException;

    /**
     * 加载文件作为资源
     * @param fileName 文件名
     * @return 资源
     */
    Resource loadFileAsResource(String fileName);

    /**
     * 获取文件列表
     * @return 文件列表
     */
    List<Map<String, Object>> getFileList();

    /**
     * 删除文件
     * @param fileName 文件名
     * @return 是否删除成功
     */
    boolean deleteFile(String fileName);

}
