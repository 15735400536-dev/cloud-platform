package com.maxinhai.platform.service.inventory;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.inventory.InventoryQueryDTO;
import com.maxinhai.platform.po.inventory.Inventory;
import com.maxinhai.platform.vo.inventory.InventoryVO;

public interface InventoryService extends IService<Inventory> {

    Page<InventoryVO> searchByPage(InventoryQueryDTO param);

    InventoryVO getInfo(String id);
}
