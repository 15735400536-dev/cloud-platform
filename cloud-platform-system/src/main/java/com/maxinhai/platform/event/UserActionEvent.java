package com.maxinhai.platform.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserActionEvent extends ApplicationEvent {

    private final String userId;
    private final String action;

    public UserActionEvent(Object source, String userId, String action) {
        super(source);
        this.userId = userId;
        this.action = action;
    }

}
