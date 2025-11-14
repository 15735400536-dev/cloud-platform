# 项目文档
## 组件版本
| SpringCloudAlibaba | SpringCloud | SpringBoot | Nacos | Sentinel | RocketMQ | Seata |
|--------------------|-------------|------------|-------|----------|----------|-------|
| 2021.0.5.0         | 2021.0.5    | 2.6.13     | 2.2.0 | 1.8.6    | 4.9.4    | 1.6.1 |

## 服务
| 服务标识                     | 服务名称     | 服务描述  |
|--------------------------|----------|-------|
| cloud-platform-gateway   | 网关       | 9999  |
| cloud-platform-system    | 系统管理模块   | 10010 |
| cloud-platform-mdm       | 主数据服务    | 10020 |
| cloud-platform-equipment | 设备管理模块   | 10030 |
| cloud-platform-produce   | 生产管理模块   | 10040 |
| cloud-platform-warehouse | 仓库管理模块   | 10050 |
| cloud-platform-quality   | 质量管理模块   | 10060 |
| cloud-platform-zlm       | zlm流媒体服务 | 10070 |
| cloud-platform-admin     | 监控服务     | 10000 |

## 项目
| 项目标识                        | 项目名称 | 项目端口  |
|-----------------------------|------|-------|
| cloud-platform-alarm        | 设备告警 | 20010 |
| cloud-platform-diary        | 日记项目 | 20020 |
| cloud-platform-friendcircle | 朋友圈啊 | 20030 |
| cloud-platform-missav       | 视频网站 | 20040 |
| cloud-platform-news         | 新闻网站 | 20050 |
| cloud-platform-novel        | 小说网站 | 20060 |

## 中间件
| 中间件          | 描述        | 端口         | 访问界面                    |
|--------------|-----------|------------|-------------------------|
| nacos        | 服务注册、配置管理 | 8848       | http://localhost:8848/  |
| mysql        | 关系型数据库    | 3306       |                         |
| pgsql        | 关系型数据库    | 5432       |                         |
| redis        | 分布式缓存     | 6379       |                         |
| xxl-job      | 分布式定时任务   | 8080       | http://localhost:8080/xxl-job-admin/  |
| rabbitmq     | 消息队列      | 5672、15672 | http://localhost:15672/ |
| minio        | 文件存储      | 9000、9001  | http://localhost:9001/browser |
| elasticsearch | 分布式搜索引擎   | 9200、9300      | http://localhost:9200/ |
| mongodb      | 非关系型数据库   | 27017       |  |

### 清除端口占用进程
```
netstat -aon | findstr <PORT>
taskkill /F /PID <PID>
```

### 解决Docker端口被占用问题
```angular2html
(HTTP code 500) server error - ports are not available: exposing port TCP 0.0.0.0:10010 -> 127.0.0.1:0: listen tcp 0.0.0.0:10010: 
bind: An attempt was made to access a socket in a way forbidden by its access permissions.
```
```angular2html
netstat -ano | findstr 6041
net stop winnat
net start winnat
```