package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("zlm_media_server")
public class MediaServer extends RecordEntity {

    private String ip;
    private Integer port;
    private String secret;

}
