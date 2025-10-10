package com.maxinhai.platform.service.order;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.order.ReceiptOrderAddDTO;
import com.maxinhai.platform.dto.order.ReceiptOrderEditDTO;
import com.maxinhai.platform.dto.order.ReceiptOrderQueryDTO;
import com.maxinhai.platform.po.order.ReceiptOrder;
import com.maxinhai.platform.vo.order.ReceiptOrderVO;

public interface ReceiptOrderService extends IService<ReceiptOrder> {

    Page<ReceiptOrderVO> searchByPage(ReceiptOrderQueryDTO param);

    ReceiptOrderVO getInfo(String id);

    void remove(String[] ids);

    void edit(ReceiptOrderEditDTO param);

    void add(ReceiptOrderAddDTO param);

    /**
     * 根据出库单ID出库
     * @param id 主键ID
     */
    void receipt(String id);

}
