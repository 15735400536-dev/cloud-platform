package com.maxinhai.platform.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class QQEvent extends ApplicationEvent {

    private final String qq;
    private final String message;

    public QQEvent(Object source, String qq, String message) {
        super(source);
        this.qq = qq;
        this.message = message;
    }

}
