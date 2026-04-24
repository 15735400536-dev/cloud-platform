package com.maxinhai.platform.po.info;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maxinhai.platform.po.RecordEntity;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sys_info_publish")
public class InfoPublish extends RecordEntity {

    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 作者
     */
    private String author;
    /**
     * 图片地址
     */
    private String imgUrls;
    /**
     * 视频地址
     */
    private String videoUrl;
    /**
     * 发布状态
     */
    private Integer publishStatus;
    /**
     * 发布者
     */
    private String publisher;
    /**
     * 发布时间
     */
    private Date publishTime;

}
