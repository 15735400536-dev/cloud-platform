package com.maxinhai.platform.service.model;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.model.WarehouseAddDTO;
import com.maxinhai.platform.dto.model.WarehouseEditDTO;
import com.maxinhai.platform.dto.model.WarehouseQueryDTO;
import com.maxinhai.platform.po.model.Warehouse;
import com.maxinhai.platform.vo.model.WarehouseVO;

public interface WarehouseService extends IService<Warehouse> {

    Page<WarehouseVO> searchByPage(WarehouseQueryDTO param);

    WarehouseVO getInfo(String id);

    void remove(String[] ids);

    void edit(WarehouseEditDTO param);

    void add(WarehouseAddDTO param);

}
