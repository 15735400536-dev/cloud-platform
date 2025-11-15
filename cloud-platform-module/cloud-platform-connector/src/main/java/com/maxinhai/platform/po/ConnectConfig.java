package com.maxinhai.platform.po;


import com.baomidou.mybatisplus.annotation.TableName;
import com.maxinhai.platform.enums.ConnectType;
import lombok.Data;

/**
 * @ClassName：ConnectionConfig
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 10:33
 * @Description: 连接配置
 */
@Data
@TableName("conn_connect")
public class ConnectConfig extends RecordEntity {

    private String key;
    private ConnectType type;
    private String ip;
    private Integer port;
    private String username;
    private String password;

}
