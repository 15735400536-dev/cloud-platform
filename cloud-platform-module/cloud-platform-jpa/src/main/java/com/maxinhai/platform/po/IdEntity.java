package com.maxinhai.platform.po;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * @ClassName：IdEntity
 * @Author: XinHai.Ma
 * @Date: 2025/9/1 16:08
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
@MappedSuperclass
public abstract class IdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
