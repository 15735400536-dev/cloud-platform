package com.maxinhai.platform.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import com.maxinhai.platform.bo.OrderInfoBO;
import com.maxinhai.platform.po.Order;
import com.maxinhai.platform.vo.OrderProgressVO;
import com.maxinhai.platform.vo.OrderStatisticsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderMapper extends MPJBaseMapper<Order> {

    /**
     * 统计订单总数
     * @return 订单总数
     */
    @Select(value = "select count(*) from prod_order where del_flag = 0")
    long countOrderTotalQty();

    /**
     * 统计订单完工数量
     * @return 订单完工数量
     */
    @Select(value = "select count(*) from prod_order where del_flag = 0 and order_status = 4")
    long countOrderFinishQty();

    /**
     * 统计订单未完工数量
     * @return 订单未完工数量
     */
    @Select(value = "select count(*) from prod_order where del_flag = 0 and order_status < 4")
    long countOrderUnFinishQty();

    /**
     * 统计今日订单完工数量
     * @return 今日订单完工数量
     */
    @Select(value = "select count(*) from prod_order " +
            "where del_flag = 0 and order_status = 4 " +
            "and actual_end_time between date_trunc('day', CURRENT_TIMESTAMP) and (date_trunc('day', CURRENT_TIMESTAMP) + INTERVAL '1 day - 1 microsecond')")
    long countOrderTodayFinishQty();

    /**
     * 统计订单完工天数
     * @return 订单完工天数
     */
    @Select(value = "select count(DISTINCT DATE(actual_end_time)) as finish_days from prod_order " +
            "where del_flag = 0 and order_status = 4 " +
            "and actual_end_time is not null")
    long countOrderFinishDays();

    /**
     * 统计工单总数
     * @return 工单总数
     */
    @Select(value = "select count(*) from prod_work_order where del_flag = 0")
    long countWorkOrderTotalQty();

    /**
     * 统计工单完工数量
     * @return 工单完工数量
     */
    @Select(value = "select count(*) from prod_work_order where del_flag = 0 and order_status = 4")
    long countWorkOrderFinishQty();

    /**
     * 统计工单未完工数量
     * @return 工单未完工数量
     */
    @Select(value = "select count(*) from prod_work_order where del_flag = 0 and order_status < 4")
    long countWorkOrderUnFinishQty();

    /**
     * 统计今日工单完工数量
     * @return 今日工单完工数量
     */
    @Select(value = "select count(*) from prod_work_order " +
            "where del_flag = 0 and order_status = 4 " +
            "and actual_end_time between date_trunc('day', CURRENT_TIMESTAMP) and (date_trunc('day', CURRENT_TIMESTAMP) + INTERVAL '1 day - 1 microsecond')")
    long countWorkOrderTodayFinishQty();

    /**
     * 统计工单完工天数
     * @return 工单完工天数
     */
    @Select(value = "select count(DISTINCT DATE(actual_end_time)) as finish_days from prod_work_order " +
            "where del_flag = 0 and order_status = 4 " +
            "and actual_end_time is not null")
    long countWorkOrderFinishDays();

    /**
     * 统计派工单总数
     * @return 派工单总数
     */
    @Select(value = "select count(*) from prod_task_order where del_flag = 0")
    long countTaskOrderTotalQty();

    /**
     * 统计派工单完工数量
     * @return 派工单完工数量
     */
    @Select(value = "select count(*) from prod_task_order " +
            "where del_flag = 0 and status = 4")
    long countTaskOrderFinishQty();

    /**
     * 统计派工单未完工数量
     * @return 派工单未完工数量
     */
    @Select(value = "select count(*) from prod_task_order " +
            "where del_flag = 0 and status < 4")
    long countTaskOrderUnFinishQty();

    /**
     * 统计今日派工单完工数量
     * @return 今日派工单完工数量
     */
    @Select(value = "select count(*) from prod_task_order " +
            "where del_flag = 0 and status = 4 " +
            "and actual_end_time between date_trunc('day', CURRENT_TIMESTAMP) and (date_trunc('day', CURRENT_TIMESTAMP) + INTERVAL '1 day - 1 microsecond')")
    long countTaskOrderTodayFinishQty();

    /**
     * 统计派工单完工天数
     * @return 派工单完工天数
     */
    @Select(value = "select count(DISTINCT DATE(actual_end_time)) as finish_days from prod_task_order " +
            "where del_flag = 0 and status = 4 " +
            "and actual_end_time is not null")
    long countTaskOrderFinishDays();

    /**
     * 统计订单信息
     * @return 订单信息
     */
    @Select(value = "SELECT " +
            "    COUNT(*) AS total_qty, " +
            "    SUM(CASE WHEN order_status = 4 THEN 1 ELSE 0 END) AS finish_qty, " +
            "    SUM(CASE WHEN order_status != 4 THEN 1 ELSE 0 END) AS unfinish_qty, " +
            "    SUM(CASE WHEN order_status = 4 AND actual_end_time >= CURRENT_DATE THEN 1 ELSE 0 END) AS today_finish_qty, " +
            "    COUNT(DISTINCT DATE(actual_end_time)) AS finish_days " +
            "FROM prod_order WHERE del_flag = 0")
    OrderInfoBO countOrderInfo();

    /**
     * 统计工单信息
     * @return 工单信息
     */
    @Select(value = "SELECT " +
            "    COUNT(*) AS total_qty, " +
            "    SUM(CASE WHEN order_status = 4 THEN 1 ELSE 0 END) AS finish_qty, " +
            "    SUM(CASE WHEN order_status != 4 THEN 1 ELSE 0 END) AS unfinish_qty, " +
            "    SUM(CASE WHEN order_status = 4 AND actual_end_time >= CURRENT_DATE THEN 1 ELSE 0 END) AS today_finish_qty, " +
            "    COUNT(DISTINCT DATE(actual_end_time)) AS finish_days " +
            "FROM prod_work_order WHERE del_flag = 0")
    OrderInfoBO countWorkOrderInfo();

    /**
     * 统计派工单信息
     * @return 派工单信息
     */
    @Select(value = "SELECT " +
            "    COUNT(*) AS total_qty, " +
            "    SUM(CASE WHEN status = 4 THEN 1 ELSE 0 END) AS finish_qty, " +
            "    SUM(CASE WHEN status != 4 THEN 1 ELSE 0 END) AS unfinish_qty, " +
            "    SUM(CASE WHEN status = 4 AND actual_end_time >= CURRENT_DATE THEN 1 ELSE 0 END) AS today_finish_qty, " +
            "    COUNT(DISTINCT DATE(actual_end_time)) AS finish_days " +
            "FROM prod_task_order WHERE del_flag = 0")
    OrderInfoBO countTaskOrderInfo();

    /**
     * 统计订单信息
     * @return 订单信息
     */
    @Select(value = "SELECT " +
            "    COUNT(*) AS total_qty, " +
            "    SUM(CASE WHEN order_status = 4 THEN 1 ELSE 0 END) AS finish_qty, " +
            "    SUM(CASE WHEN order_status = 4 AND actual_end_time >= CURRENT_DATE THEN 1 ELSE 0 END) AS today_finish_qty, " +
            "    COUNT(DISTINCT DATE(actual_end_time)) AS finish_days " +
            "FROM prod_order WHERE del_flag = 0")
    OrderInfoBO countOrderSimpleInfo();

    /**
     * 统计工单信息
     * @return 工单信息
     */
    @Select(value = "SELECT " +
            "    COUNT(*) AS total_qty, " +
            "    SUM(CASE WHEN order_status = 4 THEN 1 ELSE 0 END) AS finish_qty, " +
            "    SUM(CASE WHEN order_status = 4 AND actual_end_time >= CURRENT_DATE THEN 1 ELSE 0 END) AS today_finish_qty, " +
            "    COUNT(DISTINCT DATE(actual_end_time)) AS finish_days " +
            "FROM prod_work_order WHERE del_flag = 0")
    OrderInfoBO countWorkOrderSimpleInfo();

    /**
     * 统计派工单信息
     * @return 派工单信息
     */
    @Select(value = "SELECT " +
            "    COUNT(*) AS total_qty, " +
            "    SUM(CASE WHEN status = 4 THEN 1 ELSE 0 END) AS finish_qty, " +
            "    SUM(CASE WHEN status = 4 AND actual_end_time >= CURRENT_DATE THEN 1 ELSE 0 END) AS today_finish_qty, " +
            "    COUNT(DISTINCT DATE(actual_end_time)) AS finish_days " +
            "FROM prod_task_order WHERE del_flag = 0")
    OrderInfoBO countTaskOrderSimpleInfo();

    @Select("SELECT "
            + "(SELECT COUNT(*) FROM prod_order) AS orderTotalQty, "
            + "(SELECT COUNT(*) FROM prod_order WHERE order_status=4) AS orderFinishQty, "
            + "(SELECT COUNT(*) FROM prod_order WHERE order_status!=4) AS orderUnFinishQty, "

            + "(SELECT COUNT(*) FROM prod_work_order) AS workOrderTotalQty, "
            + "(SELECT COUNT(*) FROM prod_work_order WHERE order_status=4) AS workOrderFinishQty, "
            + "(SELECT COUNT(*) FROM prod_work_order WHERE order_status!=4) AS workOrderUnFinishQty, "

            + "(SELECT COUNT(*) FROM prod_task_order) AS taskOrderTotalQty, "
            + "(SELECT COUNT(*) FROM prod_task_order WHERE status=4) AS taskOrderFinishQty, "
            + "(SELECT COUNT(*) FROM prod_task_order WHERE status!=4) AS taskOrderUnFinishQty, "

            + "(SELECT COUNT(*) FROM prod_order WHERE actual_end_time >= CURRENT_DATE) AS orderTodayFinishQty, "
            + "(SELECT COUNT(*) FROM prod_work_order WHERE actual_end_time >= CURRENT_DATE) AS workOrderTodayFinishQty, "
            + "(SELECT COUNT(*) FROM prod_task_order WHERE actual_end_time >= CURRENT_DATE) AS taskOrderTodayFinishQty, "

            + "(SELECT COUNT(DISTINCT DATE(actual_end_time)) FROM prod_order WHERE order_status=4) AS orderFinishDays, "
            + "(SELECT COUNT(DISTINCT DATE(actual_end_time)) FROM prod_work_order WHERE order_status=4) AS workOrderFinishDays, "
            + "(SELECT COUNT(DISTINCT DATE(actual_end_time)) FROM prod_task_order WHERE status=4) AS taskOrderFinishDays ")
    OrderProgressVO countAllProgressData();

    /**
     * 获取订单统计（每个表只扫描1次，总共3次扫描）
     * @return
     */
    @Select(value = "SELECT " +
            "  po.total AS totalOrderCount, " +
            "  po.finish AS finishOrderCount, " +
            "  po.unfinish AS unFinishOrderCount, " +
            "  po.init AS initOrderCount, " +
            "  po.todayFinish AS todayFinishOrderCount, " +
            "     " +
            "  pwo.total AS totalWorkOrderCount, " +
            "  pwo.finish AS finishWorkOrderCount, " +
            "  pwo.unfinish AS unFinishWorkOrderCount, " +
            "  pwo.init AS initWorkOrderCount, " +
            "  pwo.todayFinish AS todayFinishWorkOrderCount, " +
            "   " +
            "  pto.total AS totalTaskOrderCount, " +
            "  pto.finish AS finishTaskOrderCount, " +
            "  pto.unfinish AS unFinishTaskOrderCount, " +
            "  pto.init AS initTaskOrderCount, " +
            "  pto.todayFinish AS todayFinishTaskOrderCount " +
            " " +
            "FROM ( " +
            "  SELECT " +
            "    COUNT(*) AS total, " +
            "    SUM(CASE WHEN order_status = 4 THEN 1 ELSE 0 END) AS finish, " +
            "    SUM(CASE WHEN order_status IN (1,2,3) THEN 1 ELSE 0 END) AS unfinish, " +
            "    SUM(CASE WHEN order_status = 0 THEN 1 ELSE 0 END) AS init, " +
            "    SUM(CASE WHEN order_status = 4 AND actual_end_time >= CURRENT_DATE AND actual_end_time < CURRENT_DATE + INTERVAL '1 day' THEN 1 ELSE 0 END) AS todayFinish " +
            "  FROM prod_order " +
            "  WHERE del_flag = 0 " +
            ") po " +
            "CROSS JOIN ( " +
            "  SELECT " +
            "    COUNT(*) AS total, " +
            "    SUM(CASE WHEN order_status = 4 THEN 1 ELSE 0 END) AS finish, " +
            "    SUM(CASE WHEN order_status IN (1,2,3) THEN 1 ELSE 0 END) AS unfinish, " +
            "    SUM(CASE WHEN order_status = 0 THEN 1 ELSE 0 END) AS init, " +
            "    SUM(CASE WHEN order_status = 4 AND actual_end_time >= CURRENT_DATE AND actual_end_time < CURRENT_DATE + INTERVAL '1 day' THEN 1 ELSE 0 END) AS todayFinish " +
            "  FROM prod_work_order " +
            "  WHERE del_flag = 0 " +
            ") pwo " +
            "CROSS JOIN ( " +
            "  SELECT " +
            "    COUNT(*) AS total, " +
            "    SUM(CASE WHEN status = 4 THEN 1 ELSE 0 END) AS finish, " +
            "    SUM(CASE WHEN status IN (1,2,3) THEN 1 ELSE 0 END) AS unfinish, " +
            "    SUM(CASE WHEN status = 0 THEN 1 ELSE 0 END) AS init, " +
            "    SUM(CASE WHEN status = 4 AND actual_end_time >= CURRENT_DATE AND actual_end_time < CURRENT_DATE + INTERVAL '1 day' THEN 1 ELSE 0 END) AS todayFinish " +
            "  FROM prod_task_order " +
            "  WHERE del_flag = 0 " +
            ") pto ")
    OrderStatisticsVO getOrderStatistics();

}
