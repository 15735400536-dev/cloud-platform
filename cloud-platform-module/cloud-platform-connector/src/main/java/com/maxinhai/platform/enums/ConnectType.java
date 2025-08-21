package com.maxinhai.platform.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConnectType {

    API,
    MQTT,
    MQ,
    WEBSOCKET;

}
