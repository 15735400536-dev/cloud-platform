package com.maxinhai.platform.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public abstract class PageSearch<T> {

    /**
     * 当前页码
     */
    private long current  = 1;
    /**
     * 分页大小
     */
    private long size = 10;

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Page<T> getPage() {
        return new Page<>(current, size);
    }
}
