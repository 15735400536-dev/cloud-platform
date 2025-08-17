package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.TaskOrderQueryDTO;
import com.maxinhai.platform.po.TaskOrder;
import com.maxinhai.platform.vo.TaskOrderVO;

public interface TaskOrderService extends IService<TaskOrder> {

    Page<TaskOrderVO> searchByPage(TaskOrderQueryDTO param);

    TaskOrderVO getInfo(String id);

    void remove(String[] ids);

    /**
     * 开工
     * @param taskOrderId 派工单ID
     */
    void startWork(String taskOrderId);

    /**
     * 暂停
     * @param taskOrderId 派工单ID
     */
    void pauseWork(String taskOrderId);

    /**
     * 复工
     * @param taskOrderId 派工单ID
     */
    void resumeWork(String taskOrderId);

    /**
     * 报工
     * @param taskOrderId 派工单ID
     */
    void reportWork(String taskOrderId);

    /**
     * 获取上道派工单
     * @param workOrderId 工单ID
     * @param taskOrderId 派工单ID
     * @return
     */
    TaskOrder getPreTaskOrder(String workOrderId, String taskOrderId);

    /**
     * 获取下道派工单
     * @param workOrderId 工单ID
     * @param taskOrderId 派工单ID
     * @return
     */
    TaskOrder getNextTaskOrder(String workOrderId, String taskOrderId);

    /**
     * 检测订单开工状态
     * @param workOrderId 工单ID
     * @return true.已开工 false.未开工
     */
    boolean checkOrderStart(String workOrderId);

    /**
     * 检测订单报工状态
     * @param workOrderId 工单ID
     * @return true.已报工 false.未报工
     */
    boolean checkOrderReport(String workOrderId);

    /**
     * 检测工单开工状态
     * @param orderId 订单ID
     * @return
     */
    boolean checkWorkOrderStart(String orderId);

    /**
     * 检测工单报工状态
     * @param orderId 订单ID
     * @return
     */
    boolean checkWorkOrderReport(String orderId);

    /**
     * 统计今日工单完成数量
     * @return
     */
    long getTodayFinishTaskOrderCount();

}
