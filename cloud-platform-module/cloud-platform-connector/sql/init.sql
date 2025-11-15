-- 通用连接配置表（存储所有中间件的基础连接信息）
CREATE TABLE conn_connect
(
    id          VARCHAR(32) PRIMARY KEY,                        -- 主键
    "key"       VARCHAR(50) NOT NULL,                           -- 连接标识（唯一键）
    type        VARCHAR(20) NOT NULL,                           -- 连接类型（对应ConnectType枚举）
    ip          VARCHAR(64) NOT NULL,                           -- 服务IP地址
    port        INT         NOT NULL,                           -- 服务端口
    username    VARCHAR(100),                                   -- 登录账号（可为空）
    password    VARCHAR(50),                                    -- 登录密码（可为空）
    del_flag    INT         NOT NULL DEFAULT 0,                 -- 逻辑删除：0-未删除，1-已删除
    create_by   VARCHAR(32) NOT NULL,                           -- 创建人
    create_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    update_by   VARCHAR(32),                                    -- 更新人
    update_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP  -- 更新时间
);

-- 添加唯一约束（确保key唯一）
ALTER TABLE conn_connect
    ADD CONSTRAINT uk_conn_connect_key UNIQUE ("key");

-- 添加字段注释
COMMENT
ON COLUMN conn_connect.id IS '主键';
COMMENT
ON COLUMN conn_connect."key" IS '连接标识（唯一键）';
COMMENT
ON COLUMN conn_connect.type IS '连接类型（对应ConnectType枚举：API、MQ、MQTT、WEBSOCKET等）';
COMMENT
ON COLUMN conn_connect.ip IS '服务IP地址';
COMMENT
ON COLUMN conn_connect.port IS '服务端口';
COMMENT
ON COLUMN conn_connect.username IS '登录账号（可为空）';
COMMENT
ON COLUMN conn_connect.password IS '登录密码（可为空，建议加密存储）';
COMMENT
ON COLUMN conn_connect.del_flag IS '逻辑删除：0-未删除，1-已删除';
COMMENT
ON COLUMN conn_connect.create_by IS '创建人';
COMMENT
ON COLUMN conn_connect.create_time IS '创建时间';
COMMENT
ON COLUMN conn_connect.update_by IS '更新人';
COMMENT
ON COLUMN conn_connect.update_time IS '更新时间';

-- 表注释
COMMENT
ON TABLE conn_connect IS '通用连接配置表（存储所有中间件的基础连接信息）';

-- 索引：加速按类型查询
CREATE INDEX idx_conn_connect_type ON conn_connect (type);

-- API连接特有配置表（关联conn_connect）
CREATE TABLE conn_api
(
    id          VARCHAR(32) PRIMARY KEY,                         -- 主键
    connect_id  VARCHAR(32)  NOT NULL,                           -- 关联conn_connect的id
    method      VARCHAR(10)  NOT NULL,                           -- 请求方法（如GET、POST）
    url         VARCHAR(255) NOT NULL,                           -- API接口地址
    del_flag    INT          NOT NULL DEFAULT 0,                 -- 逻辑删除：0-未删除，1-已删除
    create_by   VARCHAR(32)  NOT NULL,                           -- 创建人
    create_time TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    update_by   VARCHAR(32),                                     -- 更新人
    update_time TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP  -- 更新时间
);

-- 外键关联（主表删除时子表同步删除）
ALTER TABLE conn_api
    ADD CONSTRAINT fk_conn_api_connect
        FOREIGN KEY (connect_id) REFERENCES conn_connect (id) ON DELETE CASCADE;

-- 添加字段注释
COMMENT
ON COLUMN conn_api.id IS '主键';
COMMENT
ON COLUMN conn_api.connect_id IS '关联conn_connect的id';
COMMENT
ON COLUMN conn_api.method IS '请求方法（如GET、POST）';
COMMENT
ON COLUMN conn_api.url IS 'API接口地址';
COMMENT
ON COLUMN conn_api.del_flag IS '逻辑删除：0-未删除，1-已删除';
COMMENT
ON COLUMN conn_api.create_by IS '创建人';
COMMENT
ON COLUMN conn_api.create_time IS '创建时间';
COMMENT
ON COLUMN conn_api.update_by IS '更新人';
COMMENT
ON COLUMN conn_api.update_time IS '更新时间';

-- 表注释
COMMENT
ON TABLE conn_api IS 'API连接特有配置表（关联conn_connect）';

-- 索引：加速关联查询
CREATE INDEX idx_conn_api_connect_id ON conn_api (connect_id);

-- 消息队列（MQ）特有配置表（关联conn_connect）
CREATE TABLE conn_mq
(
    id          VARCHAR(32) PRIMARY KEY,                         -- 主键
    connect_id  VARCHAR(32)  NOT NULL,                           -- 关联conn_connect的id
    topic       VARCHAR(255) NOT NULL,                           -- 队列主题（如RabbitMQ的队列名、Kafka的topic）
    del_flag    INT          NOT NULL DEFAULT 0,                 -- 逻辑删除：0-未删除，1-已删除
    create_by   VARCHAR(32)  NOT NULL,                           -- 创建人
    create_time TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    update_by   VARCHAR(32),                                     -- 更新人
    update_time TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP  -- 更新时间
);

