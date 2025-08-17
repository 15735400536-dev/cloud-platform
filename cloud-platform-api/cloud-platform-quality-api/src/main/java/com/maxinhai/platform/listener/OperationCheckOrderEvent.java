package com.maxinhai.platform.listener;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 生成工序检测单
 */
@Getter
public class OperationCheckOrderEvent extends ApplicationEvent {

    /**
     * 派工单ID
     */
    private String taskOrderId;

    public OperationCheckOrderEvent(Object source, String taskOrderId) {
        super(source);
        this.taskOrderId = taskOrderId;
    }

}
