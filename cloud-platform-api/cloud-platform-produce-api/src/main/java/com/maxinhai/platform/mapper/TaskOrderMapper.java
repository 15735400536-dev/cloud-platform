package com.maxinhai.platform.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import com.maxinhai.platform.po.TaskOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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

}
