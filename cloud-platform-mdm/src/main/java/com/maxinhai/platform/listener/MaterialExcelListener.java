package com.maxinhai.platform.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.maxinhai.platform.bo.MaterialExcelBO;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.po.Material;
import com.maxinhai.platform.service.MaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Excel导入监听器，用于处理读取到的Excel数据
 */
@Slf4j
@Component
public class MaterialExcelListener implements ReadListener<MaterialExcelBO> {

    @Lazy
    @Resource
    private MaterialService materialService;

    // 批量处理阈值，达到该数量就进行一次处理
    private static final int BATCH_COUNT = 100;

    // 存储读取到的数据
    private List<MaterialExcelBO> dataList = new ArrayList<>(BATCH_COUNT);

    /**
     * 每读取一行数据就会调用该方法
     */
    @Override
    public void invoke(MaterialExcelBO data, AnalysisContext context) {
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
    private void saveData() {
        log.info("开始保存 {} 条数据到数据库", dataList.size());
        // 实际项目中这里会调用Service层将数据保存到数据库
        List<String> materialCodeList = dataList.stream().map(MaterialExcelBO::getCode).collect(Collectors.toList());
        // 没有内容不执行后面操作
        if (CollectionUtils.isEmpty(materialCodeList)) {
            return;
        }
        // 数据库存在导入物料，抛异常
        List<String> existCodeList = materialService.list(new LambdaQueryWrapper<Material>()
                        .select(Material::getId, Material::getName, Material::getCode)
                        .in(Material::getCode, materialCodeList)).stream()
                .map(Material::getCode)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(existCodeList)) {
            throw new BusinessException("物料编码【" + StringUtils.collectionToDelimitedString(materialCodeList, ",") + "】已存在！");
        }
        // 保存数据
        List<Material> materialList = dataList.stream().map(data -> {
            Material material = new Material();
            material.setCode(data.getCode());
            material.setName(data.getName());
            material.setUnit(data.getUnit());
            material.setSpecs(data.getSpecs());
            material.setDrawingNo(data.getDrawingNo());
            material.setMaterial(data.getMaterial());
            material.setRemark(data.getRemark());
            return material;
        }).collect(Collectors.toList());
        materialService.saveBatch(materialList);
        log.info("数据保存完成！");
    }

}
