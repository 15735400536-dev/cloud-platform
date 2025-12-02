package com.maxinhai.platform.service.alarm;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.alarm.AlarmQueryDTO;
import com.maxinhai.platform.dto.alarm.RealTimeAlarmDTO;
import com.maxinhai.platform.po.alarm.Alarm;
import com.maxinhai.platform.vo.alarm.AlarmVO;
import com.maxinhai.platform.vo.alarm.CountAlarmInfoVO;

import java.io.IOException;

public interface AlarmService extends IService<Alarm> {

    Page<AlarmVO> searchByPage(AlarmQueryDTO param);

    AlarmVO getInfo(String id);

    void remove(String[] ids);

    void realTimeAlarm(RealTimeAlarmDTO param) throws IOException;

    CountAlarmInfoVO countAlarmInfo();

    /**
     * 改造后的分页查询：合并Redis实时告警 + 数据库历史告警
     * @param param 分页查询参数
     * @return 分页查询结果
     */
    Page<AlarmVO> mergeSearchByPage(AlarmQueryDTO param);

}
