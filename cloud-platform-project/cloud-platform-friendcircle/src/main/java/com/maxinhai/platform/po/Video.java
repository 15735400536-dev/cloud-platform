package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_video")
public class Video extends RecordEntity {

    /**
     * 标题
     */
    private String title;
    /**
     * 封面地址
     */
    private String coverUrl;
    /**
     * 预览视频地址
     */
    private String previewUrl;
    /**
     * 视频地址
     */
    private String videoUrl;
    /**
     * 时长
     */
    private String duration;
    /**
     * 详情页
     */
    private String detailPage;

}
