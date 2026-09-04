package com.maxinhai.platform.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.maxinhai.platform.bo.MaterialTypeExcelBO;
import com.maxinhai.platform.enums.DelFlag;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.mapper.MaterialTypeMapper;
import com.maxinhai.platform.po.MaterialType;
import com.maxinhai.platform.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MaterialTypeExcelListener implements ReadListener<MaterialTypeExcelBO> {

    @Resource
    private MaterialTypeMapper materialTypeMapper;
    @Resource
    private JwtUtils jwtUtils;

    // 批量处理阈值，达到该数量就进行一次处理
    private static final int BATCH_COUNT = 100;

    // 存储读取到的数据
    private final List<MaterialTypeExcelBO> dataList = new ArrayList<>(BATCH_COUNT);

    /**
     * 每读取一行数据就会调用该方法
     */
    @Override
    public void invoke(MaterialTypeExcelBO data, AnalysisContext context) {
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
        log.info("开始保存 {} 条数据到数据库", dataList.size());
        // 实际项目中这里会调用Service层将数据保存到数据库
        List<String> codeList = dataList.stream().map(MaterialTypeExcelBO::getCode).collect(Collectors.toList());
        // 没有内容不执行后面操作
        if (CollectionUtils.isEmpty(codeList)) {
            return;
        }
        // 数据库存在导入物料，抛异常
        List<String> existCodeList = materialTypeMapper.selectList(new LambdaQueryWrapper<MaterialType>()
                        .select(MaterialType::getId, MaterialType::getName, MaterialType::getCode)
                        .in(MaterialType::getCode, codeList)).stream()
                .map(MaterialType::getCode)
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(existCodeList)) {
            throw new BusinessException("物料类型编码【" + StringUtils.collectionToDelimitedString(existCodeList, ",") + "】已存在！");
        }
        // 保存数据
        List<MaterialType> leve1List =  new ArrayList<>();
        List<MaterialType> leve2List =  new ArrayList<>();

        for (MaterialTypeExcelBO data : dataList) {
            MaterialType materialType = new MaterialType();
            materialType.setCode(data.getCode());
            materialType.setName(data.getName());
            materialType.setDescription(data.getDescription());
            materialType.setDelFlag(DelFlag.NORMAL.getKey());
            Date now = new Date();
            materialType.setCreateBy(jwtUtils.getUserIdFromToken(jwtUtils.getToken()));
            materialType.setCreateTime(now);
            materialType.setUpdateBy(jwtUtils.getUserIdFromToken(jwtUtils.getToken()));
            materialType.setUpdateTime(now);
            if(StringUtils.hasText(data.getParentCode()) && StringUtils.hasText(data.getParentName())) {
                // 二级分类，设置上级分类
                materialType.setParentId(data.getParentCode());
                leve2List.add(materialType);
            } else {
                // 一级分类
                materialType.setParentId("0");
                leve1List.add(materialType);
            }
        }

        // 保存一级分类
        if(!CollectionUtils.isEmpty(leve1List)) {
            materialTypeMapper.batchInsert(leve1List);
        }
        if(!CollectionUtils.isEmpty(leve2List)) {
            // 查询一级分类
            Map<String, String> parentTypeMap = materialTypeMapper.selectList(new LambdaQueryWrapper<MaterialType>()
                    .select(MaterialType::getId, MaterialType::getName, MaterialType::getCode)
                    .eq(MaterialType::getParentId, "0"))
                    .stream()
                    .collect(Collectors.toMap(MaterialType::getCode, MaterialType::getId));
            // 二级分类赋值一级分类ID
            for (MaterialType materialType : leve2List) {
                materialType.setParentId(parentTypeMap.get(materialType.getParentId()));
            }
            materialTypeMapper.batchInsert(leve2List);
        }
        log.info("数据保存完成！");
    }

}
