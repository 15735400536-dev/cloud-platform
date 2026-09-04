package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.OperateRecordQueryDTO;
import com.maxinhai.platform.enums.OperateType;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.feign.SystemFeignClient;
import com.maxinhai.platform.mapper.OperateRecordMapper;
import com.maxinhai.platform.mapper.TaskOrderMapper;
import com.maxinhai.platform.po.OperateRecord;
import com.maxinhai.platform.po.TaskOrder;
import com.maxinhai.platform.service.OperateRecordService;
import com.maxinhai.platform.service.OperatorService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.JwtUtils;
import com.maxinhai.platform.vo.OperateRecordVO;
import com.maxinhai.platform.vo.TaskOrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OperateRecordServiceImpl extends ServiceImpl<OperateRecordMapper, OperateRecord> implements OperateRecordService {

    @Resource
    private OperateRecordMapper operateRecordMapper;
    @Resource
    private TaskOrderMapper taskOrderMapper;
    @Resource
    private SystemFeignClient systemFeignClient;
    @Resource
    private OperatorService operatorService;
    @Resource
    private JwtUtils jwtUtils;

    @Override
    public Page<OperateRecordVO> searchByPage(OperateRecordQueryDTO param) {
        return operateRecordMapper.selectJoinPage(param.getPage(), OperateRecordVO.class,
                new MPJLambdaWrapper<OperateRecord>()
                        .innerJoin(TaskOrder.class, TaskOrder::getId, OperateRecord::getTaskOrderId)
                        .eq(StrUtil.isNotBlank(param.getTaskOrderId()), OperateRecord::getTaskOrderId, param.getTaskOrderId())
                        .eq(Objects.nonNull(param.getOperateType()), OperateRecord::getOperateType, param.getOperateType())
                        .orderByDesc(OperateRecord::getCreateTime));
    }

    @Override
    public Page<OperateRecordVO> searchByPageEx(OperateRecordQueryDTO param) {
        // 1. 查询分页数据（PO）
        Page<OperateRecord> operateRecordPage = operateRecordMapper.selectPage(
                new Page<>(param.getCurrent(), param.getSize()),
                new LambdaQueryWrapper<OperateRecord>()
                        .eq(StrUtil.isNotBlank(param.getTaskOrderId()), OperateRecord::getTaskOrderId, param.getTaskOrderId())
                        .eq(Objects.nonNull(param.getOperateType()), OperateRecord::getOperateType, param.getOperateType())
                        .orderByDesc(OperateRecord::getCreateTime)
        );

        // 2. 获取 PO 列表
        List<OperateRecord> records = operateRecordPage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            // 空数据直接返回
            Page<OperateRecordVO> emptyPage = new Page<>();
            emptyPage.setTotal(operateRecordPage.getTotal());
            emptyPage.setSize(operateRecordPage.getSize());
            emptyPage.setCurrent(operateRecordPage.getCurrent());
            emptyPage.setRecords(Collections.emptyList());
            return emptyPage;
        }

        // 3. 提取 taskOrderIds，批量查询详情
        List<String> taskOrderIds = records.stream()
                .map(OperateRecord::getTaskOrderId)
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.toList());

        Map<String, TaskOrderVO> taskOrderMap = new HashMap<>();
        if (CollUtil.isNotEmpty(taskOrderIds)) {
            taskOrderMap = taskOrderMapper.findTaskDetailByIds(taskOrderIds).stream()
                    .collect(Collectors.toMap(TaskOrderVO::getId, Function.identity()));
        }

        // 4. PO 转 VO，并赋值关联数据（关键修复！）
        Map<String, String> operator = operatorService.getOperator();
        Map<String, TaskOrderVO> finalTaskOrderMap = taskOrderMap;
        List<OperateRecordVO> voList = records.stream().map(po -> {
            OperateRecordVO vo = new OperateRecordVO();
            // PO 拷贝到 VO
            BeanUtils.copyProperties(po, vo);

            // 赋值工单信息
            TaskOrderVO taskOrderVO = finalTaskOrderMap.get(po.getTaskOrderId());
            if (taskOrderVO != null) {
                BeanUtils.copyProperties(taskOrderVO, vo);
            }

            // 复制操作人
            vo.setOperator(operator.getOrDefault(po.getCreateBy(), "anonymous"));
            return vo;
        }).collect(Collectors.toList());

        // 5. 组装最终分页
        Page<OperateRecordVO> pageResult = new Page<>();
        pageResult.setCurrent(operateRecordPage.getCurrent());
        pageResult.setSize(operateRecordPage.getSize());
        pageResult.setTotal(operateRecordPage.getTotal());
        pageResult.setRecords(voList);

        return pageResult;
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
    public OperateRecord generateRecord(OperateType operateType, String taskOrderId) {
        OperateRecord record = new OperateRecord();
        record.setTaskOrderId(taskOrderId);
        record.setOperateType(operateType);
        record.setOperateTime(new Date());
        record.setDelFlag(0);
        record.setCreateBy(jwtUtils.getUserIdFromToken(jwtUtils.getToken()));
        record.setCreateTime(new Date());
        record.setUpdateBy(jwtUtils.getUserIdFromToken(jwtUtils.getToken()));
        record.setUpdateTime(new Date());
        return record;
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
        if (CollectionUtils.isEmpty(startRecords)) {
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
        if (CollectionUtils.isEmpty(reportRecords)) {
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

    @Override
    public List<OperateRecordVO> getOperateRecords(String taskOrderId) {
        AjaxResult<Map<String, String>> feignResult = systemFeignClient.getUserMap();
        if (feignResult.getCode() != HttpStatus.OK.value()) {
            throw new BusinessException(feignResult.getMsg());
        }
        Map<String, String> userMap = feignResult.getData();
        List<OperateRecord> recordList = getOperateRecords(taskOrderId, OperateType.ALL);
        return recordList.stream().map(record -> {
            OperateRecordVO recordVO = BeanUtil.copyProperties(record, OperateRecordVO.class);
            recordVO.setOperator(userMap.getOrDefault(recordVO.getCreateBy(), "匿名用户"));
            recordVO.setCreator(userMap.getOrDefault(recordVO.getCreateBy(), "匿名用户"));
            return recordVO;
        }).collect(Collectors.toList());
    }
}
