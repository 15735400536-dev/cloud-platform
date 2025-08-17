package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.CheckOrderDetailAddDTO;
import com.maxinhai.platform.dto.CheckOrderDetailEditDTO;
import com.maxinhai.platform.dto.CheckOrderDetailQueryDTO;
import com.maxinhai.platform.po.CheckOrderDetail;
import com.maxinhai.platform.vo.CheckOrderDetailVO;

public interface CheckOrderDetailService extends IService<CheckOrderDetail> {

    Page<CheckOrderDetailVO> searchByPage(CheckOrderDetailQueryDTO param);

    CheckOrderDetailVO getInfo(String id);

    void remove(String[] ids);

    void edit(CheckOrderDetailEditDTO param);

    void add(CheckOrderDetailAddDTO param);

}
