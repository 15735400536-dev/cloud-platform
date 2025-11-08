package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.OperateRecordQueryDTO;
import com.maxinhai.platform.po.OperateRecord;
import com.maxinhai.platform.enums.OperateType;
import com.maxinhai.platform.vo.OperateRecordVO;

import java.util.List;
import java.util.Map;

public interface OperateRecordService extends IService<OperateRecord> {

    Page<OperateRecordVO> searchByPage(OperateRecordQueryDTO param);

    OperateRecordVO getInfo(String id);

    void remove(String[] ids);

    /**
     * 创建派工单操作记录
     * @param operateType 操作类型
     * @param taskOrderId 派工单ID
     */
    void createRecord(OperateType operateType, String taskOrderId);

    /**
     * 根据派工单ID、操作类型查询派工单操作记录
     * @param taskOrderId 派工单ID
     * @param operateType 操作类型
     */
    List<OperateRecord> getOperateRecords(String taskOrderId, OperateType operateType);

    /**
     * 根据派工单操作记录计算工时
     * @param recordList 派工单操作记录集合
     * @return
     */
    long calculateWorkTime(List<OperateRecord> recordList);

    /**
     * 根据派工单ID计算工时
     * @param taskOrderId 派工单ID
     * @return
     */
    long calculateWorkTime(String taskOrderId);

    /**
     * 根据派工单ID集合批量计算工时
     * @param taskOrderIds 派工单ID集合
     * @return
     */
    Map<String, Long> batchCalculateWorkTime(List<String> taskOrderIds);

    /**
     * 根据工单ID计算工时
     * @param workOrderId 工单ID
     * @return
     */
    long calculateWorkOrderWorkTime(String workOrderId);

    /**
     * 根据派工单ID获取操作记录
     * @param taskOrderId 派工单ID
     * @return
     */
    List<OperateRecordVO> getOperateRecords(String taskOrderId);

}
