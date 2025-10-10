package com.maxinhai.platform.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 小说 - 标签关联表
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "novel_tag_rel")
public class NovelTagRel extends RecordEntity{

    /**
     * 关联小说 ID
     */
    @Column(name = "novel_id", length = 32)
    private Long novelId;
    /**
     * 关联标签 ID
     */
    @Column(name = "tag_id", length = 32)
    private Long tagId;

}
