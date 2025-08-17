package com.maxinhai.platform.service.model;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.model.WorkshopAddDTO;
import com.maxinhai.platform.dto.model.WorkshopEditDTO;
import com.maxinhai.platform.dto.model.WorkshopQueryDTO;
import com.maxinhai.platform.po.model.Workshop;
import com.maxinhai.platform.vo.model.WorkshopVO;

public interface WorkshopService extends IService<Workshop> {

    Page<WorkshopVO> searchByPage(WorkshopQueryDTO param);

    WorkshopVO getInfo(String id);

    void remove(String[] ids);

    void edit(WorkshopEditDTO param);

    void add(WorkshopAddDTO param);

}
