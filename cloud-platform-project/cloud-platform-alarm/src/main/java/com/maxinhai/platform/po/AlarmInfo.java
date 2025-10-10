package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("alarm_info")
public class AlarmInfo extends RecordEntity {

    /**
     * 触发的规则ID
     */
    private String ruleId;
    /**
     * 告警级别（1-提示，2-警告，3-严重，4-紧急）
     */
    private Integer alarmLevel;
    /**
     * 告警内容描述
     */
    private String alarmContent;
    /**
     * 告警来源（如：设备ID、传感器编号）
     */
    private String alarmSource;
    /**
     * 告警状态（0-未处理，1-处理中，2-已处理，3-已忽略）
     */
    private Integer alarmStatus;
    /**
     * 告警发生时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date alarmTime;
    /**
     * 处理时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date processTime;
    /**
     * 处理人
     */
    private String processUser;
    /**
     * 处理备注
     */
    private String processNotes;

}
