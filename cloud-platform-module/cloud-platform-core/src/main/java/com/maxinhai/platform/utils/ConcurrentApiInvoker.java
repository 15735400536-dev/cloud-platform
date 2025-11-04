package com.maxinhai.platform.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 并发接口调用工具类
 */
@Slf4j
public class ConcurrentApiInvoker {

    /**
     * 并发调用接口指定次数
     *
     * @param callCount 调用次数
     * @param apiCaller 接口调用逻辑（通过Supplier封装，无参数有返回值）
     * @param <T>       接口返回值类型
     * @return 所有接口调用的结果列表（顺序与提交顺序一致）
     * @throws Exception 当任一接口调用抛出异常时抛出
     */
    public static <T> List<T> invokeConcurrently(int callCount, Supplier<T> apiCaller) throws Exception {
        // 校验参数
        if (callCount <= 0) {
            throw new IllegalArgumentException("调用次数必须为正数: " + callCount);
        }
        if (apiCaller == null) {
            throw new IllegalArgumentException("接口调用逻辑不能为null");
        }

        // 创建线程池（核心线程数为调用次数，确保并发执行）
        // 注意：实际生产环境可根据需求调整线程池大小（如根据CPU核心数限制）
        ExecutorService executor = Executors.newFixedThreadPool(callCount);

        try {
            // 存储所有异步任务的Future
            List<CompletableFuture<T>> futures = new ArrayList<>(callCount);

            // 提交callCount个并发任务
            for (int i = 0; i < callCount; i++) {
                // 使用CompletableFuture异步执行接口调用
                CompletableFuture<T> future = CompletableFuture.supplyAsync(apiCaller, executor);
                futures.add(future);
            }

            // 等待所有任务完成（阻塞当前线程）
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            // 收集所有结果（保持提交顺序）
            return futures.stream()
                    .map(future -> {
                        try {
                            // 获取结果（此时任务已完成，不会阻塞）
                            return future.get();
                        } catch (Exception e) {
                            // 包装异常并抛出（由调用方处理）
                            throw new RuntimeException("接口调用失败", e);
                        }
                    })
                    .collect(Collectors.toList());

        } finally {
            // 关闭线程池释放资源
            executor.shutdown();
        }
    }

    // ------------------------------ 示例用法 ------------------------------
    public static void main(String[] args) throws Exception {
        // 1. 定义接口调用逻辑（这里模拟一个带延迟的接口）
        Supplier<String> mockApiCall = () -> {
            try {
                // 模拟接口处理耗时（1秒）
                Thread.sleep(1000);
                // 返回当前线程名（用于验证并发）
                return "接口返回结果 - 线程: " + Thread.currentThread().getName();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 恢复中断状态
                throw new RuntimeException("接口调用被中断", e);
            }
        };

        // 2. 并发调用5次接口
        int callCount = 5;
        List<String> results = invokeConcurrently(callCount, mockApiCall);

        // 3. 打印结果
        log.info("===== 所有接口调用完成 =====");
        results.forEach(log::info);
    }

}
