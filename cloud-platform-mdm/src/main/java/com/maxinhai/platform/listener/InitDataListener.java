package com.maxinhai.platform.listener;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.maxinhai.platform.mapper.*;
import com.maxinhai.platform.po.Material;
import com.maxinhai.platform.po.MaterialType;
import com.maxinhai.platform.po.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
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
    @Resource
    private MaterialTypeMapper materialTypeMapper;

    @Override
    public void run(String... args) throws Exception {
//        // 查询最新产品
//        Product product = productMapper.selectOne(new LambdaQueryWrapper<Product>()
//                .select(Product::getId, Product::getCode, Product::getName)
//                .orderByDesc(Product::getCreateTime)
//                .last("limit 1"));
//        if (Objects.isNull(product)) {
//            return;
//        }
//        // 创建工艺路线
//        Routing routing = new Routing();
//        routing.setCode(String.format("编码%s", DateUtil.format(new Date(), "yyyyMMddHHmmss")));
//        routing.setName(String.format("名称%s", DateUtil.format(new Date(), "yyyyMMddHHmmss")));
//        routing.setProductId(product.getId());
//        routing.setVersion("V1.0");
//        routingMapper.insert(routing);
//        // 查询工序
//        List<Operation> operationList = operationMapper.selectList(new LambdaQueryWrapper<Operation>()
//                .select(Operation::getId, Operation::getCode, Operation::getName)
//                .orderByAsc(Operation::getCreateTime));
//        // 工序绑定到工艺路线
//        for (int i = 0; i < operationList.size(); i++) {
//            Operation operation = operationList.get(i);
//            RoutingOperationRel rel = new RoutingOperationRel(routing.getId(), operation.getId(), i + 1);
//            routingOperationRelMapper.insert(rel);
//        }
//
//
//        // 查询物料信息
//        List<Material> materialList = materialMapper.selectList(new LambdaQueryWrapper<Material>()
//                .select(Material::getId, Material::getCode, Material::getName)
//                .orderByAsc(Material::getCreateTime));
//        // 创建物料清单
//        Bom bom = new Bom();
//        bom.setCode(String.format("编码%s", DateUtil.format(new Date(), "yyyyMMddHHmmss")));
//        bom.setName(String.format("名称%s", DateUtil.format(new Date(), "yyyyMMddHHmmss")));
//        bom.setProductId(product.getId());
//        bom.setVersion("V1.0");
//        bomMapper.insert(bom);
//        // 物料绑定到物料清单明细
//        for (int i = 0; i < materialList.size(); i++) {
//            Material material = materialList.get(0);
//            BomDetail bomDetail = new BomDetail();
//            bomDetail.setBomId(bom.getId());
//            bomDetail.setMaterialId(material.getId());
//            bomDetail.setQty(BigDecimal.ONE);
//            bomDetail.setParentId("0");
//            bomDetailMapper.insert(bomDetail);
//        }

//        readJson();
    }

    public void readJson() {
        // 读取汽车数据
        String jsonStr = ResourceUtil.readUtf8Str("car1.json");
        JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
        JSONArray jsonArray = jsonObject.getJSONArray("燃油车型");
        for (Object o : jsonArray) {
            JSONObject json = (JSONObject) o;
            Product product = new Product();
            product.setCode(json.getStr("车型"));
            product.setName(json.getStr("车型"));
            productMapper.insert(product);
        }

        // 读取物料数据
        jsonStr = ResourceUtil.readUtf8Str("sngz.json");
        jsonObject = JSONUtil.parseObj(jsonStr);
        JSONObject bomInfo = jsonObject.getJSONObject("水泥管桩物料清单").getJSONObject("bom_info");
        MaterialType materialType = new MaterialType();
        materialType.setCode(bomInfo.getStr("适用机型"));
        materialType.setName(bomInfo.getStr("适用机型"));
        materialType.setDescription(bomInfo.getStr("备注"));
        materialType.setParentId("0");
        materialTypeMapper.insert(materialType);

        JSONObject mrlType = jsonObject.getJSONObject("水泥管桩物料清单").getJSONObject("物料分类");
        for (String key : mrlType.keySet()) {
            jsonArray = mrlType.getJSONArray(key);
            for (Object o : jsonArray) {
                JSONObject json = (JSONObject) o;
                Material material = new Material();
                material.setCode(json.getStr("物料ID"));
                material.setName(json.getStr("物料名称"));
                material.setUnit(json.getStr("单位"));
                material.setDescription(json.getStr("供应商"));
                material.setModel(json.getStr("规格型号"));
                material.setSpecs(json.getStr("规格型号"));
                material.setMaterial(json.getStr("物料描述"));
                material.setMaterialTypeId(materialType.getId());
                materialMapper.insert(material);
            }
        }
    }

}
