package com.maxinhai.platform.utils;

import cn.hutool.core.date.DateUtil;

import java.util.Date;

public class DateUtils {

    /**
     * 获取今日开始时间
     * @return
     */
    public static Date getBeginTimeOfToday() {
        return DateUtil.beginOfDay(new Date());
    }

    /**
     * 获取今日结束时间
     * @return
     */
    public static Date getEndTimeOfToday() {
        return DateUtil.endOfDay(new Date());
    }

    /**
     * 获取当月开始时间
     * @return
     */
    public static Date getBeginTimeOfMonth() {
        return DateUtil.beginOfMonth(new Date());
    }

    /**
     * 获取当月结束时间
     * @return
     */
    public static Date getEndTimeOfMonth() {
        return DateUtil.endOfMonth(new Date());
    }

    /**
     * 获取当年开始时间
     * @return
     */
    public static Date getBeginTimeOfYear() {
        return DateUtil.beginOfYear(new Date());
    }

    /**
     * 获取当年结束时间
     * @return
     */
    public static Date getEndTimeOfYear() {
        return DateUtil.endOfYear(new Date());
    }

}
