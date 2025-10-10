package com.maxinhai.platform.utils;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/**
 * @ClassName：FixedSizeEventQueue
 * @Author: XinHai.Ma
 * @Date: 2025/8/27 17:48
 * @Description: 定长消息队列，用于存储和消费事件
 */
public class FixedSizeEventQueue<T> {

    // 存储事件的队列
    private final Queue<T> queue;
    // 队列的最大容量
    private final int maxCapacity;
    // 队列满时的策略
    private final OverflowPolicy overflowPolicy;
    // 用于线程安全的锁
    private final ReentrantLock lock = new ReentrantLock();
    // 队列非空条件
    private final Condition notEmpty = lock.newCondition();
    // 队列未满条件
    private final Condition notFull = lock.newCondition();

    /**
     * 队列满时的处理策略
     */
    public enum OverflowPolicy {
        // 阻塞直到队列有空间
        BLOCK,
        // 覆盖最旧的元素
        OVERWRITE
    }

    /**
     * 构造函数
     *
     * @param maxCapacity    队列最大容量
     * @param overflowPolicy 队列满时的处理策略
     */
    public FixedSizeEventQueue(int maxCapacity, OverflowPolicy overflowPolicy) {
        if (maxCapacity <= 0) {
            throw new IllegalArgumentException("最大容量必须为正数");
        }
        this.maxCapacity = maxCapacity;
        this.overflowPolicy = overflowPolicy;
        this.queue = new ArrayDeque<>(maxCapacity);
    }

    /**
     * 构造函数，默认使用阻塞策略
     *
     * @param maxCapacity 队列最大容量
     */
    public FixedSizeEventQueue(int maxCapacity) {
        this(maxCapacity, OverflowPolicy.BLOCK);
    }

    /**
     * 向队列中添加事件
     *
     * @param event 要添加的事件
     * @throws InterruptedException 如果线程被中断
     */
    public void offer(T event) throws InterruptedException {
        if (event == null) {
            throw new NullPointerException("事件不能为null");
        }

        lock.lockInterruptibly();
        try {
            // 根据不同的策略处理队列满的情况
            if (overflowPolicy == OverflowPolicy.BLOCK) {
                // 阻塞直到队列有空间
                while (queue.size() == maxCapacity) {
                    notFull.await();
                }
                queue.offer(event);
            } else {
                // 覆盖最旧的元素
                if (queue.size() == maxCapacity) {
                    queue.poll(); // 移除最旧的元素
                }
                queue.offer(event);
            }
            // 通知消费者队列非空
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 从队列中获取并移除事件（阻塞直到有事件可用）
     *
     * @return 事件
     * @throws InterruptedException 如果线程被中断
     */
    public T take() throws InterruptedException {
        lock.lockInterruptibly();
        try {
            // 阻塞直到队列非空
            while (queue.isEmpty()) {
                notEmpty.await();
            }
            T event = queue.poll();
            // 通知生产者队列未满
            notFull.signal();
            return event;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 尝试从队列中获取并移除事件（非阻塞）
     *
     * @return 事件，如果队列为空则返回null
     */
    public T poll() {
        lock.lock();
        try {
            if (queue.isEmpty()) {
                return null;
            }
            T event = queue.poll();
            notFull.signal();
            return event;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 消费队列中的所有事件
     *
     * @param consumer 事件消费者
     */
    public void consumeAll(Consumer<T> consumer) {
        lock.lock();
        try {
            T event;
            while ((event = queue.poll()) != null) {
                consumer.accept(event);
                notFull.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 获取当前队列中的事件数量
     *
     * @return 事件数量
     */
    public int size() {
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 检查队列是否为空
     *
     * @return 如果队列为空则返回true，否则返回false
     */
    public boolean isEmpty() {
        lock.lock();
        try {
            return queue.isEmpty();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 检查队列是否已满
     *
     * @return 如果队列已满则返回true，否则返回false
     */
    public boolean isFull() {
        lock.lock();
        try {
            return queue.size() == maxCapacity;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 清空队列
     */
    public void clear() {
        lock.lock();
        try {
            queue.clear();
            notFull.signalAll();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 获取队列的最大容量
     *
     * @return 最大容量
     */
    public int getMaxCapacity() {
        return maxCapacity;
    }

}
