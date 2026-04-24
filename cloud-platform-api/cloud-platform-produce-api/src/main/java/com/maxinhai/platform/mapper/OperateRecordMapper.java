package com.maxinhai.platform.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import com.maxinhai.platform.bo.TaskOrderWorkTimeBO;
import com.maxinhai.platform.po.OperateRecord;
import com.maxinhai.platform.vo.worktime.UserWorkTimeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface OperateRecordMapper extends MPJBaseMapper<OperateRecord> {

    @Select(value = "select record.task_order_id, record.operate_type, record.operate_time " +
            "from prod_operate_record record " +
            "inner join prod_task_order task on task.id = record.task_order_id " +
            "inner join prod_work_order wo on wo.id = task.work_order_id " +
            "where record.del_flag = 0 " +
            "and task.del_flag = 0 " +
            "and wo.del_flag = 0 " +
            "and wo.id = #{workOrderId} " +
            "order by record.operate_time asc")
    List<OperateRecord> getWorkOrderWorkTime(@Param("workOrderId") String workOrderId);

    @Select(value = "select record.task_order_id, record.operate_type, record.operate_time " +
            "from prod_operate_record record " +
            "inner join prod_task_order task on task.id = record.task_order_id " +
            "inner join prod_work_order wo on wo.id = task.work_order_id " +
            "inner join prod_order so on so.id = wo.order_id " +
            "where record.del_flag = 0 " +
            "and task.del_flag = 0 " +
            "and wo.del_flag = 0 " +
            "and so.del_flag = 0 " +
            "and so.id = #{orderId} " +
            "order by record.operate_time asc")
    List<OperateRecord> getOrderWorkTime(@Param("orderId") String orderId);

    /**
     * 按开工/暂停/复工/报工 精准统计每个派工单工时
     */
    @Select({
            "<script>",
            "WITH record_order AS (",
            "    SELECT",
            "        task_order_id,",
            "        operate_type,",
            "        operate_time,",
            "        ROW_NUMBER() OVER (PARTITION BY task_order_id ORDER BY operate_time) AS rn",
            "    FROM prod_operate_record",
            "    WHERE del_flag = 0",
            "),",
            "time_span AS (",
            "    SELECT",
            "        curr.task_order_id,",
            "        curr.operate_time AS start_time,",
            "        next.operate_time AS end_time",
            "    FROM record_order curr",
            "    LEFT JOIN record_order next",
            "        ON curr.task_order_id = next.task_order_id",
            "        AND curr.rn + 1 = next.rn",
            "    WHERE curr.operate_type IN (1, 3)",
            "      AND next.operate_type IN (2, 4)",
            ")",
            "SELECT",
            "    task_order_id,",
            "    ROUND(SUM(EXTRACT(EPOCH FROM (end_time - start_time))) / 3600, 2) AS total_work_hours",
            "FROM time_span",
            "GROUP BY task_order_id",
            "ORDER BY total_work_hours DESC",
            "</script>"
    })
    List<Map<String, Object>> selectTaskOrderWorkTime();

    @Select({
            "<script>",
            "WITH record_order AS (",
            "    SELECT",
            "        task_order_id,",
            "        operate_type,",
            "        operate_time,",
            "        ROW_NUMBER() OVER (PARTITION BY task_order_id ORDER BY operate_time) AS rn",
            "    FROM prod_operate_record",
            "    WHERE del_flag = 0",
            "    <if test='taskOrderIds != null and taskOrderIds.size() > 0'>",
            "      AND task_order_id IN",
            "      <foreach collection='taskOrderIds' item='id' open='(' separator=',' close=')'>",
            "          #{id}",
            "      </foreach>",
            "    </if>",
            "),",
            "time_span AS (",
            "    SELECT",
            "        curr.task_order_id,",
            "        curr.operate_time AS start_time,",
            "        next.operate_time AS end_time",
            "    FROM record_order curr",
            "    LEFT JOIN record_order next",
            "        ON curr.task_order_id = next.task_order_id",
            "        AND curr.rn + 1 = next.rn",
            "    WHERE curr.operate_type IN (1, 3)",
            "      AND next.operate_type IN (2, 4)",
            ")",
            "SELECT",
            "    task_order_id AS \"taskOrderId\",",
            "    ROUND(SUM(EXTRACT(EPOCH FROM (end_time - start_time))) / 3600, 2) AS \"totalWorkHours\"",
            "FROM time_span",
            "GROUP BY task_order_id",
            "ORDER BY totalWorkHours DESC",
            "</script>"
    })
    List<TaskOrderWorkTimeBO> selectTaskOrderWorkTimeByTaskOrderIds(@Param("taskOrderIds") List<String> taskOrderIds);

    @Select({
            "<script>",
            "WITH record_order AS (",
            "    SELECT",
            "        task_order_id,",
            "        operate_type,",
            "        operate_time,",
            "        ROW_NUMBER() OVER (PARTITION BY task_order_id ORDER BY operate_time) AS rn",
            "    FROM prod_operate_record",
            "    WHERE del_flag = 0",
            "),",
            "work_time AS (",
            "    SELECT",
            "        curr.task_order_id,",
            "        ROUND(SUM(EXTRACT(EPOCH FROM (next.operate_time - curr.operate_time))) / 3600, 2) AS total_work_hours",
            "    FROM record_order curr",
            "    LEFT JOIN record_order next ON curr.task_order_id = next.task_order_id AND curr.rn + 1 = next.rn",
            "    WHERE curr.operate_type IN (1, 3) AND next.operate_type IN (2, 4)",
            "    GROUP BY curr.task_order_id",
            "),",
            "task_user AS (",
            "    SELECT",
            "        pto.id AS task_order_id,",
            "        pto.create_by AS user_id,",
            "        pto.status AS task_status",
            "    FROM prod_task_order pto",
            "    WHERE pto.del_flag = 0",
            ")",
            "SELECT",
            "    su.id AS userId,",
            "    su.account,",
            "    su.username,",
            "    COUNT(DISTINCT CASE WHEN tu.task_status = 4 THEN tu.task_order_id END) AS taskOrderFinishQty,",
            "    COALESCE(SUM(wt.total_work_hours), 0) AS totalWorkTime",
            "FROM sys_user su",
            "LEFT JOIN task_user tu ON su.id = tu.user_id",
            "LEFT JOIN work_time wt ON tu.task_order_id = wt.task_order_id",
            "WHERE su.del_flag = 0",
            "GROUP BY su.id, su.account, su.username",
            "ORDER BY totalWorkTime DESC",
            "</script>"
    })
    List<UserWorkTimeVO> selectUserTaskOrderWorkTime();

    /**
     * 根据用户ID集合查询用户工单工时统计数据
     * <p>包含：用户信息、完成工单数、总工时（按操作记录开始/结束时间自动计算）</p>
     * <p>SQL 优化说明：已针对 260w+ 操作记录做高性能优化，使用 LEAD 窗口函数避免自连接</p>
     * <p>必须索引（提升查询性能 10~50 倍）：</p>
     * <ul>
     *     <li>1. 操作记录表索引：CREATE INDEX idx_por_task_time_type ON prod_operate_record (del_flag, task_order_id, operate_time, operate_type);</li>
     *     <li>2. 工单表索引：CREATE INDEX idx_pto_user_del ON prod_task_order (del_flag, create_by, id, status);</li>
     * </ul>
     * @param userIds 用户ID集合
     * @return 用户工单工时统计列表
     */
    @Select({
            "<script>",
            "WITH task_user AS (",
            "    SELECT",
            "        pto.id AS task_order_id,",
            "        pto.create_by AS user_id,",
            "        pto.status AS task_status",
            "    FROM prod_task_order pto",
            "    WHERE pto.del_flag = 0 ",
            "    AND pto.create_by IN ",
            "    <foreach collection='userIds' item='id' open='(' separator=',' close=')'>",
            "        #{id}",
            "    </foreach>",
            "),",
            "record_lead AS (",
            "    SELECT",
            "        task_order_id,",
            "        operate_type,",
            "        operate_time,",
            "        LEAD(operate_time) OVER (PARTITION BY task_order_id ORDER BY operate_time) AS next_time,",
            "        LEAD(operate_type) OVER (PARTITION BY task_order_id ORDER BY operate_time) AS next_type",
            "    FROM prod_operate_record",
            "    WHERE del_flag = 0",
            "    AND task_order_id IN (SELECT task_order_id FROM task_user)",
            "),",
            "work_time AS (",
            "    SELECT",
            "        task_order_id,",
            "        ROUND(SUM(EXTRACT(EPOCH FROM (next_time - operate_time))) / 3600, 2) AS total_work_hours",
            "    FROM record_lead",
            "    WHERE operate_type IN (1,3)",
            "    AND next_type IN (2,4)",
            "    GROUP BY task_order_id",
            ")",
            "SELECT ",
            "    su.id AS \"userId\", ",
            "    su.account AS \"account\", ",
            "    su.username AS \"username\", ",
            "    COUNT(DISTINCT CASE WHEN tu.task_status = 4 THEN tu.task_order_id END) AS \"taskOrderFinishQty\", ",
            "    COALESCE(SUM(wt.total_work_hours), 0) AS \"totalWorkTime\" ",
            "FROM sys_user su ",
            "LEFT JOIN task_user tu ON su.id = tu.user_id ",
            "LEFT JOIN work_time wt ON tu.task_order_id = wt.task_order_id ",
            "WHERE su.del_flag = 0 ",
            "AND su.id IN ",
            "<foreach collection='userIds' item='id' open='(' separator=',' close=')'>",
            "    #{id}",
            "</foreach>",
            "GROUP BY su.id, su.account, su.username ",
            "ORDER BY \"totalWorkTime\" DESC ",
            "</script>"
    })
    List<UserWorkTimeVO> selectUserTaskOrderWorkTimeByUserIds(@Param("userIds") List<String> userIds);

}
