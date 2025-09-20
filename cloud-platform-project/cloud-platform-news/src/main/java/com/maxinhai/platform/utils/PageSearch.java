package com.maxinhai.platform.utils;

public abstract class PageSearch {

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

}
