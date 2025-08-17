package com.maxinhai.platform.service.model;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.model.ProductionLineAddDTO;
import com.maxinhai.platform.dto.model.ProductionLineEditDTO;
import com.maxinhai.platform.dto.model.ProductionLineQueryDTO;
import com.maxinhai.platform.po.model.ProductionLine;
import com.maxinhai.platform.vo.model.ProductionLineVO;

public interface ProductionLineService extends IService<ProductionLine> {

    Page<ProductionLineVO> searchByPage(ProductionLineQueryDTO param);

    ProductionLineVO getInfo(String id);

    void remove(String[] ids);

    void edit(ProductionLineEditDTO param);

    void add(ProductionLineAddDTO param);

}
