package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.MaintenancePlanAddDTO;
import com.maxinhai.platform.dto.MaintenancePlanEditDTO;
import com.maxinhai.platform.dto.MaintenancePlanQueryDTO;
import com.maxinhai.platform.po.MaintenancePlan;
import com.maxinhai.platform.vo.MaintenancePlanVO;

public interface MaintenancePlanService extends IService<MaintenancePlan> {

    Page<MaintenancePlanVO> searchByPage(MaintenancePlanQueryDTO param);

    MaintenancePlanVO getInfo(String id);

    void remove(String[] ids);

    void edit(MaintenancePlanEditDTO param);

    void add(MaintenancePlanAddDTO param);

}
