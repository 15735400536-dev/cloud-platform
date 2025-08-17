package com.maxinhai.platform.listener;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.maxinhai.platform.mapper.*;
import com.maxinhai.platform.po.*;
import com.maxinhai.platform.po.technology.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
//@Component
public class InitDataListener implements CommandLineRunner {

    @Resource
    private ProductMapper productMapper;
    @Resource
    private RoutingMapper routingMapper;
    @Resource
    private OperationMapper operationMapper;
    @Resource
    private RoutingOperationRelMapper routingOperationRelMapper;
    @Resource
    private BomMapper bomMapper;
    @Resource
    private BomDetailMapper bomDetailMapper;
    @Resource
    private MaterialMapper materialMapper;

    @Override
    public void run(String... args) throws Exception {
        // 查询最新产品
        Product product = productMapper.selectOne(new LambdaQueryWrapper<Product>()
                .select(Product::getId, Product::getCode, Product::getName)
                .orderByDesc(Product::getCreateTime)
                .last("limit 1"));
        if(Objects.isNull(product)) {
            return;
        }
        // 创建工艺路线
        Routing routing = new Routing();
        routing.setCode(String.format("编码%s", DateUtil.format(new Date(), "yyyyMMddHHmmss")));
        routing.setName(String.format("名称%s", DateUtil.format(new Date(), "yyyyMMddHHmmss")));
        routing.setProductId(product.getId());
        routing.setVersion("V1.0");
        routingMapper.insert(routing);
        // 查询工序
        List<Operation> operationList = operationMapper.selectList(new LambdaQueryWrapper<Operation>()
                .select(Operation::getId, Operation::getCode, Operation::getName)
                .orderByAsc(Operation::getCreateTime));
        // 工序绑定到工艺路线
        for (int i = 0; i < operationList.size(); i++) {
            Operation operation = operationList.get(i);
            RoutingOperationRel rel = new RoutingOperationRel(routing.getId(), operation.getId(), i + 1);
            routingOperationRelMapper.insert(rel);
        }


        // 查询物料信息
        List<Material> materialList = materialMapper.selectList(new LambdaQueryWrapper<Material>()
                .select(Material::getId, Material::getCode, Material::getName)
                .orderByAsc(Material::getCreateTime));
        // 创建物料清单
        Bom bom = new Bom();
        bom.setCode(String.format("编码%s", DateUtil.format(new Date(), "yyyyMMddHHmmss")));
        bom.setName(String.format("名称%s", DateUtil.format(new Date(), "yyyyMMddHHmmss")));
        bom.setProductId(product.getId());
        bom.setVersion("V1.0");
        bomMapper.insert(bom);
        // 物料绑定到物料清单明细
        for (int i = 0; i < materialList.size(); i++) {
            Material material = materialList.get(0);
            BomDetail bomDetail = new BomDetail();
            bomDetail.setBomId(bom.getId());
            bomDetail.setMaterialId(material.getId());
            bomDetail.setQty(BigDecimal.ONE);
            bomDetail.setParentId("0");
            bomDetailMapper.insert(bomDetail);
        }
    }
}
