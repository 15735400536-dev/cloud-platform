package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.OrderAddDTO;
import com.maxinhai.platform.dto.OrderQueryDTO;
import com.maxinhai.platform.po.Order;
import com.maxinhai.platform.vo.OrderVO;

public interface OrderService extends IService<Order> {

    Page<OrderVO> searchByPage(OrderQueryDTO param);

    OrderVO getInfo(String id);

    void remove(String[] ids);

    void add(OrderAddDTO param);

    /**
     * 统计今日订单完成数量
     * @return 今日订单完成数量
     */
    long getTodayFinishOrderCount();
}
