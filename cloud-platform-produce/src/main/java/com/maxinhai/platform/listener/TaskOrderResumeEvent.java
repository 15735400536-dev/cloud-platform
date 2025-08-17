package com.maxinhai.platform.listener;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 派工单复工事件
 */
@Getter
public class TaskOrderResumeEvent extends ApplicationEvent {

    private String taskOrderId;

    public TaskOrderResumeEvent(Object source, String taskOrderId) {
        super(source);
        this.taskOrderId = taskOrderId;
    }
}
