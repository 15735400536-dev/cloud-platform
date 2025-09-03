package com.maxinhai.platform.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

/**
 * @ClassName：RecordEntity
 * @Author: XinHai.Ma
 * @Date: 2025/9/3 13:16
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class RecordEntity extends IdEntity {

    @Field("del_flag")
    private Integer delFlag;

    @CreatedBy
    @Field("create_by")
    private String createBy;

    @CreatedDate
    @Field("create_time")
    private LocalDateTime createTime;

    @LastModifiedBy
    @Field("update_by")
    private String updateBy;

    @LastModifiedDate
    @Field("update_time")
    private LocalDateTime updateTime;

}
