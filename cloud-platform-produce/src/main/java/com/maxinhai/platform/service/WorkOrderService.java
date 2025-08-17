package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.WorkOrderQueryDTO;
import com.maxinhai.platform.po.WorkOrder;
import com.maxinhai.platform.vo.WorkOrderVO;

public interface WorkOrderService extends IService<WorkOrder> {

    Page<WorkOrderVO> searchByPage(WorkOrderQueryDTO param);

    WorkOrderVO getInfo(String id);

    void remove(String[] ids);

    /**
     * 统计今日工单完成数量
     * @return
     */
    long getTodayFinishWorkOrderCount();

}
