package com.maxinhai.platform.handler.message.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName：MsgEvent
 * @Author: XinHai.Ma
 * @Date: 2025/8/27 17:24
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MsgEvent<T> {

    /**
     * 消息ID（唯一性）
     */
    private String id;
    /**
     * 消息处理器标识（唯一性）
     */
    private String key;
    /**
     * 传输数据
     */
    private T data;

}
