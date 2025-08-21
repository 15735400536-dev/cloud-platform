package com.maxinhai.platform.dto;

import com.baomidou.mybatisplus.annotation.DbType;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @ClassName：CustomDateSourceAddDTO
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 18:08
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
@ApiModel("DTO")
public class CustomDataSourceAddDTO {

    /**
     * 数据源标识
     */
    private String key;
    /**
     * 数据库类型（MySQL、PgSQL、Oracle、SqlServer）
     */
    private DbType type;
    /**
     * 数据库驱动：com.mysql.cj.jdbc.Driver
     */
    private String driverClassName;
    /**
     * 连接地址：jdbc:mysql://localhost:3306/testdb?useUnicode=true
     * &characterEncoding=utf8
     * &useSSL=false
     * &serverTimezone=Asia/Shanghai
     */
    private String url;
    /**
     * 用户名：root
     */
    private String username;
    /**
     * 密码：root
     */
    private String password;
    /**
     * 数据库：123456
     */
    private String database;

}
