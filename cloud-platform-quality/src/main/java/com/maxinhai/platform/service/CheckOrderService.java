package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.CheckOrderAddDTO;
import com.maxinhai.platform.dto.CheckOrderEditDTO;
import com.maxinhai.platform.dto.CheckOrderQueryDTO;
import com.maxinhai.platform.po.CheckOrder;
import com.maxinhai.platform.po.technology.Operation;
import com.maxinhai.platform.po.Product;
import com.maxinhai.platform.po.TaskOrder;
import com.maxinhai.platform.vo.CheckOrderVO;

public interface CheckOrderService extends IService<CheckOrder> {

    Page<CheckOrderVO> searchByPage(CheckOrderQueryDTO param);

    CheckOrderVO getInfo(String id);

    void remove(String[] ids);

    void edit(CheckOrderEditDTO param);

    void add(CheckOrderAddDTO param);

    /**
     * 根据工单ID生成质检单
     *
     * @param workOrderId 工单ID
     */
    void generate(String workOrderId);

    /**
     * 根据派工单ID生成工序质检单
     *
     * @param taskOrderId 派工单ID
     */
    void generateCheckOrder(String taskOrderId);

    /**
     * 生成自检质检单
     *
     * @param workOrderId 工单ID
     * @param operationId 工序ID
     */
    void generateSelfCheckOrder(String workOrderId, String operationId);

    /**
     * 生成互检质检单
     *
     * @param workOrderId 工单ID
     * @param operationId 工序ID
     */
    void generateMutualCheckOrder(String workOrderId, String operationId);

    /**
     * 生成专检质检单
     *
     * @param workOrderId 工单ID
     * @param operationId 工序ID
     */
    void generateSpecialCheckOrder(String workOrderId, String operationId);

    /**
     * 生成自检质检单
     *
     * @param taskOrder 派工单信息
     * @param product 产品信息
     * @param operation 工序信息
     */
    void generateSelfCheckOrder(TaskOrder taskOrder, Product product, Operation operation);

    /**
     * 生成互检质检单
     *
     * @param taskOrder 派工单信息
     * @param product 产品信息
     * @param operation 工序信息
     */
    void generateMutualCheckOrder(TaskOrder taskOrder, Product product, Operation operation);

    /**
     * 生成专检质检单
     *
     * @param taskOrder 派工单信息
     * @param product 产品信息
     * @param operation 工序信息
     */
    void generateSpecialCheckOrder(TaskOrder taskOrder, Product product, Operation operation);

}
