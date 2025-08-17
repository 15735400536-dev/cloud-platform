package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.InspectionPlanAddDTO;
import com.maxinhai.platform.dto.InspectionPlanEditDTO;
import com.maxinhai.platform.dto.InspectionPlanQueryDTO;
import com.maxinhai.platform.po.InspectionPlan;
import com.maxinhai.platform.vo.InspectionPlanVO;

public interface InspectionPlanService extends IService<InspectionPlan> {

    Page<InspectionPlanVO> searchByPage(InspectionPlanQueryDTO param);

    InspectionPlanVO getInfo(String id);

    void remove(String[] ids);

    void edit(InspectionPlanEditDTO param);

    void add(InspectionPlanAddDTO param);

}
