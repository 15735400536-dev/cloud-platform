package com.maxinhai.platform.listener;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 派工单报工事件
 */
@Getter
public class TaskOrderReportEvent extends ApplicationEvent {

    private String taskOrderId;

    public TaskOrderReportEvent(Object source, String taskOrderId) {
        super(source);
        this.taskOrderId = taskOrderId;
    }
}
