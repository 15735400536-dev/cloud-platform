package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("alarm_image")
public class AlarmImage extends RecordEntity {

    /**
     * 告警记录ID
     */
    private String alarmId;
    /**
     * 图片地址
     */
    private String imageUrl;

}
