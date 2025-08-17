package com.maxinhai.platform.listener;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 生成检测单
 */
@Getter
public class CheckOrderEvent extends ApplicationEvent {

    /**
     * 工单ID
     */
    private String workOrderId;

    public CheckOrderEvent(Object source, String workOrderId) {
        super(source);
        this.workOrderId = workOrderId;
    }

}
