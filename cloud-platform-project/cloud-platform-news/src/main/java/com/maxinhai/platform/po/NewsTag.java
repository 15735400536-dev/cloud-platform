package com.maxinhai.platform.po;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 新闻标签表（news_tag）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "news_tag")
public class NewsTag extends RecordEntity {

    /**
     * 标签名，唯一
     */
    @Column(name = "tag", nullable = false, length = 50)
    private String tag;

}
