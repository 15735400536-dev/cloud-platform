package com.maxinhai.platform.po;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 新闻表（news_info）（核心表）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "news_info")
public class NewsInfo extends RecordEntity {

    /**
     * 新闻标题（唯一，防重复）
     */
    private String title;
    /**
     * 新闻摘要（列表页展示）
     */
    private String summary;
    /**
     * 新闻正文（富文本 HTML）
     */
    private String content;
    /**
     * 封面图 URL（列表页展示）
     */
    private String coverImg;
    /**
     * 所属分类 ID（关联 news_category）
     */
    @Column(name = "category_id", nullable = false)
    private Long categoryId;
    /**
     * 新闻来源（如 “新华社”“原创”）
     */
    private String source;
    /**
     * 作者 ID（关联 sys_user）
     */
    @Column(name = "author_id", nullable = false, length = 32)
    private String authorId;
    /**
     * 状态（0 - 草稿，1 - 待审核，2 - 审核通过，3 - 已发布，4 - 已驳回）
     */
    @Column(name = "status", nullable = false)
    private Integer status;
    /**
     * 阅读量（去重，Redis 计数后定时同步）
     */
    private Long viewCount = 0L;
    /**
     * 点赞数
     */
    private Long likeCount = 0L;
    /**
     * 评论数
     */
    private Long commentCount = 0L;
    /**
     * 发布时间（定时发布时使用）
     */
    private LocalDateTime publishTime;

}
