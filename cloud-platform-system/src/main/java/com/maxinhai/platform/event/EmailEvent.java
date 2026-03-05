package com.maxinhai.platform.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EmailEvent extends ApplicationEvent {

    private final String email;
    private final String message;

    public EmailEvent(Object source, String email, String message) {
        super(source);
        this.email = email;
        this.message = message;
    }

}
