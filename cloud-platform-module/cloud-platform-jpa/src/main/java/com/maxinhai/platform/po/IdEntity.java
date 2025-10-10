package com.maxinhai.platform.po;

import lombok.Data;

import javax.persistence.*;

/**
 * @ClassName：IdEntity
 * @Author: XinHai.Ma
 * @Date: 2025/9/1 16:08
 * @Description: ID基类
 */
@Data
@MappedSuperclass
public abstract class IdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 11)
    private Long id;

}
