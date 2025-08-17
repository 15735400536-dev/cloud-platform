package com.maxinhai.platform.service.order;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.order.ReceiptOrderDetailAddDTO;
import com.maxinhai.platform.dto.order.ReceiptOrderDetailEditDTO;
import com.maxinhai.platform.dto.order.ReceiptOrderDetailQueryDTO;
import com.maxinhai.platform.po.order.ReceiptOrderDetail;
import com.maxinhai.platform.vo.order.ReceiptOrderDetailVO;

public interface ReceiptOrderDetailService extends IService<ReceiptOrderDetail> {

    Page<ReceiptOrderDetailVO> searchByPage(ReceiptOrderDetailQueryDTO param);

    ReceiptOrderDetailVO getInfo(String id);

    void remove(String[] ids);

    void edit(ReceiptOrderDetailEditDTO param);

    void add(ReceiptOrderDetailAddDTO param);

    /**
     * 入库单入库
     * @param id
     */
    void receipt(String id);

}
