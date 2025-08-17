package com.maxinhai.platform.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {

    /**
     * 当前页数据
     */
    protected List<T> records;
    /**
     * 记录总条数
     */
    protected long total;
    /**
     * 分页大小
     */
    protected long size;
    /**
     * 当前页码
     */
    protected long current;
    /**
     * 总页数
     */
    private long totalPage;

    /**
     * Page转PageResult
     * @param param
     * @return
     * @param <T>
     */
    public static <T> PageResult<T> convert(Page<T> param) {
        PageResult pageResult = new PageResult();
        pageResult.setRecords(param.getRecords());
        pageResult.setTotal(param.getTotal());
        pageResult.setSize(param.getSize());
        pageResult.setCurrent(param.getCurrent());
        pageResult.setTotalPage((pageResult.getTotal() + pageResult.getSize() - 1) / pageResult.getSize());
        return pageResult;
    }

}
