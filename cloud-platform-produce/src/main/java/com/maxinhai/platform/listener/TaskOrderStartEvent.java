package com.maxinhai.platform.listener;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 派工单开工事件
 */
@Getter
public class TaskOrderStartEvent extends ApplicationEvent {

    private String taskOrderId;

    public TaskOrderStartEvent(Object source, String taskOrderId) {
        super(source);
        this.taskOrderId = taskOrderId;
    }
}
