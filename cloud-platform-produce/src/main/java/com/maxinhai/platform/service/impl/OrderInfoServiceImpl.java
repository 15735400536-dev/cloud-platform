package com.maxinhai.platform.service.impl;

import com.maxinhai.platform.bo.OrderInfoBO;
import com.maxinhai.platform.mapper.OrderMapper;
import com.maxinhai.platform.service.OrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class OrderInfoServiceImpl implements OrderInfoService {

    @Resource
    private OrderMapper orderMapper;

    // 注意：三个异步查询不能和业务代码写在一个Service里，不然@Async方法会失效，调用的是自己本身的方法，不是代理类的方法
    @Override
    @Async("ioIntensiveExecutor")
    public CompletableFuture<OrderInfoBO> countOrderSimpleInfoAsync() {
        // 打印当前线程名称和线程池信息
        log.info("线程ID：{}, 执行线程名称：{}, 是否为守护线程：{}, ", Thread.currentThread().getId(), Thread.currentThread().getName(), Thread.currentThread().isDaemon());
        return CompletableFuture.completedFuture(orderMapper.countOrderSimpleInfo());
    }

    @Override
    @Async("ioIntensiveExecutor")
    public CompletableFuture<OrderInfoBO> countWorkOrderSimpleInfoAsync() {
        // 打印当前线程名称和线程池信息
        log.info("线程ID：{}, 执行线程名称：{}, 是否为守护线程：{}, ", Thread.currentThread().getId(), Thread.currentThread().getName(), Thread.currentThread().isDaemon());
        return CompletableFuture.completedFuture(orderMapper.countWorkOrderSimpleInfo());
    }

    @Override
    @Async("ioIntensiveExecutor")
    public CompletableFuture<OrderInfoBO> countTaskOrderSimpleInfoAsync() {
        // 打印当前线程名称和线程池信息
        log.info("线程ID：{}, 执行线程名称：{}, 是否为守护线程：{}, ", Thread.currentThread().getId(), Thread.currentThread().getName(), Thread.currentThread().isDaemon());
        return CompletableFuture.completedFuture(orderMapper.countTaskOrderSimpleInfo());
    }

}
