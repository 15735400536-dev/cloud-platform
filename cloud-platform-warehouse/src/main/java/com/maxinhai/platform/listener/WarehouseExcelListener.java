package com.maxinhai.platform.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.maxinhai.platform.bo.WarehouseExcelBO;
import com.maxinhai.platform.mapper.model.WarehouseAreaMapper;
import com.maxinhai.platform.mapper.model.WarehouseLocationMapper;
import com.maxinhai.platform.mapper.model.WarehouseMapper;
import com.maxinhai.platform.mapper.model.WarehouseRackMapper;
import com.maxinhai.platform.po.model.Warehouse;
import com.maxinhai.platform.po.model.WarehouseArea;
import com.maxinhai.platform.po.model.WarehouseLocation;
import com.maxinhai.platform.po.model.WarehouseRack;
import com.maxinhai.platform.service.model.WarehouseAreaService;
import com.maxinhai.platform.service.model.WarehouseLocationService;
import com.maxinhai.platform.service.model.WarehouseRackService;
import com.maxinhai.platform.service.model.WarehouseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Excel导入监听器，用于处理读取到的Excel数据
 */
@Slf4j
@Component
public class WarehouseExcelListener implements ReadListener<WarehouseExcelBO> {

    @Lazy
    @Resource
    private WarehouseService warehouseService;
    @Resource
    private WarehouseAreaMapper areaMapper;
    @Resource
    private WarehouseRackMapper rackMapper;
    @Resource
    private WarehouseLocationMapper locationMapper;
    @Resource
    private WarehouseMapper warehouseMapper;
    @Resource
    private WarehouseAreaService areaService;
    @Resource
    private WarehouseRackService rackService;
    @Resource
    private WarehouseLocationService locationService;

    // 批量处理阈值，达到该数量就进行一次处理
    private static final int BATCH_COUNT = 100;

    // 存储读取到的数据
    private List<WarehouseExcelBO> dataList = new ArrayList<>(BATCH_COUNT);

    /**
     * 每读取一行数据就会调用该方法
     */
    @Override
    public void invoke(WarehouseExcelBO data, AnalysisContext context) {
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
        Map<String, Warehouse> warehouseMap = new HashMap<>(dataList.size());
        Map<String, List<WarehouseArea>> wmsAreaMap = new HashMap<>(dataList.size());
        Map<String, List<WarehouseRack>> wmsRackMap = new HashMap<>(dataList.size());
        Map<String, List<WarehouseLocation>> wmsLocationMap = new HashMap<>(dataList.size());
        for (WarehouseExcelBO warehouseExcelBO : dataList) {
            // 仓库
            if (!warehouseMap.containsKey(warehouseExcelBO.getWarehouseCode())) {
                warehouseMap.put(warehouseExcelBO.getWarehouseCode(), Warehouse.build(warehouseExcelBO));
            }
            // 库区
            List<WarehouseArea> areaList = wmsAreaMap.getOrDefault(warehouseExcelBO.getWarehouseCode(), new ArrayList<>());
            Set<String> areaSet = areaList.stream().map(WarehouseArea::getCode).collect(Collectors.toSet());
            if (!areaSet.contains(warehouseExcelBO.getAreaCode())) {
                areaList.add(WarehouseArea.build(warehouseExcelBO));
                wmsAreaMap.put(warehouseExcelBO.getWarehouseCode(), areaList);
            }
            // 货架
            List<WarehouseRack> rackList = wmsRackMap.getOrDefault(warehouseExcelBO.getAreaCode(), new ArrayList<>());
            Set<String> rackSet = rackList.stream().map(WarehouseRack::getCode).collect(Collectors.toSet());
            if (!rackSet.contains(warehouseExcelBO.getRackCode())) {
                rackList.add(WarehouseRack.build(warehouseExcelBO));
                wmsRackMap.put(warehouseExcelBO.getAreaCode(), rackList);
            }
            // 库位
            List<WarehouseLocation> locationList = wmsLocationMap.getOrDefault(warehouseExcelBO.getRackCode(), new ArrayList<>());
            Set<String> locationSet = locationList.stream().map(WarehouseLocation::getCode).collect(Collectors.toSet());
            if (!locationSet.contains(warehouseExcelBO.getLocationCode())) {
                locationList.add(WarehouseLocation.build(warehouseExcelBO));
                wmsLocationMap.put(warehouseExcelBO.getRackCode(), locationList);
            }
        }

        for (Warehouse warehouse : warehouseMap.values()) {
            // 仓库
            warehouseMapper.insert(warehouse);
            // 库区
            List<WarehouseArea> areaList = wmsAreaMap.get(warehouse.getCode());
            for (WarehouseArea area : areaList) {
                area.setWarehouseId(warehouse.getId());
                areaMapper.insert(area);

                // 货架
                List<WarehouseRack> rackList = wmsRackMap.get(area.getCode());
                for (WarehouseRack rack : rackList) {
                    rack.setWarehouseId(warehouse.getId());
                    rack.setAreaId(area.getId());
                    rackMapper.insert(rack);

                    // 库位
                    List<WarehouseLocation> locationList = wmsLocationMap.get(rack.getCode());
                    for (WarehouseLocation location : locationList) {
                        location.setWarehouseId(warehouse.getId());
                        location.setAreaId(area.getId());
                        location.setRackId(rack.getId());
                        locationMapper.insert(location);
                    }
                }
            }
        }

        log.info("数据保存完成！");
    }

}
