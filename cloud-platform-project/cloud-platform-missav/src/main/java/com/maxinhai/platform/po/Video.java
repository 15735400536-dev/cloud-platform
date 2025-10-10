package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
//@Table(name = "t_video")  // 表名
@TableName("t_video")
public class Video extends RecordEntity {

    /**
     * 标题
     */
//    @Column(name = "title", type = MySqlTypeConstant.VARCHAR, length = 50, comment = "标题")
    private String title;
    /**
     * 封面地址
     */
//    @Column(name = "cover_url", type = MySqlTypeConstant.VARCHAR, length = 100, comment = "封面地址")
    private String coverUrl;
    /**
     * 预览视频地址
     */
//    @Column(name = "preview_url", type = MySqlTypeConstant.VARCHAR, length = 100, comment = "预览视频地址")
    private String previewUrl;
    /**
     * 视频地址
     */
//    @Column(name = "video_url", type = MySqlTypeConstant.VARCHAR, length = 100, comment = "视频地址")
    private String videoUrl;
    /**
     * 时长
     */
//    @Column(name = "duration", type = MySqlTypeConstant.VARCHAR, length = 32, comment = "时长")
    private String duration;
    /**
     * 详情页
     */
//    @Column(name = "detail_page", type = MySqlTypeConstant.VARCHAR, length = 100, comment = "详情页")
    private String detailPage;

}
