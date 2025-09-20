package com.maxinhai.platform.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 章节内容表
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "novel_chapter_content")
public class NovelChapterContent extends RecordEntity {

    /**
     * 章节ID
     */
    @Column(name = "chapter_id", length = 32)
    private Long chapterId;
    /**
     * 章节内容
     */
    @Column(name = "content", length = 100)
    private String content;

}
