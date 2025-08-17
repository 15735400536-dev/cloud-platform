package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.DataDictAddDTO;
import com.maxinhai.platform.dto.DataDictEditDTO;
import com.maxinhai.platform.dto.DataDictQueryDTO;
import com.maxinhai.platform.po.DataDict;
import com.maxinhai.platform.vo.DataDictVO;

public interface DataDictService extends IService<DataDict> {

    Page<DataDictVO> searchByPage(DataDictQueryDTO param);

    DataDictVO getInfo(String id);

    void remove(String[] ids);

    void edit(DataDictEditDTO param);

    void add(DataDictAddDTO param);

}
