package com.maxinhai.platform.service.order;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.order.TransferOrderDetailAddDTO;
import com.maxinhai.platform.dto.order.TransferOrderDetailEditDTO;
import com.maxinhai.platform.dto.order.TransferOrderDetailQueryDTO;
import com.maxinhai.platform.po.order.TransferOrderDetail;
import com.maxinhai.platform.vo.order.TransferOrderDetailVO;

public interface TransferOrderDetailService extends IService<TransferOrderDetail> {

    Page<TransferOrderDetailVO> searchByPage(TransferOrderDetailQueryDTO param);

    TransferOrderDetailVO getInfo(String id);

    void remove(String[] ids);

    void edit(TransferOrderDetailEditDTO param);

    void add(TransferOrderDetailAddDTO param);

    /**
     * 移库单移库
     * @param id 主键ID
     */
    void transfer(String id);

}
