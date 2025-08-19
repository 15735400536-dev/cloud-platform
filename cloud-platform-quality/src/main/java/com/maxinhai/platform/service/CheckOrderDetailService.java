package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.CheckOrderDetailAddDTO;
import com.maxinhai.platform.dto.CheckOrderDetailEditDTO;
import com.maxinhai.platform.dto.CheckOrderDetailQueryDTO;
import com.maxinhai.platform.po.CheckOrderDetail;
import com.maxinhai.platform.vo.CheckOrderDetailVO;

import java.util.List;

public interface CheckOrderDetailService extends IService<CheckOrderDetail> {

    Page<CheckOrderDetailVO> searchByPage(CheckOrderDetailQueryDTO param);

    CheckOrderDetailVO getInfo(String id);

    void remove(String[] ids);

    void edit(CheckOrderDetailEditDTO param);

    void add(CheckOrderDetailAddDTO param);

    /**
     * 根据质检单ID查询检测项列表
     * @param checkOrderId
     * @return
     */
    List<CheckOrderDetailVO> getCheckItemList(String checkOrderId);

    /**
     * 填写质检单
     * @param itemList
     */
    void filing(List<CheckOrderDetailEditDTO> itemList);

}
