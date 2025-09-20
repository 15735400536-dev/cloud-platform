package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.UploadFileQueryDTO;
import com.maxinhai.platform.po.UploadFile;
import com.maxinhai.platform.vo.UploadFileVO;

public interface UploadFileService extends IService<UploadFile> {

    Page<UploadFileVO> searchByPage(UploadFileQueryDTO param);

    void delFile(String id);

}
