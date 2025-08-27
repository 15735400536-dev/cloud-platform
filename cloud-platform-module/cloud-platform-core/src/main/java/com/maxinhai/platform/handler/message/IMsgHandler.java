package com.maxinhai.platform.handler.message;

import com.maxinhai.platform.handler.message.event.MsgEvent;

public interface IMsgHandler {

    void subscribe();

    void handle(MsgEvent event);

}
