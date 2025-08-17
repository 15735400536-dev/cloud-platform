package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.util.Date;

@Data
public class RecordEntity extends IdEntity {

    @TableLogic(value = "0", delval = "1")
    @TableField(fill = FieldFill.INSERT)
    protected Integer delFlag;
    @TableField(fill = FieldFill.INSERT)
    protected String createBy;
    @TableField(fill = FieldFill.INSERT)
    protected Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected String updateBy;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    protected Date updateTime;

}
