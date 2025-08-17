package com.maxinhai.platform.service.model;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.model.WarehouseAreaAddDTO;
import com.maxinhai.platform.dto.model.WarehouseAreaEditDTO;
import com.maxinhai.platform.dto.model.WarehouseAreaQueryDTO;
import com.maxinhai.platform.po.model.WarehouseArea;
import com.maxinhai.platform.vo.model.WarehouseAreaVO;

public interface WarehouseAreaService extends IService<WarehouseArea> {

    Page<WarehouseAreaVO> searchByPage(WarehouseAreaQueryDTO param);

    WarehouseAreaVO getInfo(String id);

    void remove(String[] ids);

    void edit(WarehouseAreaEditDTO param);

    void add(WarehouseAreaAddDTO param);

}
