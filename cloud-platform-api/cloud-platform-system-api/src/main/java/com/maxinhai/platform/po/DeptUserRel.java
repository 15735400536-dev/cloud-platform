package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName：DeptUserRel
 * @Author: XinHai.Ma
 * @Date: 2025/9/28 14:37
 * @Description: 部门关联用户表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_dept_user_rel")
public class DeptUserRel extends RecordEntity {

    private String deptId;
    private String userId;

}
