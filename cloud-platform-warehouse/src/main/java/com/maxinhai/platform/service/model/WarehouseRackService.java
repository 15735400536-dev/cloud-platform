package com.maxinhai.platform.service.model;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.model.WarehouseRackAddDTO;
import com.maxinhai.platform.dto.model.WarehouseRackEditDTO;
import com.maxinhai.platform.dto.model.WarehouseRackQueryDTO;
import com.maxinhai.platform.po.model.WarehouseRack;
import com.maxinhai.platform.vo.model.WarehouseRackVO;

public interface WarehouseRackService extends IService<WarehouseRack> {

    Page<WarehouseRackVO> searchByPage(WarehouseRackQueryDTO param);

    WarehouseRackVO getInfo(String id);

    void remove(String[] ids);

    void edit(WarehouseRackEditDTO param);

    void add(WarehouseRackAddDTO param);

}
