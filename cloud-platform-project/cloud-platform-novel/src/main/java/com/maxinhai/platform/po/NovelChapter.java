package com.maxinhai.platform.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 小说章节表
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "novel_chapter")
public class NovelChapter extends RecordEntity{

    /**
     * 小说ID
     */
    @Column(name = "novel_id", length = 32)
    private Long novelId;
    /**
     * 章节名称
     */
    @Column(name = "title", length = 100)
    private String title;
    /**
     * 章节顺序
     */
    @Column(name = "sort", length = 32)
    private Integer sort;
    /**
     * 是否VIP章节
     */
    @Column(name = "is_vip", length = 32)
    private Boolean isVip;
    /**
     * 章节字数
     */
    @Column(name = "word_count", length = 32)
    private Integer wordCount;
    /**
     * 状态
     */
    @Column(name = "status", length = 32)
    private Integer status;
    /**
     * 发布时间
     */
    @Column(name = "publish_time")
    private LocalDateTime publishTime;

}
