package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.bo.DailyProcessFinishTaskOrderQtyBO;
import com.maxinhai.platform.dto.TaskOrderQueryDTO;
import com.maxinhai.platform.po.TaskOrder;
import com.maxinhai.platform.vo.DailyOpTaskOrderVO;
import com.maxinhai.platform.vo.DailyTaskOrderVO;
import com.maxinhai.platform.vo.TaskOrderVO;

import java.util.List;

public interface TaskOrderService extends IService<TaskOrder> {

    /**
     * 分页查询（7张表关联+分页统计+时间范围查询，这种查询非常慢）
     * @param param 分页查询参数
     * @return 分页数据
     */
    Page<TaskOrderVO> searchByPage(TaskOrderQueryDTO param);

    /**
     * 分页查询优化版（方案3）
     * @param param 分页查询参数
     * 优化方案：
     *     方案1：给主表和关联表新建索引（最简单、最稳、最推荐），结论：单表有400W数据，新建索引查询效率提升不大；
     *     方案2：宽表冗余，将关联表查询字段加到宽表（适合高并发、统计型接口），优点：无join，缺点：改动大，数据需要同步更新，有冗余数据；
     *     方案3：延迟关联/分步查询：分页先查主表 + 后关联子表，优点：改动小，缺点：未在主表存储的字段无法条件查询；
     * @return 分页数据
     */
    Page<TaskOrderVO> searchByPageEx(TaskOrderQueryDTO param);

    TaskOrderVO getInfo(String id);

    void remove(String[] ids);

    /**
     * 开工
     *
     * @param taskOrderId 派工单ID
     */
    void startWork(String taskOrderId);

    /**
     * 暂停
     *
     * @param taskOrderId 派工单ID
     */
    void pauseWork(String taskOrderId);

    /**
     * 复工
     *
     * @param taskOrderId 派工单ID
     */
    void resumeWork(String taskOrderId);

    /**
     * 报工
     *
     * @param taskOrderId 派工单ID
     */
    void reportWork(String taskOrderId);

    /**
     * 获取上道派工单
     *
     * @param workOrderId 工单ID
     * @param taskOrderId 派工单ID
     * @return 上道派工单
     */
    TaskOrder getPreTaskOrder(String workOrderId, String taskOrderId);

    /**
     * 获取下道派工单
     *
     * @param workOrderId 工单ID
     * @param taskOrderId 派工单ID
     * @return 下道派工单
     */
    TaskOrder getNextTaskOrder(String workOrderId, String taskOrderId);

    /**
     * 检测订单开工状态（有工单开工，则订单开工）
     *
     * @param workOrderId 工单ID
     * @return true.可开工 false.不可开工
     */
    boolean checkOrderStart(String workOrderId);

    /**
     * 检测订单报工状态
     *
     * @param workOrderId 工单ID
     * @return true.可报工 false.不可报工
     */
    boolean checkOrderReport(String workOrderId);

    /**
     * 检测工单开工状态（第一道派工单开工，则工单可开工）
     *
     * @param orderId 订单ID
     * @return true.可开工 false.不可开工
     */
    boolean checkWorkOrderStart(String orderId);

    /**
     * 检测工单报工状态
     *
     * @param orderId 订单ID
     * @return true.可报工 false.不可报工
     */
    boolean checkWorkOrderReport(String orderId);

    /**
     * 统计今日工单完成数量
     *
     * @return 今日工单完成数量
     */
    long getTodayFinishTaskOrderCount();

    /**
     * 查询每天每道工序派工单完成数量
     *
     * @return 每天每道工序派工单完成数量
     */

    List<DailyProcessFinishTaskOrderQtyBO> queryDailyProcessFinishTaskOrderQty();

    /**
     * 统计每日派工单完成数量
     *
     * @return 每日派工单完成数量
     */
    DailyTaskOrderVO countDailyTaskOrder();

    /**
     * 统计每日每道工序派工单完成数量
     *
     * @return 每日每道工序派工单完成数量
     */
    DailyOpTaskOrderVO countDailyOpTaskOrder();

    /**
     * 统计每日每道工序派工单完成数量（优化版）
     *
     * @return 每日每道工序派工单完成数量
     */
    DailyOpTaskOrderVO countDailyOpTaskOrderEx();

}
