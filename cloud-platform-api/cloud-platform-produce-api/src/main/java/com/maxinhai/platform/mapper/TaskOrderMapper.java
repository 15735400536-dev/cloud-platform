package com.maxinhai.platform.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import com.maxinhai.platform.bo.DailyProcessFinishTaskOrderQtyBO;
import com.maxinhai.platform.bo.DailyTaskOrderBO;
import com.maxinhai.platform.bo.TaskOrderProcessSortBO;
import com.maxinhai.platform.po.TaskOrder;
import com.maxinhai.platform.vo.TaskOrderVO;
import com.maxinhai.platform.vo.worktime.CountTaskFinishQtyVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface TaskOrderMapper extends MPJBaseMapper<TaskOrder> {

    @Select(value = "select o.id, o.order_code, o.actual_begin_time as order_begin_time, o.actual_end_time as order_end_time, " +
            "wo.work_order_code, wo.actual_begin_time as work_begin_time, wo.actual_end_time as work_end_time, " +
            "task.task_order_code, task.actual_begin_time as task_begin_time, task.actual_end_time as task_end_time " +
            "from prod_order o " +
            "inner join prod_work_order wo on o.id = wo.order_id " +
            "inner join prod_task_order task on task.work_order_id = wo.id " +
            "where o.del_flag = 0 " +
            "and wo.del_flag = 0 " +
            "and task.del_flag = 0 " +
            "and task.status = 4 " +
            "and wo.order_status = 4 " +
            "and o.order_status = 4")
    List<Object> selectTaskOrderList(@Param("taskOrder") String taskOrderCode);

    @Select(value = "select task.work_order_id, task.id, task.sort, task.status " +
            "from prod_task_order task " +
            "where task.del_flag = 0 " +
            "and task.work_order_id in (" +
            "  select wo.id " +
            "  from prod_work_order wo " +
            "  where wo.del_flag = 0 " +
            "  and wo.order_status in (0,1,2,3) " +
            "  order by wo.order_id asc " +
            "  limit 5000" +
            ") " +
            "and task.status in (0,1,2,3) " +
            "order by task.sort asc")
    List<TaskOrder> queryCanStartTaskList();

    @Select(value = "select task.work_order_id, task.id, task.sort, task.status " +
            "from prod_task_order task " +
            "where task.del_flag = 0 " +
            "and task.work_order_id in (" +
            "  select wo.id " +
            "  from prod_work_order wo " +
            "  where wo.del_flag = 0 " +
            "  and wo.order_status in (0,1,2,3) " +
            "  order by wo.order_id asc " +
            "  limit #{count} " +
            ") " +
            "and task.status in (0,1,2,3) " +
            "order by task.sort asc")
    List<TaskOrder> queryCanStartTaskListByCount(Integer count);

    @Select(value = "select task.work_order_id, task.id, task.sort, task.status " +
            "from prod_task_order task " +
            "where task.del_flag = 0 " +
            "and task.work_order_id in (" +
            "  select wo.id " +
            "  from prod_work_order wo " +
            "  where wo.del_flag = 0 " +
            "  and wo.order_status in (0,1,2,3) " +
            "  and wo.plan_begin_time between #{beginTime} and #{endTime} " +
            "  order by wo.order_id asc " +
            "  limit 1000" +
            ") " +
            "and task.status in (0,1,2,3) " +
            "order by task.sort asc")
    List<TaskOrder> queryCanStartedTaskList(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    /**
     * 查询每天每道工序派工单完成数量
     *
     * @return 每天每道工序派工单完成数量
     */
    @Select(value = "select date(task.actual_end_time) as daily, task.operation_id, op.code as operation_code, op.name as operation_name, count(*) as qty " +
            "from prod_task_order task " +
            "inner join mdm_operation op on task.operation_id = op.id " +
            "where task.del_flag = 0 and task.status = 4 " +
            "group by date(task.actual_end_time), task.operation_id, op.code, op.name " +
            "order by date(task.actual_end_time) ")
    List<DailyProcessFinishTaskOrderQtyBO> queryDailyProcessFinishTaskOrderQty();

    /**
     * 根据开始时间、结束时间统计每天派工单完工数量
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 每天派工单完工数量
     */
    @Select(value = "select date(actual_end_time) as daily, count(*) as finishQty " +
            "from prod_task_order " +
            "where del_flag = 0 " +
            "and actual_end_time between #{beginTime} and #{endTime} " +
            "group by date(actual_end_time) " +
            "order by date(actual_end_time) asc")
    List<DailyTaskOrderBO> queryDailyTaskOrder(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    @Select(value = "select date(task.actual_end_time) as daily, op.code as opCode, count(*) as finishQty " +
            "from prod_task_order task " +
            "inner join mdm_operation op on task.operation_id = op.id " +
            "where task.del_flag = 0 " +
            "and op.del_flag = 0 " +
            "and task.actual_end_time between #{beginTime} and #{endTime} " +
            "group by date(task.actual_end_time), op.code " +
            "order by date(task.actual_end_time) asc")
    List<DailyTaskOrderBO> queryDailyOpTaskOrder(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    @Select(value = "select wo.id as workOrderId, wo.work_order_code as workOrderCode, count(*) as finishQty " +
            "from prod_order so " +
            "inner join prod_work_order wo on wo.order_id = so.id " +
            "inner join prod_task_order task on task.order_id = so.id and task.work_order_id = wo.id and task.status = 4 " +
            "where task.del_flag = 0 " +
            "and wo.del_flag = 0 " +
            "and so.del_flag = 0 " +
            "group by wo.id, wo.work_order_code")
    List<CountTaskFinishQtyVO> countTaskFinishQtyByWorkOrderId();

    @Select(value = "select so.id as orderId, so.order_code as orderCode, count(*) as finishQty " +
            "from prod_order so " +
            "inner join prod_work_order wo on wo.order_id = so.id " +
            "inner join prod_task_order task on task.order_id = so.id and task.work_order_id = wo.id and task.status = 4 " +
            "where task.del_flag = 0 " +
            "and wo.del_flag = 0 " +
            "and so.del_flag = 0 " +
            "group by so.id, so.order_code")
    List<CountTaskFinishQtyVO> countTaskFinishQtyByOrderId();

    /**
     * 根据ID集合批量查询详情（多表关联，超快）
     */
    @Select({
            "<script>",
            "SELECT ",
            "    t.*, ",
            "    t1.order_code, ",
            "    t2.work_order_code, ",
            "    t3.id as product_id, ",
            "    t3.code as product_code, ",
            "    t3.name as product_name, ",
            "    t4.id as bom_id, ",
            "    t4.code as bom_code, ",
            "    t4.name as bom_name, ",
            "    t5.id as routing_id, ",
            "    t5.code as routing_code, ",
            "    t5.name as routing_name, ",
            "    t6.id as operation_id, ",
            "    t6.code as operation_code, ",
            "    t6.name as operation_name ",
            "FROM prod_task_order t ",
            "INNER JOIN prod_order t1 ON t1.id = t.order_id ",
            "INNER JOIN prod_work_order t2 ON t2.id = t.work_order_id ",
            "INNER JOIN mdm_product t3 ON t3.id = t.product_id ",
            "INNER JOIN mdm_bom t4 ON t4.id = t.bom_id ",
            "INNER JOIN mdm_routing t5 ON t5.id = t.routing_id ",
            "INNER JOIN mdm_operation t6 ON t6.id = t.operation_id ",
            "WHERE t.del_flag = 0 ",
            "AND t.id IN ",
            "    <foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "        #{id}",
            "    </foreach>",
            "</script>"
    })
    List<TaskOrderVO> findTaskDetailByIds(@Param("ids") List<String> ids);

    /**
     * 查找派工单中的工序顺序号集合
     * @return 工序顺序号集合
     */
    @Select(value = "SELECT " +
            "    sort, " +
            "    COUNT(*) AS repeat_count " +
            "FROM " +
            "    prod_task_order " +
            "WHERE status IN (0,1,2,3) " +
            "GROUP BY " +
            "    sort " +
            "HAVING " +
            "    COUNT(*) > 0 " +
            "ORDER BY " +
            "    sort ASC")
    List<TaskOrderProcessSortBO> selectProcessSort();

    /**
     * 根据工序顺序号查找可操作的派工单集合
     * @param sort 工序顺序号
     * @return 可操作的派工单集合
     */
    @Select(value = "SELECT " +
            "  task.work_order_id, " +
            "  task.id, " +
            "  task.sort, " +
            "  task.status " +
            "FROM prod_task_order task " +
            "WHERE " +
            "  task.del_flag = 0 " +
            "  AND task.status in (0,1,2,3) " +
            "  AND task.sort = #{sort} " +
            "  AND ( " +
            "    #{sort} = 1 " +
            "    OR EXISTS ( " +
            "      SELECT 1 " +
            "      FROM prod_task_order t2 " +
            "      WHERE " +
            "        t2.del_flag = 0 " +
            "        AND t2.status = 4 " +
            "        AND t2.sort = #{sort} - 1 " +
            "        AND t2.work_order_id = task.work_order_id " +
            "    ) " +
            "  ) " +
            "ORDER BY task.create_time DESC " +
            "LIMIT 100")
    List<TaskOrder> selectOperableTaskOrderList(Integer sort);

    /**
     * 根据派工单ID查询同一个工单下的所有派工单
     * @param taskOrderId 派工单ID
     * @return
     */
    @Select(value = "SELECT id, work_order_id, order_id, status, sort " +
            "FROM prod_task_order " +
            "WHERE del_flag = 0 " +
            "AND work_order_id = ( " +
            "    SELECT work_order_id FROM prod_task_order WHERE id = #{taskOrderId} LIMIT 1 " +
            ") " +
            "ORDER BY sort ASC")
    List<TaskOrder> selectTaskOrderListByTaskOrderId(String taskOrderId);

    /**
     * 工单能否开工-根据工单ID查询工单下派工单是否全部初始化状态
     * @param workOrderId 工单ID
     * @return 初始化记录数量
     */
    @Select(value = "select count(1) from prod_task_order " +
            "where del_flag = 0 " +
            "and status = 0 " +
            "and work_order_id = #{workOrderId}")
    int countInitTask(@Param("workOrderId") String workOrderId);

    /**
     * 工单是否暂停-根据工单ID查询工单下是否存在暂停记录
     * @param workOrderId 工单ID
     * @return 暂停记录数量
     */
    @Select(value = "select count(1) from prod_task_order " +
            "where del_flag = 0 " +
            "and status = 2 " +
            "and work_order_id = #{workOrderId} " +
            "limit 1")
    int countPauseTask(@Param("workOrderId") String workOrderId);

    /**
     * 工单能否复工-根据工单ID查询工单下派工单是否存在初始、暂停记录
     * @param workOrderId 工单ID
     * @return 初始、暂停记录数量
     */
    @Select(value = "select count(1) from prod_task_order " +
            "where del_flag = 0 " +
            "and status in (0,2) " +
            "and work_order_id = #{workOrderId}")
    int countInitAndPauseTask(@Param("workOrderId") String workOrderId);

    /**
     * 工单能否报工-根据工单ID查询工单下派工单是否全部报工
     * @param workOrderId 工单ID
     * @return 报工记录数量
     */
    @Select(value = "select count(1) from prod_task_order " +
            "where del_flag = 0 " +
            "and status = 4 " +
            "and work_order_id = #{workOrderId}")
    int countReportTask(@Param("workOrderId") String workOrderId);

    /**
     * 根据工单ID查询工单下派工单数量
     * @param workOrderId 工单ID
     * @return 派工单数量
     */
    @Select(value = "select count(1) from prod_task_order " +
            "where work_order_id = #{workOrderId} " +
            "and status = 0")
    int countTask(@Param("workOrderId") String workOrderId);

}
