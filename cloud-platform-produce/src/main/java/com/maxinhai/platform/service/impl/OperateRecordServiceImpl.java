package com.maxinhai.platform.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.dto.OperateRecordQueryDTO;
import com.maxinhai.platform.po.OperateRecord;
import com.maxinhai.platform.po.Order;
import com.maxinhai.platform.po.TaskOrder;
import com.maxinhai.platform.enums.OperateType;
import com.maxinhai.platform.mapper.OperateRecordMapper;
import com.maxinhai.platform.mapper.TaskOrderMapper;
import com.maxinhai.platform.service.OperateRecordService;
import com.maxinhai.platform.vo.OperateRecordVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OperateRecordServiceImpl extends ServiceImpl<OperateRecordMapper, OperateRecord> implements OperateRecordService {

    @Resource
    private OperateRecordMapper operateRecordMapper;
    @Resource
    private TaskOrderMapper taskOrderMapper;

    @Override
    public Page<OperateRecordVO> searchByPage(OperateRecordQueryDTO param) {
        return operateRecordMapper.selectJoinPage(param.getPage(), OperateRecordVO.class,
                new MPJLambdaWrapper<OperateRecord>()
                        .innerJoin(TaskOrder.class, TaskOrder::getId, OperateRecord::getTaskOrderId)
                        .eq(StrUtil.isNotBlank(param.getTaskOrderId()), OperateRecord::getTaskOrderId, param.getTaskOrderId())
                        .eq(Objects.nonNull(param.getOperateType()), OperateRecord::getOperateType, param.getOperateType())
                        .orderByDesc(Order::getCreateTime));
    }

    @Override
    public OperateRecordVO getInfo(String id) {
        return operateRecordMapper.selectJoinOne(OperateRecordVO.class, new MPJLambdaWrapper<OperateRecord>()
                .innerJoin(TaskOrder.class, TaskOrder::getId, OperateRecord::getTaskOrderId)
                .eq(OperateRecord::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        operateRecordMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void createRecord(OperateType operateType, String taskOrderId) {
        OperateRecord record = new OperateRecord();
        record.setTaskOrderId(taskOrderId);
        record.setOperateType(operateType);
        record.setOperateTime(new Date());
        operateRecordMapper.insert(record);
    }

    @Override
    public List<OperateRecord> getOperateRecords(String taskOrderId, OperateType operateType) {
        return operateRecordMapper.selectList(new LambdaQueryWrapper<OperateRecord>()
                .eq(OperateRecord::getTaskOrderId, taskOrderId)
                .eq(Objects.nonNull(operateType) && !OperateType.ALL.equals(operateType), OperateRecord::getOperateType, operateType)
                .orderByAsc(OperateRecord::getOperateTime));
    }

    @Override
    public long calculateWorkTime(List<OperateRecord> recordList) {
        List<String> taskOrderIds = recordList.stream().map(OperateRecord::getTaskOrderId).distinct().collect(Collectors.toList());
        if (taskOrderIds.size() > 1) {
            throw new BusinessException("数据不合法,存在多个派工单数据!");
        }
        // 开工记录
        List<OperateRecord> startRecords = recordList.stream()
                .filter(operateRecord -> OperateType.START.equals(operateRecord.getOperateType()))
                .collect(Collectors.toList());
        if(CollectionUtils.isEmpty(startRecords)) {
            throw new BusinessException("数据异常,开工记录不存在!");
        }
        if (startRecords.size() > 1) {
            throw new BusinessException("数据异常,存在多条开工记录!");
        }
        // 暂停记录
        List<OperateRecord> pauseRecords = recordList.stream()
                .filter(operateRecord -> OperateType.PAUSE.equals(operateRecord.getOperateType()))
                .sorted(Comparator.comparing(OperateRecord::getOperateTime))
                .collect(Collectors.toList());
        // 复工记录
        List<OperateRecord> resumeRecords = recordList.stream()
                .filter(operateRecord -> OperateType.RESUME.equals(operateRecord.getOperateType()))
                .sorted(Comparator.comparing(OperateRecord::getOperateTime))
                .collect(Collectors.toList());
        if (pauseRecords.size() != resumeRecords.size()) {
            throw new BusinessException("暂停记录与复工记录不一致!");
        }
        // 报工记录
        List<OperateRecord> reportRecords = recordList.stream()
                .filter(operateRecord -> OperateType.REPORT.equals(operateRecord.getOperateType()))
                .collect(Collectors.toList());
        if(CollectionUtils.isEmpty(reportRecords)) {
            throw new BusinessException("数据异常,报工记录不存在!");
        }
        if (reportRecords.size() > 1) {
            throw new BusinessException("数据异常,存在多条报工记录!");
        }
        // 计算总时长(秒)
        OperateRecord startRecord = startRecords.get(0);
        OperateRecord reportRecord = reportRecords.get(0);
        long totalSeconds = DateUtil.between(startRecord.getOperateTime(), reportRecord.getOperateTime(), DateUnit.SECOND);

        // 计算暂停时长(秒)
        long pauseSeconds = 0;
        for (int i = 0; i < pauseRecords.size(); i++) {
            // 暂停时长 = 复工时间 - 暂停时间
            OperateRecord pauseRecord = pauseRecords.get(i);
            OperateRecord resumeRecord = resumeRecords.get(i);
            pauseSeconds += DateUtil.between(pauseRecord.getOperateTime(), resumeRecord.getOperateTime(), DateUnit.SECOND);
        }

        return totalSeconds - pauseSeconds;
    }

    @Override
    public long calculateWorkTime(String taskOrderId) {
        List<OperateRecord> recordList = this.getOperateRecords(taskOrderId, OperateType.ALL);
        return this.calculateWorkTime(recordList);
    }

    @Override
    public Map<String, Long> batchCalculateWorkTime(List<String> taskOrderIds) {
        Map<String, Long> taskTimeMap = new HashMap<>();
        if (CollectionUtils.isEmpty(taskOrderIds)) {
            return taskTimeMap;
        }
        List<OperateRecord> recordList = operateRecordMapper.selectList(new LambdaQueryWrapper<OperateRecord>()
                .in(OperateRecord::getTaskOrderId, taskOrderIds));
        if (CollectionUtils.isEmpty(recordList)) {
            return taskTimeMap;
        }

        // 按照派工单ID、操作时间升序分组
        Map<String, List<OperateRecord>> taskRecordMap = recordList.stream()
                .collect(Collectors.groupingBy(
                        OperateRecord::getTaskOrderId,
                        Collectors.collectingAndThen(Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparing(OperateRecord::getOperateTime))
                                        .collect(Collectors.toList()))));

        taskRecordMap.forEach((key, value) -> taskTimeMap.put(key, this.calculateWorkTime(value)));
        return taskTimeMap;
    }

    @Override
    public long calculateWorkOrderWorkTime(String workOrderId) {
        List<String> taskOrderIds = taskOrderMapper.selectList(new LambdaQueryWrapper<TaskOrder>()
                        .select(TaskOrder::getId, TaskOrder::getWorkOrderId)
                        .eq(TaskOrder::getWorkOrderId, workOrderId)).stream()
                .map(TaskOrder::getId).collect(Collectors.toList());
        Map<String, Long> taskTimeMap = batchCalculateWorkTime(taskOrderIds);
        return taskTimeMap.values().stream().mapToLong(Long::longValue).sum();
    }
}
