package com.maxinhai.platform.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.maxinhai.platform.excel.DataDictExcel;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.mapper.DataDictMapper;
import com.maxinhai.platform.mapper.DictTypeMapper;
import com.maxinhai.platform.po.DataDict;
import com.maxinhai.platform.po.DictType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Excel导入监听器，用于处理读取到的Excel数据
 */
@Slf4j
@Component
public class DataDictExcelListener implements ReadListener<DataDictExcel> {

    @Resource
    private DictTypeMapper dictTypeMapper;
    @Resource
    private DataDictMapper dataDictMapper;

    // 批量处理阈值，达到该数量就进行一次处理
    private static final int BATCH_COUNT = 100;

    // 存储读取到的数据
    private List<DataDictExcel> dataList = new ArrayList<>(BATCH_COUNT);

    /**
     * 每读取一行数据就会调用该方法
     */
    @Override
    public void invoke(DataDictExcel data, AnalysisContext context) {
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
        log.info("所有数据解析完成！");
    }

    /**
     * 保存数据到数据库
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveData() {
        log.info("开始保存 {} 条数据到数据库", dataList.size());
        // 没有内容不执行后面操作
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        // 保存数据
        Set<String> dictTypeSet = dataList.stream().map(DataDictExcel::getDictType).collect(Collectors.toSet());
        List<DictType> dictTypeList = dictTypeMapper.selectList(new LambdaQueryWrapper<DictType>()
                .select(DictType::getDictType)
                .in(DictType::getDictType, dictTypeSet));
        if (!dictTypeList.isEmpty()) {
            Set<String> repeatKeySet = dictTypeList.stream().map(DictType::getDictType).collect(Collectors.toSet());
            String msg = StringUtils.collectionToDelimitedString(repeatKeySet, ",");
            throw new BusinessException("字典类型【" + msg + "】已存在！");
        }

        Map<String, DictType> dictTypeMap = new HashMap<>();
        for (DataDictExcel dataDictExcel : dataList) {
            // 构建字典类型
            if (!dictTypeMap.containsKey(dataDictExcel.getDictType())) {
                DictType type = DataDictExcel.buildDictType(dataDictExcel);
                dictTypeMapper.insert(type);
                dictTypeMap.put(dataDictExcel.getDictKey(), type);
            }

            // 构建数据字典
            DataDict dataDict = DataDictExcel.buildDataDict(dataDictExcel);
            dataDictMapper.insert(dataDict);
        }

        log.info("数据保存完成！");
    }

}
