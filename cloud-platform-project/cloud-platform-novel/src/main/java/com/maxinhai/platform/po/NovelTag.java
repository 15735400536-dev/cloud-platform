package com.maxinhai.platform.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 小说标签表
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "novel_tag")
public class NovelTag extends RecordEntity {

    /**
     * 标签名称（如 “系统流”“穿越”，不可重复）
     */
    @Column(name = "name", length = 32)
    private String name;
    /**
     * 排序权重（值越大，标签显示越靠前）
     */
    @Column(name = "sort", length = 32)
    private Integer sort;

}
