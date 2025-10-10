package com.maxinhai.platform.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 作者信息表
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "author_info")
public class AuthorInfo extends RecordEntity {

    /**
     * 用户ID
     */
    @Column(name = "user_id", length = 32)
    private String userId;
    /**
     * 昵称
     */
    @Column(name = "nickname", length = 32)
    private String nickname;
    /**
     * 自我介绍
     */
    @Column(name = "info", length = 100)
    private String info;

}
