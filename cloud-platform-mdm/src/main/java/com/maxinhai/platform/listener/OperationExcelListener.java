package com.maxinhai.platform.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.maxinhai.platform.bo.OperationExcelBO;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.mapper.OperationMapper;
import com.maxinhai.platform.po.technology.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class OperationExcelListener implements ReadListener<OperationExcelBO> {

    @Resource
    private OperationMapper operationMapper;

    // 批量处理阈值，达到该数量就进行一次处理
    private static final int BATCH_COUNT = 100;

    // 存储读取到的数据
    private final List<OperationExcelBO> dataList = new ArrayList<>(BATCH_COUNT);

    /**
     * 每读取一行数据就会调用该方法
     */
    @Override
    public void invoke(OperationExcelBO data, AnalysisContext context) {
        log.info("读取到数据: {}", data);
        dataList.add(data);

        // 达到BATCH_COUNT，需要去存储一次数据库，防止数据量太大，内存溢出
        if (dataList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            dataList.clear();
        }
    }

    /**
     * 所有数据读取完成后调用该方法
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也被存储
        saveData();
        // 存储完成清理 list
        dataList.clear();
        log.info("所有数据解析完成！");
    }

    /**
     * 保存数据到数据库
     */
    private void saveData() {
        // 没有内容不执行后面操作
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }

        // 数据库存在导入物料，抛异常
        List<String> codeList = dataList.stream().map(OperationExcelBO::getCode).collect(Collectors.toList());
        List<String> existCodeList = operationMapper.selectList(new LambdaQueryWrapper<Operation>()
                        .select(Operation::getId, Operation::getCode, Operation::getName)
                        .in(Operation::getCode, codeList))
                .stream()
                .map(Operation::getCode)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(existCodeList)) {
            throw new BusinessException("工序编码【" + StringUtils.collectionToDelimitedString(existCodeList, ",") + "】已存在！");
        }

        log.info("开始保存 {} 条数据到数据库", dataList.size());
        // 保存数据
        for (OperationExcelBO excelBO : dataList) {
            Operation operation = new Operation();
            operation.setCode(excelBO.getCode());
            operation.setName(excelBO.getName());
            operation.setDescription(excelBO.getDescription());
            operation.setIsKey("是".equals(excelBO.getIsKey()) ? Boolean.TRUE : Boolean.FALSE);
            operation.setSetupTime(excelBO.getSetupTime());
            operation.setWorkTime(excelBO.getWorkTime());
            operation.setStatus(1);
            operationMapper.insert(operation);
        }
        log.info("数据保存完成！");
    }

}
