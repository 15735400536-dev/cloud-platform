package com.maxinhai.platform.listener;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.maxinhai.platform.po.Material;
import com.maxinhai.platform.mapper.MaterialMapper;
import com.maxinhai.platform.mapper.inventory.InventoryAdjustmentMapper;
import com.maxinhai.platform.mapper.inventory.InventoryMapper;
import com.maxinhai.platform.mapper.model.WarehouseAreaMapper;
import com.maxinhai.platform.mapper.model.WarehouseLocationMapper;
import com.maxinhai.platform.mapper.model.WarehouseMapper;
import com.maxinhai.platform.mapper.model.WarehouseRackMapper;
import com.maxinhai.platform.mapper.order.IssueOrderMapper;
import com.maxinhai.platform.mapper.order.ReceiptOrderMapper;
import com.maxinhai.platform.mapper.order.TransferOrderMapper;
import com.maxinhai.platform.mapper.stocktaking.StocktakingMapper;
import com.maxinhai.platform.po.inventory.Inventory;
import com.maxinhai.platform.po.inventory.InventoryAdjustment;
import com.maxinhai.platform.po.inventory.InventoryAdjustmentDetail;
import com.maxinhai.platform.po.model.Warehouse;
import com.maxinhai.platform.po.model.WarehouseArea;
import com.maxinhai.platform.po.model.WarehouseLocation;
import com.maxinhai.platform.po.model.WarehouseRack;
import com.maxinhai.platform.po.order.*;
import com.maxinhai.platform.po.stocktaking.Stocktaking;
import com.maxinhai.platform.po.stocktaking.StocktakingDetail;
import com.maxinhai.platform.service.inventory.InventoryAdjustmentDetailService;
import com.maxinhai.platform.service.order.IssueOrderDetailService;
import com.maxinhai.platform.service.order.ReceiptOrderDetailService;
import com.maxinhai.platform.service.order.TransferOrderDetailService;
import com.maxinhai.platform.service.stocktaking.StocktakingDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InitDataListener implements CommandLineRunner {

    @Resource
    private WarehouseMapper warehouseMapper;
    @Resource
    private WarehouseAreaMapper warehouseAreaMapper;
    @Resource
    private WarehouseRackMapper warehouseRackMapper;
    @Resource
    private WarehouseLocationMapper warehouseLocationMapper;
    @Resource
    private ReceiptOrderMapper receiptOrderMapper;
    @Resource
    private IssueOrderMapper issueOrderMapper;
    @Resource
    private TransferOrderMapper transferOrderMapper;
    @Resource
    private MaterialMapper materialMapper;
    @Resource
    private IssueOrderDetailService issueOrderDetailService;
    @Resource
    private ReceiptOrderDetailService receiptOrderDetailService;
    @Resource
    private TransferOrderDetailService transferOrderDetailService;
    @Resource
    private StocktakingMapper stocktakingMapper;
    @Resource
    private StocktakingDetailService stocktakingDetailService;
    @Resource
    private InventoryMapper inventoryMapper;
    @Resource
    private InventoryAdjustmentMapper inventoryAdjustmentMapper;
    @Resource
    private InventoryAdjustmentDetailService inventoryAdjustmentDetailService;

    @Override
    public void run(String... args) throws Exception {
        createOrder();

        // 生成指定长度的随机字符串（包含大小写字母、数字）
        String serialNo = RandomUtil.randomString(6);
        Warehouse warehouse = new Warehouse();
        warehouse.setCode("material(" + serialNo + ")");
        warehouse.setName("仓库(" + serialNo + ")");
        warehouse.setStatus(1);
        warehouse.setAddress("山西省晋城市阳城县北留镇");
        warehouse.setContactPerson("马鑫海");
        warehouse.setContactPhone("15735400536");
        warehouse.setRemark("备注");
        warehouseMapper.insert(warehouse);

        String[] areaArr = new String[]{"A", "B", "C", "D"};
        for (String areaStr : areaArr) {
            WarehouseArea area = new WarehouseArea();
            area.setWarehouseId(warehouse.getId());
            area.setCode(areaStr);
            area.setName("库区" +  areaStr);
            area.setStatus(1);
            area.setRemark("备注");
            warehouseAreaMapper.insert(area);

            String[] rackArr = new String[]{"1", "2", "3", "4"};
            for (String rackStr : rackArr) {
                WarehouseRack rack = new WarehouseRack();
                rack.setWarehouseId(warehouse.getId());
                rack.setAreaId(area.getId());
                rack.setCode(rackStr);
                rack.setName("货位" + rackStr);
                rack.setStatus(1);
                warehouseRackMapper.insert(rack);

                for (int i = 1; i <= 10; i++) {
                    WarehouseLocation location = new WarehouseLocation();
                    location.setWarehouseId(warehouse.getId());
                    location.setAreaId(area.getId());
                    location.setRackId(rack.getId());
                    location.setCode(String.valueOf(i));
                    location.setName("货位" + i);
                    location.setLocationType(1);
                    location.setStatus(1);
                    warehouseLocationMapper.insert(location);
                }
            }
        }
    }

    public void createOrder() {
        // 查询仓库信息
        Warehouse warehouse = warehouseMapper.selectOne(new LambdaQueryWrapper<Warehouse>()
                .select(Warehouse::getId, Warehouse::getCode, Warehouse::getName)
                .orderByDesc(Warehouse::getCreateTime)
                .last("limit 1"));
        // 查询库位消息
        List<WarehouseLocation> locationList = warehouseLocationMapper.selectList(new LambdaQueryWrapper<WarehouseLocation>()
                .eq(WarehouseLocation::getWarehouseId, warehouse.getId())
                .orderByDesc(WarehouseLocation::getCreateTime));
        // 查询物料信息
        List<Material> materialList = materialMapper.selectList(new LambdaQueryWrapper<Material>()
                .select(Material::getId, Material::getCode, Material::getName)
                .orderByDesc(Material::getCreateTime));

        // 入库单
        String issueOrderNo = "ISSUE_" + RandomUtil.randomString(6);
        IssueOrder issueOrder = new IssueOrder();
        issueOrder.setOrderNo(issueOrderNo);
        issueOrder.setWarehouseId(warehouse.getId());
        issueOrder.setOrderType(2);
        issueOrder.setSourceOrderNo(issueOrderNo);
        issueOrder.setStatus(0);
        issueOrder.setIssueTime(new Date());
        issueOrder.setTotalQty(BigDecimal.ONE);
        issueOrder.setTotalAmount(BigDecimal.ONE);
        issueOrder.setOperator("马鑫海");
        issueOrder.setRemark("备注");
        issueOrderMapper.insert(issueOrder);
        List<IssueOrderDetail> orderDetailList = materialList.stream().map(material -> {
            IssueOrderDetail issueOrderDetail = new IssueOrderDetail();
            issueOrderDetail.setIssueOrderId(issueOrder.getId());
            issueOrderDetail.setMaterialId(material.getId());
            issueOrderDetail.setLocationId(locationList.get(0).getId());
            issueOrderDetail.setBatchNo(issueOrderNo);
            issueOrderDetail.setPlanQty(BigDecimal.ONE);
            issueOrderDetail.setActualQty(BigDecimal.ONE);
            issueOrderDetail.setUnitCost(BigDecimal.ONE);
            issueOrderDetail.setAmount(BigDecimal.ONE);
            return issueOrderDetail;
        }).collect(Collectors.toList());
        issueOrderDetailService.saveBatch(orderDetailList);

        // 出库单
        String receiptOrderNo = "RECEIPT_" + RandomUtil.randomString(6);
        ReceiptOrder receiptOrder = new ReceiptOrder();
        receiptOrder.setOrderNo(receiptOrderNo);
        receiptOrder.setWarehouseId(warehouse.getId());
        receiptOrder.setOrderType(2);
        receiptOrder.setSourceOrderNo(receiptOrderNo);
        receiptOrder.setStatus(0);
        receiptOrder.setReceiptTime(new Date());
        receiptOrder.setTotalQty(BigDecimal.ONE);
        receiptOrder.setTotalAmount(BigDecimal.ONE);
        receiptOrder.setOperator("马鑫海");
        receiptOrder.setRemark("备注");
        receiptOrderMapper.insert(receiptOrder);
        List<ReceiptOrderDetail> orderDetailList1 = materialList.stream().map(material -> {
            ReceiptOrderDetail orderDetail = new ReceiptOrderDetail();
            orderDetail.setReceiptOrderId(receiptOrder.getId());
            orderDetail.setMaterialId(material.getId());
            orderDetail.setLocationId(locationList.get(0).getId());
            orderDetail.setBatchNo(receiptOrder.getOrderNo());
            orderDetail.setPlanQty(BigDecimal.ONE);
            orderDetail.setActualQty(BigDecimal.ONE);
            orderDetail.setUnitCost(BigDecimal.ONE);
            orderDetail.setAmount(BigDecimal.ONE);
            orderDetail.setProductionDate(new Date());
            orderDetail.setExpiryDate(new Date());
            return orderDetail;
        }).collect(Collectors.toList());
        receiptOrderDetailService.saveBatch(orderDetailList1);

        // 移库单
        String transferNo = "TRANSFER_" + RandomUtil.randomString(6);
        TransferOrder transferOrder = new TransferOrder();
        transferOrder.setTransferNo(transferNo);
        transferOrder.setSourceWarehouseId(warehouse.getId());
        transferOrder.setTargetWarehouseId(warehouse.getId());
        transferOrder.setStatus(0);
        transferOrder.setTransferTime(new Date());
        transferOrder.setTotalQty(BigDecimal.ONE);
        transferOrder.setOperator("马鑫海");
        transferOrder.setRemark("备注");
        transferOrderMapper.insert(transferOrder);
        List<TransferOrderDetail> orderDetailList2 = materialList.stream().map(material -> {
            TransferOrderDetail transferOrderDetail = new TransferOrderDetail();
            transferOrderDetail.setTransferOrderId(transferOrder.getId());
            transferOrderDetail.setMaterialId(material.getId());
            transferOrderDetail.setSourceLocationId(locationList.get(0).getId());
            transferOrderDetail.setTargetLocationId(locationList.get(1).getId());
            transferOrderDetail.setBatchNo(transferNo);
            transferOrderDetail.setPlanQty(BigDecimal.ONE);
            transferOrderDetail.setActualQty(BigDecimal.ONE);
            return transferOrderDetail;
        }).collect(Collectors.toList());
        transferOrderDetailService.saveBatch(orderDetailList2);

        // 盘点单
        String stocktakingNo = "STOCKTAKING" + RandomUtil.randomString(6);
        Stocktaking stocktaking = new Stocktaking();
        stocktaking.setStocktakingNo(stocktakingNo);
        stocktaking.setWarehouseId(warehouse.getId());
        stocktaking.setAreaId(null);
        stocktaking.setStatus(0);
        stocktaking.setStartTime(new Date());
        stocktaking.setEndTime(new Date());
        stocktaking.setOperator("马鑫海");
        stocktaking.setRemark("备注");
        stocktakingMapper.insert(stocktaking);
        List<StocktakingDetail> stocktakingDetailList = materialList.stream().map(material -> {
            StocktakingDetail stocktakingDetail = new StocktakingDetail();
            stocktakingDetail.setStocktakingId(stocktaking.getId());
            stocktakingDetail.setMaterialId(material.getId());
            stocktakingDetail.setLocationId(locationList.get(0).getId());
            stocktakingDetail.setBatchNo(stocktakingNo);
            stocktakingDetail.setSystemQty(BigDecimal.ONE);
            stocktakingDetail.setActualQty(BigDecimal.ONE);
            stocktakingDetail.setDifferenceQty(BigDecimal.ZERO);
            stocktakingDetail.setRemark("备注");
            return stocktakingDetail;
        }).collect(Collectors.toList());
        stocktakingDetailService.saveBatch(stocktakingDetailList);

        // 库存表
        Inventory inventory = new Inventory();
        inventory.setWarehouseId(warehouse.getId());
        inventory.setAreaId(locationList.get(0).getAreaId());
        inventory.setRackId(locationList.get(0).getRackId());
        inventory.setLocationId(locationList.get(0).getId());
        inventory.setMaterialId(materialList.get(0).getId());
        inventory.setBatchNo(materialList.get(0).getCode());
        inventory.setQty(BigDecimal.ONE);
        inventory.setLockedQty(BigDecimal.ZERO);
        inventory.setAvailableQty(BigDecimal.ONE);
        inventory.setProductionDate(new Date());
        inventory.setExpiryDate(new Date());
        inventoryMapper.insert(inventory);

        // 库存调整单
        String adjustmentNo = "ADJUSTMENT" + RandomUtil.randomString(6);
        InventoryAdjustment inventoryAdjustment = new InventoryAdjustment();
        inventoryAdjustment.setAdjustmentNo(adjustmentNo);
        inventoryAdjustment.setWarehouseId(warehouse.getId());
        inventoryAdjustment.setAdjustmentType(1);
        inventoryAdjustment.setStatus(0);
        inventoryAdjustment.setAdjustmentTime(new Date());
        inventoryAdjustment.setTotalQty(BigDecimal.ONE);
        inventoryAdjustment.setOperator("马鑫海");
        inventoryAdjustment.setRemark("备注");
        inventoryAdjustmentMapper.insert(inventoryAdjustment);

        List<InventoryAdjustmentDetail> adjustmentDetailList = materialList.stream().map(material -> {
            InventoryAdjustmentDetail inventoryAdjustmentDetail = new InventoryAdjustmentDetail();
            inventoryAdjustmentDetail.setAdjustmentId(inventoryAdjustment.getId());
            inventoryAdjustmentDetail.setMaterialId(material.getId());
            inventoryAdjustmentDetail.setLocationId(locationList.get(0).getId());
            inventoryAdjustmentDetail.setBatchNo(adjustmentNo);
            inventoryAdjustmentDetail.setBeforeQty(BigDecimal.ONE);
            inventoryAdjustmentDetail.setAdjustmentQty(BigDecimal.ZERO);
            inventoryAdjustmentDetail.setAfterQty(BigDecimal.ONE);
            inventoryAdjustmentDetail.setReason("原因");
            return inventoryAdjustmentDetail;
        }).collect(Collectors.toList());
        inventoryAdjustmentDetailService.saveBatch(adjustmentDetailList);
    }
}
