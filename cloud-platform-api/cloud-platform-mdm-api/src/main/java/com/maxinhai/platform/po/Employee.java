package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("mdm_employee")
public class Employee extends RecordEntity {

    /**
     * 员工工号（唯一）
     */
    private String empCode;
    /**
     * 员工姓名
     */
    private String empName;
    /**
     * 性别：男/女
     */
    private String sex;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 身份证号
     */
    private String idCard;
    /**
     * 职位
     */
    private String position;
    /**
     * 家庭住址
     */
    private String address;
    /**
     * 工种：普工/技工/焊工/装配等
     */
    private String workType;
    /**
     * 出生日期
     */
    private Date birthday;
    /**
     * 入职日期
     */
    private Date entryDate;
    /**
     * 离职日期
     */
    private Date leaveDate;
    /**
     * 状态：0在职 1离职 2试用期 3停薪留职
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;

}
