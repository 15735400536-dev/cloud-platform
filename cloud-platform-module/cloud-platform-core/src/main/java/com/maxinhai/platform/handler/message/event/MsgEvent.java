package com.maxinhai.platform.handler.message.event;

import lombok.Data;

/**
 * @ClassName：MsgEvent
 * @Author: XinHai.Ma
 * @Date: 2025/8/27 17:24
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
public class MsgEvent<T> {

    private String id;
    private String key;
    private T data;

}
