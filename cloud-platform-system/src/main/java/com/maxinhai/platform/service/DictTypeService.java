package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.DictTypeAddDTO;
import com.maxinhai.platform.dto.DictTypeEditDTO;
import com.maxinhai.platform.dto.DictTypeQueryDTO;
import com.maxinhai.platform.po.DictType;
import com.maxinhai.platform.vo.DictTypeVO;

public interface DictTypeService extends IService<DictType> {

    Page<DictTypeVO> searchByPage(DictTypeQueryDTO param);

    DictTypeVO getInfo(String id);

    void remove(String[] ids);

    void edit(DictTypeEditDTO param);

    void add(DictTypeAddDTO param);

}
