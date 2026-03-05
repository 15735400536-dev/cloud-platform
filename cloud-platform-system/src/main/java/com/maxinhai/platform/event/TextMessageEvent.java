package com.maxinhai.platform.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TextMessageEvent extends ApplicationEvent {

    private final String phone;
    private final String message;

    public TextMessageEvent(Object source, String phone, String message) {
        super(source);
        this.phone = phone;
        this.message = message;
    }

}
