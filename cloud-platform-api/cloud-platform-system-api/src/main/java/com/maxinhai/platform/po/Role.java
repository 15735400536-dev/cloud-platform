package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_role")
public class Role extends RecordEntity {

    private String roleKey;
    private String roleName;
    private String roleDesc;

}
