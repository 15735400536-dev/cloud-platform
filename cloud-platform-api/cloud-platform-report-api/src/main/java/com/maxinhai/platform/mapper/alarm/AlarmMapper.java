package com.maxinhai.platform.mapper.alarm;

import com.github.yulichang.base.MPJBaseMapper;
import com.maxinhai.platform.bo.AlgorithmAlarmBO;
import com.maxinhai.platform.po.alarm.Alarm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AlarmMapper extends MPJBaseMapper<Alarm> {

    /**
     * 查询算法告警次数
     *
     * @return 算法告警次数
     */
    @Select(value = "select alg.key as algorithm_key, count(*) as alarm_total " +
            "from t_alarm alarm " +
            "inner join t_algorithm alg on alarm.algorithm_id = alg.id " +
            "where alarm.del_flag = 0 " +
            "and alarm.del_flag = 0 " +
            "group by alg.key")
    List<AlgorithmAlarmBO> queryAlgorithmAlarmCount();

    /**
     * 查询告警总数次数
     *
     * @return 告警总数次数
     */
    @Select(value = "select count(*) from t_alarm where del_flag = 0")
    long queryAlarmTotalCount();

}
