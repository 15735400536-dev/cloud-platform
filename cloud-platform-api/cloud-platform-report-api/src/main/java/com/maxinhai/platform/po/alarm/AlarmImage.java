package com.maxinhai.platform.po.alarm;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maxinhai.platform.po.RecordEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_alarm_image")
public class AlarmImage extends RecordEntity {

    private String alarmId;
    private String imageName;
    private String imageUrl;

}
