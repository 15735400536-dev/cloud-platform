package com.maxinhai.platform.service.model;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.model.WarehouseLocationAddDTO;
import com.maxinhai.platform.dto.model.WarehouseLocationEditDTO;
import com.maxinhai.platform.dto.model.WarehouseLocationQueryDTO;
import com.maxinhai.platform.po.model.WarehouseLocation;
import com.maxinhai.platform.vo.model.WarehouseLocationVO;

public interface WarehouseLocationService extends IService<WarehouseLocation> {

    Page<WarehouseLocationVO> searchByPage(WarehouseLocationQueryDTO param);

    WarehouseLocationVO getInfo(String id);

    void remove(String[] ids);

    void edit(WarehouseLocationEditDTO param);

    void add(WarehouseLocationAddDTO param);

}
