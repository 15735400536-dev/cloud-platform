package com.maxinhai.platform.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.maxinhai.platform.bo.EquipExcelBO;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.po.Equipment;
import com.maxinhai.platform.service.EquipmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @ClassName：EquipListener
 * @Author: XinHai.Ma
 * @Date: 2025/8/28 16:23
 * @Description: Excel导入监听器，用于处理读取到的Excel数据
 */
@Slf4j
@Component
public class EquipListener implements ReadListener<EquipExcelBO> {

    @Lazy
    @Resource
    private EquipmentService equipmentService;

    // 批量处理阈值，达到该数量就进行一次处理
    private static final int BATCH_COUNT = 100;

    // 存储读取到的数据
    private final List<EquipExcelBO> dataList = new ArrayList<>(BATCH_COUNT);

    /**
     * 每读取一行数据就会调用该方法
     */
    @Override
    public void invoke(EquipExcelBO data, AnalysisContext context) {
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
        log.info("开始保存 {} 条数据到数据库", dataList.size());
        // 实际项目中这里会调用Service层将数据保存到数据库
        Set<String> equipCodeSet = dataList.stream().map(EquipExcelBO::getEquipCode).collect(Collectors.toSet());
        List<Equipment> equipList = equipmentService.list(new LambdaQueryWrapper<Equipment>()
                .select(Equipment::getId, Equipment::getEquipCode)
                .in(Equipment::getEquipCode, equipCodeSet));
        if (!equipList.isEmpty()) {
            Set<String> repeatCodeSet = equipList.stream().map(Equipment::getEquipCode).collect(Collectors.toSet());
            String msg = StringUtils.collectionToDelimitedString(repeatCodeSet, ",");
            throw new BusinessException("设备【" + msg + "】已存在！");
        }
        // 保存数据
        List<Equipment> equipmentList = dataList.stream()
                .map(EquipExcelBO::build)
                .collect(Collectors.toList());
        equipmentService.saveBatch(equipmentList);
        log.info("数据保存完成！");
    }

}
