package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.CheckItemAddDTO;
import com.maxinhai.platform.dto.CheckItemEditDTO;
import com.maxinhai.platform.dto.CheckItemQueryDTO;
import com.maxinhai.platform.po.CheckItem;
import com.maxinhai.platform.vo.CheckItemVO;

public interface CheckItemService extends IService<CheckItem> {

    Page<CheckItemVO> searchByPage(CheckItemQueryDTO param);

    CheckItemVO getInfo(String id);

    void remove(String[] ids);

    void edit(CheckItemEditDTO param);

    void add(CheckItemAddDTO param);

}
