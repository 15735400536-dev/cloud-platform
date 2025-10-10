package com.maxinhai.platform.service.order;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.order.TransferOrderAddDTO;
import com.maxinhai.platform.dto.order.TransferOrderEditDTO;
import com.maxinhai.platform.dto.order.TransferOrderQueryDTO;
import com.maxinhai.platform.po.order.TransferOrder;
import com.maxinhai.platform.vo.order.TransferOrderVO;

public interface TransferOrderService extends IService<TransferOrder> {

    Page<TransferOrderVO> searchByPage(TransferOrderQueryDTO param);

    TransferOrderVO getInfo(String id);

    void remove(String[] ids);

    void edit(TransferOrderEditDTO param);

    void add(TransferOrderAddDTO param);

    /**
     * 根据移库单ID移库
     * @param id 主键ID
     */
    void transfer(String id);

}
