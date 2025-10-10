package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.DiaryTypeAddDTO;
import com.maxinhai.platform.dto.DiaryTypeEditDTO;
import com.maxinhai.platform.dto.DiaryTypeQueryDTO;
import com.maxinhai.platform.po.DiaryType;
import com.maxinhai.platform.vo.DiaryTypeVO;

public interface DiaryTypeService extends IService<DiaryType> {

    Page<DiaryTypeVO> searchByPage(DiaryTypeQueryDTO param);

    DiaryTypeVO getInfo(String id);

    void remove(String[] ids);

    void edit(DiaryTypeEditDTO param);

    void add(DiaryTypeAddDTO param);
    
}
