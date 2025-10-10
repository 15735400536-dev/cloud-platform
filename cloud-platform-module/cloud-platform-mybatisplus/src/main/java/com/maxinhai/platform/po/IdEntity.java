package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * Id实体类
 */
@Data
public abstract class IdEntity {

    @TableId(type = IdType.ASSIGN_ID)
    protected String id;

}
