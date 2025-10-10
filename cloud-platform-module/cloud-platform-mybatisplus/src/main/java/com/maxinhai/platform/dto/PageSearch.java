package com.maxinhai.platform.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public abstract class PageSearch<T> {

    /**
     * 当前页码
     */
    private long current = 1;
    /**
     * 分页大小
     */
    private long size = 10;

    public long getCurrent() {
        if (current <= 0) {
            current = 1;
        }
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public long getSize() {
        if (size <= 0) {
            size = 10;
        }
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Page<T> getPage() {
        return new Page<>(current, size);
    }
}
