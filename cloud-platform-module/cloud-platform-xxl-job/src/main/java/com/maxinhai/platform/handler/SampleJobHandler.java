package com.maxinhai.platform.handler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class SampleJobHandler {

    private static final Logger logger = LoggerFactory.getLogger(SampleJobHandler.class);

    /**
     * 简单任务示例
     */
    @XxlJob("simpleJobHandler")
    public void simpleJobHandler() throws Exception {
        logger.info("XXL-Job 简单任务执行开始...");

        // 任务执行逻辑
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        XxlJobHelper.log("当前时间: {}", currentTime);

        logger.info("XXL-Job 简单任务执行结束...");
    }

    /**
     * 分片任务示例
     */
    @XxlJob("shardingJobHandler")
    public void shardingJobHandler() throws Exception {
        // 获取分片参数
        int shardIndex = XxlJobHelper.getShardIndex();
        int shardTotal = XxlJobHelper.getShardTotal();

        logger.info("分片任务执行 - 分片索引: {}, 总分片数: {}", shardIndex, shardTotal);

        // 不同分片执行不同逻辑
        XxlJobHelper.log("分片 {} 处理中...", shardIndex);
    }

    @XxlJob("Demo")
    public ReturnT<String> demo() {
        String param = XxlJobHelper.getJobParam();
        XxlJobHelper.log("测试开始");
        logger.info("任务参数: {}", param);
        XxlJobHelper.log("测试结束");
        return ReturnT.SUCCESS;
    }

}
