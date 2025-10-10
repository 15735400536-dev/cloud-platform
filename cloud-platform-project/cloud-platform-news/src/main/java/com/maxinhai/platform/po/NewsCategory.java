package com.maxinhai.platform.po;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 新闻分类表（news_category）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "news_category")
public class NewsCategory extends RecordEntity {

    /**
     * 分类名称（如 “国内新闻”）
     */
    @Column(name = "category_name", nullable = false, length = 50)
    private String categoryName;
    /**
     * 父分类 ID（0 - 顶级分类）
     */
    @Column(name = "parent_id", nullable = false)
    private Long parentId;
    /**
     * 排序序号（越小越靠前）
     */
    @Column(name = "sort", nullable = false)
    private Integer sort;

}