-- 外键关联
ALTER TABLE conn_mq
    ADD CONSTRAINT fk_conn_mq_connect
        FOREIGN KEY (connect_id) REFERENCES conn_connect (id) ON DELETE CASCADE;

-- 添加字段注释
COMMENT
ON COLUMN conn_mq.id IS '主键';
COMMENT
ON COLUMN conn_mq.connect_id IS '关联conn_connect的id';
COMMENT
ON COLUMN conn_mq.topic IS '队列主题（如RabbitMQ的队列名、Kafka的topic）';
COMMENT
ON COLUMN conn_mq.del_flag IS '逻辑删除：0-未删除，1-已删除';
COMMENT
ON COLUMN conn_mq.create_by IS '创建人';
COMMENT
ON COLUMN conn_mq.create_time IS '创建时间';
COMMENT
ON COLUMN conn_mq.update_by IS '更新人';
COMMENT
ON COLUMN conn_mq.update_time IS '更新时间';

-- 表注释
COMMENT
ON TABLE conn_mq IS '消息队列（MQ）特有配置表（关联conn_connect）';

-- 索引
CREATE INDEX idx_conn_mq_connect_id ON conn_mq (connect_id);

-- MQTT特有配置表（关联conn_connect）
CREATE TABLE conn_mqtt
(
    id          VARCHAR(32) PRIMARY KEY,                         -- 主键
    connect_id  VARCHAR(32)  NOT NULL,                           -- 关联conn_connect的id
    topic       VARCHAR(255) NOT NULL,                           -- MQTT主题
    qos         INT          NOT NULL CHECK (qos IN (0, 1, 2)),  -- QoS等级（0/1/2）
    del_flag    INT          NOT NULL DEFAULT 0,                 -- 逻辑删除：0-未删除，1-已删除
    create_by   VARCHAR(32)  NOT NULL,                           -- 创建人
    create_time TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    update_by   VARCHAR(32),                                     -- 更新人
    update_time TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP  -- 更新时间
);

-- 外键关联
ALTER TABLE conn_mqtt
    ADD CONSTRAINT fk_conn_mqtt_connect
        FOREIGN KEY (connect_id) REFERENCES conn_connect (id) ON DELETE CASCADE;

-- 添加字段注释
COMMENT
ON COLUMN conn_mqtt.id IS '主键';
COMMENT
ON COLUMN conn_mqtt.connect_id IS '关联conn_connect的id';
COMMENT
ON COLUMN conn_mqtt.topic IS 'MQTT主题';
COMMENT
ON COLUMN conn_mqtt.qos IS 'QoS等级（0/1/2）';
COMMENT
ON COLUMN conn_mqtt.del_flag IS '逻辑删除：0-未删除，1-已删除';
COMMENT
ON COLUMN conn_mqtt.create_by IS '创建人';
COMMENT
ON COLUMN conn_mqtt.create_time IS '创建时间';
COMMENT
ON COLUMN conn_mqtt.update_by IS '更新人';
COMMENT
ON COLUMN conn_mqtt.update_time IS '更新时间';

-- 表注释
COMMENT
ON TABLE conn_mqtt IS 'MQTT特有配置表（关联conn_connect）';

-- 索引
CREATE INDEX idx_conn_mqtt_connect_id ON conn_mqtt (connect_id);

-- WebSocket特有配置表（关联conn_connect）
CREATE TABLE conn_websocket
(
    id          VARCHAR(32) PRIMARY KEY,                         -- 主键
    connect_id  VARCHAR(32)  NOT NULL,                           -- 关联conn_connect的id
    topic       VARCHAR(255) NOT NULL,                           -- WebSocket消息主题
    del_flag    INT          NOT NULL DEFAULT 0,                 -- 逻辑删除：0-未删除，1-已删除
    create_by   VARCHAR(32)  NOT NULL,                           -- 创建人
    create_time TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    update_by   VARCHAR(32),                                     -- 更新人
    update_time TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP  -- 更新时间
);

-- 外键关联
ALTER TABLE conn_websocket
    ADD CONSTRAINT fk_conn_websocket_connect
        FOREIGN KEY (connect_id) REFERENCES conn_connect (id) ON DELETE CASCADE;

-- 添加字段注释
COMMENT
ON COLUMN conn_websocket.id IS '主键';
COMMENT
ON COLUMN conn_websocket.connect_id IS '关联conn_connect的id';
COMMENT
ON COLUMN conn_websocket.topic IS 'WebSocket消息主题';
COMMENT
ON COLUMN conn_websocket.del_flag IS '逻辑删除：0-未删除，1-已删除';
COMMENT
ON COLUMN conn_websocket.create_by IS '创建人';
COMMENT
ON COLUMN conn_websocket.create_time IS '创建时间';
COMMENT
ON COLUMN conn_websocket.update_by IS '更新人';
COMMENT
ON COLUMN conn_websocket.update_time IS '更新时间';

-- 表注释
COMMENT
ON TABLE conn_websocket IS 'WebSocket特有配置表（关联conn_connect）';

-- 索引
CREATE INDEX idx_conn_websocket_connect_id ON conn_websocket (connect_id);