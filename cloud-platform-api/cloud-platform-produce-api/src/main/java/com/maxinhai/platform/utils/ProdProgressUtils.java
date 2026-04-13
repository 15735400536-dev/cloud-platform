package com.maxinhai.platform.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * MES 生产进度计算工具
 * Java 11 版本
 */
public class ProdProgressUtils {

    // 进度百分比保留2位小数
    private static final int SCALE = 2;

    /**
     * 计算生产进度百分比
     * @param finishedQty 已完成数量
     * @param planQty 计划总数量
     * @return 进度百分比 0~100
     */
    public static BigDecimal calculateProgress(Integer finishedQty, Integer planQty) {
        // 空值安全处理
        int finished = (finishedQty == null) ? 0 : finishedQty;
        int plan = (planQty == null) ? 0 : planQty;

        // 计划数量为0，进度0%
        if (plan <= 0) {
            return BigDecimal.ZERO;
        }

        // 已完成数量不能超过计划
        if (finished > plan) {
            finished = plan;
        }

        // 计算：完成数 / 计划数 * 100
        return new BigDecimal(finished)
                .multiply(new BigDecimal(100))
                .divide(new BigDecimal(plan), SCALE, RoundingMode.HALF_UP);
    }

    // ------------------- 下面是你业务直接用的方法 -------------------

    /**
     * 计算 生产订单 进度
     */
    public static BigDecimal getOrderProgress(Integer finishedQty, Integer planQty) {
        return calculateProgress(finishedQty, planQty);
    }

    /**
     * 计算 生产工单 进度
     */
    public static BigDecimal getWorkOrderProgress(Integer finishedQty, Integer planQty) {
        return calculateProgress(finishedQty, planQty);
    }

    /**
     * 计算 派工单/任务单 进度
     */
    public static BigDecimal getTaskOrderProgress(Integer finishedQty, Integer planQty) {
        return calculateProgress(finishedQty, planQty);
    }

}
