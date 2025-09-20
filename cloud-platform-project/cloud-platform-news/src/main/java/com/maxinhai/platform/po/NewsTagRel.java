package com.maxinhai.platform.po;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 新闻 - 标签关联表（news_tag_rel）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "news_tag_rel")
public class NewsTagRel extends RecordEntity {

    @Column(name = "news_id", nullable = false)
    private Long newsId;
    @Column(name = "tag_id", nullable = false)
    private Long tagId;

}
