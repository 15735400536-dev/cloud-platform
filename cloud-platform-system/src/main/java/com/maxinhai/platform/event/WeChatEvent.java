package com.maxinhai.platform.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class WeChatEvent extends ApplicationEvent {

    private final String weChat;
    private final String message;

    public WeChatEvent(Object source, String weChat, String message) {
        super(source);
        this.weChat = weChat;
        this.message = message;
    }

}
