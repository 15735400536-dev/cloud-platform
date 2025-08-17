package com.maxinhai.platform.service.model;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.model.FactoryAddDTO;
import com.maxinhai.platform.dto.model.FactoryEditDTO;
import com.maxinhai.platform.dto.model.FactoryQueryDTO;
import com.maxinhai.platform.po.model.Factory;
import com.maxinhai.platform.vo.model.FactoryVO;

public interface FactoryService extends IService<Factory> {

    Page<FactoryVO> searchByPage(FactoryQueryDTO param);

    FactoryVO getInfo(String id);

    void remove(String[] ids);

    void edit(FactoryEditDTO param);

    void add(FactoryAddDTO param);

}
