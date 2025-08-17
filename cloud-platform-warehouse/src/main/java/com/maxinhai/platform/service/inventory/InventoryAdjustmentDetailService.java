package com.maxinhai.platform.service.inventory;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.inventory.InventoryAdjustmentDetailAddDTO;
import com.maxinhai.platform.dto.inventory.InventoryAdjustmentDetailEditDTO;
import com.maxinhai.platform.dto.inventory.InventoryAdjustmentDetailQueryDTO;
import com.maxinhai.platform.po.inventory.InventoryAdjustmentDetail;
import com.maxinhai.platform.vo.inventory.InventoryAdjustmentDetailVO;

public interface InventoryAdjustmentDetailService extends IService<InventoryAdjustmentDetail> {

    Page<InventoryAdjustmentDetailVO> searchByPage(InventoryAdjustmentDetailQueryDTO param);

    InventoryAdjustmentDetailVO getInfo(String id);

    void remove(String[] ids);

    void edit(InventoryAdjustmentDetailEditDTO param);

    void add(InventoryAdjustmentDetailAddDTO param);

    /**
     * 库存调整
     * @param id
     */
    void adjustment(String id);

}
