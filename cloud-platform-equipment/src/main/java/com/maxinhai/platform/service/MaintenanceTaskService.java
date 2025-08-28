package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.MaintenanceTaskQueryDTO;
import com.maxinhai.platform.po.MaintenanceTask;
import com.maxinhai.platform.vo.MaintenanceTaskVO;

public interface MaintenanceTaskService extends IService<MaintenanceTask> {

    Page<MaintenanceTaskVO> searchByPage(MaintenanceTaskQueryDTO param);

    MaintenanceTaskVO getInfo(String id);

}
