package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.RepairTaskQueryDTO;
import com.maxinhai.platform.po.RepairTask;
import com.maxinhai.platform.vo.RepairTaskVO;

public interface RepairTaskService extends IService<RepairTask> {

    Page<RepairTaskVO> searchByPage(RepairTaskQueryDTO param);

    RepairTaskVO getInfo(String id);

}
