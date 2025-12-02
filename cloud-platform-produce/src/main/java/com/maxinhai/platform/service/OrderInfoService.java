package com.maxinhai.platform.service;

import com.maxinhai.platform.bo.OrderInfoBO;

import java.util.concurrent.CompletableFuture;

public interface OrderInfoService {

    /**
     * 异步查询订单信息
     * @return 订单信息
     */
    CompletableFuture<OrderInfoBO> countOrderSimpleInfoAsync();

    /**
     * 异步查询工单信息
     * @return 工单信息
     */
    CompletableFuture<OrderInfoBO> countWorkOrderSimpleInfoAsync();

    /**
     * 异步查询派工单信息
     * @return 派工单信息
     */
    CompletableFuture<OrderInfoBO> countTaskOrderSimpleInfoAsync();

}
