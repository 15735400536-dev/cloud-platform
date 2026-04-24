package com.maxinhai.platform.po.info;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maxinhai.platform.po.RecordEntity;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sys_info_publish_record")
public class InfoPublishRecord extends RecordEntity {

    /**
     * 信息ID
     */
    private String infoId;
    /**
     * 操作状态
     */
    private Integer status;
    /**
     * 操作者
     */
    private String operator;
    /**
     * 操作时间
     */
    private Date operateTime;

}
