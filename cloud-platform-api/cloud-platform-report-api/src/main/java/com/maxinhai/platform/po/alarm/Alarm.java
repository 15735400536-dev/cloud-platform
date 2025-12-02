package com.maxinhai.platform.po.alarm;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maxinhai.platform.enums.AlarmStatus;
import com.maxinhai.platform.po.RecordEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_alarm")
public class Alarm extends RecordEntity {

    private String algorithmId;
    private String alarmMsg;
    private Date startTime;
    private Date endTime;
    private AlarmStatus status;

}
