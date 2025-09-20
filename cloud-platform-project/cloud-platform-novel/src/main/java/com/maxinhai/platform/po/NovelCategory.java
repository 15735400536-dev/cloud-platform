package com.maxinhai.platform.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 小说分类表
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "novel_category")
public class NovelCategory extends RecordEntity {

    /**
     * 分类名称（如 “玄幻”“都市”“科幻”，不可重复）
     */
    @Column(name = "name", length = 50)
    private String name;
    /**
     * 排序权重（值越大，分类显示越靠前）
     */
    @Column(name = "sort", length = 32)
    private Integer sort;
    /**
     * 状态：0 - 隐藏，1 - 显示（隐藏后不在前端展示）
     */
    @Column(name = "status", length = 32)
    private Integer status;
    /**
     * 分类简介（如 “玄幻：包含修仙、魔法、异世界等元素”）
     */
    @Column(name = "description", length = 100)
    private String description;

}
