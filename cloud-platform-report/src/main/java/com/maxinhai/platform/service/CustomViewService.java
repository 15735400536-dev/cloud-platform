package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.CustomViewAddDTO;
import com.maxinhai.platform.dto.CustomViewEditDTO;
import com.maxinhai.platform.dto.CustomViewQueryDTO;
import com.maxinhai.platform.po.CustomView;
import com.maxinhai.platform.vo.CustomViewVO;

public interface CustomViewService extends IService<CustomView> {

    Page<CustomViewVO> searchByPage(CustomViewQueryDTO param);

    CustomViewVO getInfo(String id);

    void remove(String[] ids);

    void edit(CustomViewEditDTO param);

    void add(CustomViewAddDTO param);

}
