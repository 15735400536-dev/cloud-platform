package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName：Supplier
 * @Author: XinHai.Ma
 * @Date: 2025/9/26 15:43
 * @Description: 供应商表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_supplier")
public class Supplier extends RecordEntity {

    /**
     * 供应商名称（企业名称，如 “XX 电子元件厂”）
     */
    private String name;
    /**
     * 供应商类型（如 “原材料”“设备”“服务”“软件”）
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
     * 供应商等级：1 - 核心（优先合作），2 - 优质，3 - 普通，4 - 备选（用于评估）
     */
    private Integer level;
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
