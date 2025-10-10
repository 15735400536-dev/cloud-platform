package com.maxinhai.platform.service.model;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.WorkCenterAddDTO;
import com.maxinhai.platform.dto.WorkCenterEditDTO;
import com.maxinhai.platform.dto.WorkCenterQueryDTO;
import com.maxinhai.platform.po.model.WorkCenter;
import com.maxinhai.platform.vo.WorkCenterVO;

public interface WorkCenterService extends IService<WorkCenter> {

    Page<WorkCenterVO> searchByPage(WorkCenterQueryDTO param);

    WorkCenterVO getInfo(String id);

    void remove(String[] ids);

    void edit(WorkCenterEditDTO param);

    void add(WorkCenterAddDTO param);
    
}
