package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.OrderAddDTO;
import com.maxinhai.platform.dto.OrderQueryDTO;
import com.maxinhai.platform.po.Order;
import com.maxinhai.platform.vo.OrderProgressVO;
import com.maxinhai.platform.vo.OrderVO;

import java.util.concurrent.ExecutionException;

public interface OrderService extends IService<Order> {

    Page<OrderVO> searchByPage(OrderQueryDTO param);

    OrderVO getInfo(String id);

    void remove(String[] ids);

    void add(OrderAddDTO param);

    /**
     * 统计今日订单完成数量
     * @return 今日订单完成数量
     */
    long getTodayFinishOrderCount();

    /**
     * 订单进度统计（效率低下，响应时间4-15秒）
     * @return 统计结果
     */
    OrderProgressVO orderProgress();

    /**
     * 订单进度统计（效率高，响应时间1秒以内）
     * @return 统计结果
     */
    OrderProgressVO orderProgressEx();

    /**
     * 订单进度统计（效率高，减少数据库交互次数）
     * @return 统计结果
     */
    OrderProgressVO orderProgressEx1();

    /**
     * 订单进度统计（效率高，减少数据库交互次数，减少查询时数据）
     * 添加组合索引，提升单表查询效率：
     *    CREATE INDEX idx_order_status_actual_end_time ON "prod_order"(order_status, actual_end_time);
     *    CREATE INDEX idx_work_order_status_actual_end_time ON "prod_work_order"(order_status, actual_end_time);
     *    CREATE INDEX idx_task_order_status_actual_end_time ON "prod_task_order"(status, actual_end_time);
     * @return 统计结果
     */
    OrderProgressVO orderProgressEx2();

    /**
     * 订单进度统计（效率高，减少数据库交互次数，减少查询时数据，多线程异步查询，增加数据库瞬时压力）
     * @return 统计结果
     */
    OrderProgressVO orderProgressEx3() throws ExecutionException, InterruptedException;

}
