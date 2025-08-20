package com.maxinhai.platform.service.inventory;

import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.enums.OperateType;
import com.maxinhai.platform.po.inventory.Inventory;
import com.maxinhai.platform.po.inventory.InventoryFlow;
import com.maxinhai.platform.po.order.IssueOrderDetail;
import com.maxinhai.platform.po.order.ReceiptOrderDetail;
import com.maxinhai.platform.po.order.TransferOrderDetail;

import java.math.BigDecimal;

public interface InventoryFlowService extends IService<InventoryFlow> {

    void createFlow(TransferOrderDetail orderDetail, BigDecimal beforeQty, BigDecimal afterQt);

    void createFlow(ReceiptOrderDetail orderDetail, BigDecimal beforeQty, BigDecimal afterQt);

    void createFlow(IssueOrderDetail orderDetail, BigDecimal beforeQty, BigDecimal afterQt);

    void createFlow(OperateType operateType, Inventory inventory, BigDecimal changeQty, BigDecimal beforeQty, BigDecimal afterQt);

}
