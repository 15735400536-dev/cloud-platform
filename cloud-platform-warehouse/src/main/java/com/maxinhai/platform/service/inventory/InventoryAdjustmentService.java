package com.maxinhai.platform.service.inventory;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.inventory.InventoryAdjustmentQueryDTO;
import com.maxinhai.platform.po.inventory.InventoryAdjustment;
import com.maxinhai.platform.vo.inventory.InventoryAdjustmentVO;

public interface InventoryAdjustmentService extends IService<InventoryAdjustment> {

    Page<InventoryAdjustmentVO> searchByPage(InventoryAdjustmentQueryDTO param);

    InventoryAdjustmentVO getInfo(String id);

}
