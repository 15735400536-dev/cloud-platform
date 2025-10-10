package com.maxinhai.platform.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 小说表
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "novel")
public class Novel extends RecordEntity{

    /**
     * 小说标题
     */
    @Column(name = "title", length = 50)
    public String title;
    /**
     * 作者ID
     */
    @Column(name = "author_id", length = 32)
    private Long authorId;
    /**
     * 小说分类ID
     */
    @Column(name = "category_id", length = 32)
    private Long categoryId;
    /**
     * 封面地址
     */
    @Column(name = "cover_url", length = 100)
    private String coverUrl;
    /**
     * 状态
     */
    @Column(name = "status", length = 32)
    private Integer status;

}
