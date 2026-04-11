package com.maxinhai.platform.scheduled;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.maxinhai.platform.enums.OperateType;
import com.maxinhai.platform.enums.WarehouseAreaType;
import com.maxinhai.platform.enums.WarehouseType;
import com.maxinhai.platform.mapper.MaterialMapper;
import com.maxinhai.platform.mapper.inventory.InventoryFlowMapper;
import com.maxinhai.platform.mapper.inventory.InventoryMapper;
import com.maxinhai.platform.mapper.model.WarehouseAreaMapper;
import com.maxinhai.platform.mapper.model.WarehouseLocationMapper;
import com.maxinhai.platform.mapper.model.WarehouseMapper;
import com.maxinhai.platform.mapper.model.WarehouseRackMapper;
import com.maxinhai.platform.po.Material;
import com.maxinhai.platform.po.inventory.Inventory;
import com.maxinhai.platform.po.inventory.InventoryFlow;
import com.maxinhai.platform.po.model.Warehouse;
import com.maxinhai.platform.po.model.WarehouseArea;
import com.maxinhai.platform.po.model.WarehouseLocation;
import com.maxinhai.platform.po.model.WarehouseRack;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class WmsScheduled {

    private final WarehouseMapper warehouseMapper;
    private final WarehouseAreaMapper warehouseAreaMapper;
    private final WarehouseRackMapper warehouseRackMapper;
    private final WarehouseLocationMapper warehouseLocationMapper;

    private final MaterialMapper materialMapper;
    private final InventoryMapper inventoryMapper;
    private final InventoryFlowMapper inventoryFlowMapper;

    private static final WarehouseType[] WAREHOUSE_TYPES = {
            WarehouseType.PT,
            WarehouseType.LS,
            WarehouseType.YL,
            WarehouseType.CP,
            WarehouseType.BJ,
            WarehouseType.WH
    };

    private static final WarehouseAreaType[] WAREHOUSE_AREA_TYPE = {
            WarehouseAreaType.QC,
            WarehouseAreaType.OK,
            WarehouseAreaType.NG,
            WarehouseAreaType.RT,
            WarehouseAreaType.RP,
            WarehouseAreaType.BF,
            WarehouseAreaType.LS
    };

    /**
     * 获取仓库编码最新序列号
     *
     * @param warehouseType 仓库类型
     * @return 最新序列号
     */
    public Integer getSeq(WarehouseType warehouseType) {
        Warehouse warehouse = warehouseMapper.selectOne(new LambdaQueryWrapper<Warehouse>()
                .select(Warehouse::getCode, Warehouse::getType)
                .eq(Warehouse::getType, warehouseType)
                .orderByDesc(Warehouse::getCode)
                .last("limit 1"));
        return Objects.isNull(warehouse) ? Integer.parseInt("001")
                : Integer.parseInt(warehouse.getCode().replace(warehouseType.getCode(), "")) + 1;
    }

    /**
     * 获取库区编码最新序列号
     *
     * @param areaType 库区类型
     * @return 最新序列号
     */
    public Integer getSeq(String warehouseId, WarehouseAreaType areaType) {
        WarehouseArea warehouseArea = warehouseAreaMapper.selectOne(new LambdaQueryWrapper<WarehouseArea>()
                .select(WarehouseArea::getCode, WarehouseArea::getType)
                .eq(WarehouseArea::getWarehouseId, warehouseId)
                .eq(WarehouseArea::getType, areaType)
                .orderByDesc(WarehouseArea::getCode)
                .last("limit 1"));
        return Objects.isNull(warehouseArea) ? Integer.parseInt("001")
                : Integer.parseInt(warehouseArea.getCode().replace(areaType.getCode(), "")) + 1;
    }

//    @Scheduled(cron = "0 0 1 * * ?")
    @Scheduled(cron = "0 0 * * * ?")
//    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void initModel() {
        List<Material> materialList = materialMapper.selectList(new LambdaQueryWrapper<Material>()
                .select(Material::getId, Material::getCode, Material::getName));

        // 创建仓库
        for (WarehouseType warehouseType : WAREHOUSE_TYPES) {
            Integer seq = getSeq(warehouseType);

            Warehouse warehouse = new Warehouse();
            warehouse.setCode(warehouseType.getCode() + seq);
            warehouse.setName(warehouseType.getName() + seq);
            warehouse.setType(warehouseType.getCode());
            warehouse.setStatus(1);
            warehouse.setAddress("山西省晋城市阳城县北留镇小沟村南山西路2号");
            warehouse.setContactPerson("马鑫海");
            warehouse.setContactPhone("15735400536");
            warehouse.setRemark(warehouseType.getName());
            warehouseMapper.insert(warehouse);

            // 创建库区
            for (WarehouseAreaType areaType : WAREHOUSE_AREA_TYPE) {
                seq = getSeq(warehouse.getId(), areaType);

                WarehouseArea area = new WarehouseArea();
                area.setWarehouseId(warehouse.getId());
                area.setCode(warehouse.getCode() + areaType.getCode() + seq);
                area.setName(areaType.getName() + seq);
                area.setType(areaType.getCode());
                area.setStatus(1);
                area.setRemark(areaType.getName());
                warehouseAreaMapper.insert(area);

                // 创建货架
                for (int i = 1; i <= 24; i++) {
                    WarehouseRack rack = new WarehouseRack();
                    rack.setWarehouseId(warehouse.getId());
                    rack.setAreaId(area.getId());
                    rack.setCode(area.getCode() + String.format("%03d", i));
                    rack.setName("货架" + String.format("%03d", i));
                    rack.setStatus(1);
                    warehouseRackMapper.insert(rack);

                    // 创建库位
                    for (int j = 1; j <= 60; j++) {
                        WarehouseLocation location = new WarehouseLocation();
                        location.setWarehouseId(warehouse.getId());
                        location.setAreaId(area.getId());
                        location.setRackId(rack.getId());
                        location.setCode(rack.getCode() + String.format("%03d", j));
                        location.setName("库位" + String.format("%03d", j));
                        location.setLocationType(1);
                        location.setStatus(1);
                        warehouseLocationMapper.insert(location);

                        for (Material material : materialList) {
                            // 创建库存
                            Inventory inventory = new Inventory();
                            inventory.setWarehouseId(location.getWarehouseId());
                            inventory.setAreaId(location.getAreaId());
                            inventory.setRackId(location.getRackId());
                            inventory.setLocationId(location.getId());
                            inventory.setMaterialId(material.getId());
                            inventory.setBatchNo(DateUtil.format(new Date(), "yyyyMMddHH"));
                            inventory.setQty(BigDecimal.ONE);
                            inventory.setLockedQty(BigDecimal.ZERO);
                            inventory.setAvailableQty(BigDecimal.ONE);
                            inventory.setProductionDate(new Date());
                            inventory.setExpiryDate(new Date());
                            inventoryMapper.insert(inventory);

                            // 创建库存流水
                            InventoryFlow inventoryFlow = new InventoryFlow();
                            inventoryFlow.setOrderCode("IO" + DateUtil.format(new Date(), "yyyyMMddHH"));
                            inventoryFlow.setOrderType(1);
                            inventoryFlow.setWarehouseId(location.getWarehouseId());
                            inventoryFlow.setAreaId(location.getAreaId());
                            inventoryFlow.setRackId(location.getRackId());
                            inventoryFlow.setLocationId(location.getId());
                            inventoryFlow.setMaterialId(material.getId());
                            inventoryFlow.setBatchNo(DateUtil.format(new Date(), "yyyyMMddHH"));
                            inventoryFlow.setBeforeQty(BigDecimal.ZERO);
                            inventoryFlow.setAfterQt(BigDecimal.ONE);
                            inventoryFlow.setChangeQty(BigDecimal.ONE);
                            inventoryFlow.setOperateType(OperateType.ISSUE);
                            inventoryFlow.setOperateTime(new Date());
                            inventoryFlowMapper.insert(inventoryFlow);
                        }
                    }
                }
            }
        }
    }

}
