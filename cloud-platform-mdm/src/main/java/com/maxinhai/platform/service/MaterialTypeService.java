package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.MaterialTypeAddDTO;
import com.maxinhai.platform.dto.MaterialTypeEditDTO;
import com.maxinhai.platform.dto.MaterialTypeQueryDTO;
import com.maxinhai.platform.po.MaterialType;
import com.maxinhai.platform.vo.MaterialTypeTreeVO;
import com.maxinhai.platform.vo.MaterialTypeVO;

import java.util.List;

public interface MaterialTypeService extends IService<MaterialType> {

    Page<MaterialTypeVO> searchByPage(MaterialTypeQueryDTO param);

    MaterialTypeVO getInfo(String id);

    void remove(String[] ids);

    void edit(MaterialTypeEditDTO param);

    void add(MaterialTypeAddDTO param);

    /**
     * 获取物料类型树状结构
     */
    List<MaterialTypeTreeVO> getMaterialTypeTree();

}
