package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName：Customer
 * @Author: XinHai.Ma
 * @Date: 2025/9/26 15:43
 * @Description: 客户表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_customer")
public class Customer extends RecordEntity {

    /**
     * 客户名称（个人 / 企业名称，如 “张三”“XX 科技有限公司”）
     */
    private String name;
    /**
     * 客户类型：0 - 个人客户，1 - 企业客户
     */
    private Integer type;
    /**
     * 主要联系人姓名
     */
    private String contactPerson;
    /**
     * 联系电话（手机号 / 座机）
     */
    private String contactPhone;
    /**
     * 联系邮箱（可选）
     */
    private String contactEmail;
    /**
     * 省份（如 “广东省”，便于地区统计）
     */
    private String province;
    /**
     * 城市（如 “深圳市”）
     */
    private String city;
    /**
     * 区 / 县（如 “南山区”）
     */
    private String district;
    /**
     * 详细地址（门牌号等）
     */
    private String address;
    /**
     * 客户等级：1-VIP，2 - 重要，3 - 普通，4 - 潜在（用于分层服务）
     */
    private Integer customer_level;
    /**
     * 客户来源（如 “官网咨询”“转介绍”“展会”）
     */
    private String source;
    /**
     * 状态：0 - 禁用（停止合作），1 - 正常（合作中）
     */
    private Integer status;
    /**
     * 备注（如特殊需求、合作历史等）
     */
    private String remark;

}
