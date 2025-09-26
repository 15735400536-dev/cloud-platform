package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.SupplierAddDTO;
import com.maxinhai.platform.dto.SupplierEditDTO;
import com.maxinhai.platform.dto.SupplierQueryDTO;
import com.maxinhai.platform.po.Supplier;
import com.maxinhai.platform.vo.SupplierVO;

public interface SupplierService extends IService<Supplier> {

    Page<SupplierVO> searchByPage(SupplierQueryDTO param);

    SupplierVO getInfo(String id);

    void remove(String[] ids);

    void edit(SupplierEditDTO param);

    void add(SupplierAddDTO param);

}
