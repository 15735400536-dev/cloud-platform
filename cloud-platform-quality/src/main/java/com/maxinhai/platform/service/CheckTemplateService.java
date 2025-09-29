package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.bo.CheckTemplateExcel;
import com.maxinhai.platform.dto.CheckTemplateAddDTO;
import com.maxinhai.platform.dto.CheckTemplateEditDTO;
import com.maxinhai.platform.dto.CheckTemplateQueryDTO;
import com.maxinhai.platform.po.CheckTemplate;
import com.maxinhai.platform.vo.CheckTemplateVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CheckTemplateService extends IService<CheckTemplate> {

    Page<CheckTemplateVO> searchByPage(CheckTemplateQueryDTO param);

    CheckTemplateVO getInfo(String id);

    void remove(String[] ids);

    void edit(CheckTemplateEditDTO param);

    void add(CheckTemplateAddDTO param);

    void importExcel(MultipartFile file);

    /**
     * 保存excel数据
     * @param dataList excel数据
     */
    void saveExcelData(List<CheckTemplateExcel> dataList);

}
