package com.maxinhai.platform.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 线程池配置类
 * 提供三种不同用途的线程池：CPU密集型、IO密集型、批量任务型
 */
@Slf4j
@Configuration
public class ThreadPoolConfig {

    // ====================== 监控统计指标 ======================
    private static final AtomicLong totalTasks = new AtomicLong(0);
    private static final AtomicLong completedTasks = new AtomicLong(0);
    private static final AtomicLong rejectedTasks = new AtomicLong(0);

    // ====================== 核心配置参数 ======================
    // CPU核心数（根据服务器自动适配）
    private static final int CPU_CORES = Runtime.getRuntime().availableProcessors();

    /**
     * CPU密集型任务线程池
     * 适用于计算密集型任务，线程数设置为CPU核心数+1
     */
    @Bean("cpuIntensiveExecutor")
    public Executor cpuIntensiveExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心配置
        executor.setCorePoolSize(CPU_CORES);
        executor.setMaxPoolSize(CPU_CORES + 1);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("cpu-pool-");

        // 拒绝策略：调用者运行（避免任务丢失）
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 优雅关闭配置
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);

        // 监控增强
        executor.setTaskDecorator(new MonitoringTaskDecorator());

        executor.initialize();
        return executor;
    }

    /**
     * IO密集型任务线程池
     * 适用于网络IO、数据库操作等阻塞型任务
     */
    @Bean("ioIntensiveExecutor")
    public Executor ioIntensiveExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心配置（线程数 = CPU核心数 * 2）
        executor.setCorePoolSize(CPU_CORES * 2);
        executor.setMaxPoolSize(CPU_CORES * 4);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("io-pool-");

        // 动态参数调整（根据系统负载）
        executor.setAllowCoreThreadTimeOut(true);
        executor.setKeepAliveSeconds(60);

        // 拒绝策略：记录日志后丢弃
        executor.setRejectedExecutionHandler((r, e) -> {
            rejectedTasks.incrementAndGet();
            System.err.println("任务被拒绝: " + r.toString());
        });

        executor.initialize();
        return executor;
    }

    /**
     * 批量任务线程池
     * 适用于后台批处理任务，低优先级
     */
    @Bean("batchTaskExecutor")
    public Executor batchTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心配置
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("batch-pool-");

        // 优先级控制
        executor.setTaskDecorator(new PriorityTaskDecorator());

        // 拒绝策略：直接丢弃
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());

        executor.initialize();
        return executor;
    }

    // ====================== 监控和统计功能 ======================

    /**
     * 任务装饰器 - 用于监控统计
     */
    static class MonitoringTaskDecorator implements TaskDecorator {
        @Override
        public Runnable decorate(Runnable runnable) {
            totalTasks.incrementAndGet();
            long startTime = System.currentTimeMillis();

            return () -> {
                try {
                    runnable.run();
                } finally {
                    completedTasks.incrementAndGet();
                    long duration = System.currentTimeMillis() - startTime;
                    // 可在此处记录任务执行时间
                }
            };
        }
    }

    /**
     * 优先级任务装饰器
     */
    static class PriorityTaskDecorator implements TaskDecorator {
        @Override
        public Runnable decorate(Runnable runnable) {
            return new PriorityRunnable(runnable, Thread.NORM_PRIORITY);
        }

        static class PriorityRunnable implements Runnable {
            private final Runnable runnable;
            private final int priority;

            PriorityRunnable(Runnable runnable, int priority) {
                this.runnable = runnable;
                this.priority = priority;
            }

            @Override
            public void run() {
                Thread currentThread = Thread.currentThread();
                int originalPriority = currentThread.getPriority();
                try {
                    currentThread.setPriority(priority);
                    runnable.run();
                } finally {
                    currentThread.setPriority(originalPriority);
                }
            }
        }
    }

    // ====================== 监控统计API ======================

    /**
     * 获取线程池统计信息
     */
    public static ThreadPoolStats getThreadPoolStats() {
        return new ThreadPoolStats(
                totalTasks.get(),
                completedTasks.get(),
                rejectedTasks.get()
        );
    }

    /**
     * 线程池统计信息DTO
     */
    public static class ThreadPoolStats {
        private final long totalTasks;
        private final long completedTasks;
        private final long rejectedTasks;

        public ThreadPoolStats(long totalTasks, long completedTasks, long rejectedTasks) {
            this.totalTasks = totalTasks;
            this.completedTasks = completedTasks;
            this.rejectedTasks = rejectedTasks;
        }

        // 计算任务成功率
        public double getSuccessRate() {
            if (totalTasks == 0) return 0.0;
            return (double) completedTasks / totalTasks * 100;
        }

        // 计算拒绝率
        public double getRejectRate() {
            if (totalTasks == 0) return 0.0;
            return (double) rejectedTasks / totalTasks * 100;
        }

        // Getters
        public long getTotalTasks() { return totalTasks; }
        public long getCompletedTasks() { return completedTasks; }
        public long getRejectedTasks() { return rejectedTasks; }
    }

}
