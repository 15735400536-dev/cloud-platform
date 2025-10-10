package com.maxinhai.platform.service.technology.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.bo.BomBO;
import com.maxinhai.platform.bo.RoutingBO;
import com.maxinhai.platform.bo.RoutingExcelBO;
import com.maxinhai.platform.dto.technology.RoutingAddDTO;
import com.maxinhai.platform.dto.technology.RoutingEditDTO;
import com.maxinhai.platform.dto.technology.RoutingQueryDTO;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.listener.RoutingExcelListener;
import com.maxinhai.platform.mapper.OperationMapper;
import com.maxinhai.platform.mapper.ProductMapper;
import com.maxinhai.platform.mapper.RoutingMapper;
import com.maxinhai.platform.mapper.RoutingOperationRelMapper;
import com.maxinhai.platform.po.Product;
import com.maxinhai.platform.po.technology.Bom;
import com.maxinhai.platform.po.technology.Operation;
import com.maxinhai.platform.po.technology.Routing;
import com.maxinhai.platform.po.technology.RoutingOperationRel;
import com.maxinhai.platform.service.CommonCodeCheckService;
import com.maxinhai.platform.service.technology.RoutingOperationRelService;
import com.maxinhai.platform.service.technology.RoutingService;
import com.maxinhai.platform.vo.technology.RoutingVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RoutingServiceImpl extends ServiceImpl<RoutingMapper, Routing> implements RoutingService {

    @Resource
    private RoutingMapper routingMapper;
    @Resource
    private RoutingOperationRelMapper relMapper;
    @Resource
    private RoutingOperationRelService relService;
    @Resource
    private CommonCodeCheckService commonCodeCheckService;
    @Resource
    private RoutingExcelListener routingExcelListener;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private OperationMapper operationMapper;

    @Override
    public Page<RoutingVO> searchByPage(RoutingQueryDTO param) {
        return routingMapper.selectJoinPage(param.getPage(), RoutingVO.class,
                new MPJLambdaWrapper<Routing>()
                        .innerJoin(Product.class, Product::getId, Routing::getProductId)
                        // 查询条件
                        .like(StrUtil.isNotBlank(param.getCode()), Routing::getCode, param.getCode())
                        .like(StrUtil.isNotBlank(param.getName()), Routing::getName, param.getName())
                        // 字段别名
                        .selectAll(Routing.class)
                        .selectAs(Product::getCode, RoutingVO::getProductCode)
                        .selectAs(Product::getName, RoutingVO::getProductName)
                        // 排序
                        .orderByDesc(Routing::getCreateTime));
    }

    @Override
    public RoutingVO getInfo(String id) {
        return routingMapper.selectJoinOne(RoutingVO.class,
                new MPJLambdaWrapper<Routing>()
                        .innerJoin(Product.class, Product::getId, Routing::getProductId)
                        // 查询条件
                        .like(Routing::getId, id)
                        // 字段别名
                        .selectAll(Routing.class)
                        .selectAs(Product::getCode, RoutingVO::getProductCode)
                        .selectAs(Product::getName, RoutingVO::getProductName));
    }

    @Override
    public void remove(String[] ids) {
        List<String> collect = Arrays.stream(ids).collect(Collectors.toList());
        // 删除工艺路线
        routingMapper.deleteBatchIds(collect);
        // 删除工艺路线明细
        relMapper.delete(new LambdaQueryWrapper<RoutingOperationRel>().in(RoutingOperationRel::getRoutingId, collect));
    }

    @Override
    public void edit(RoutingEditDTO param) {
        Routing routing = BeanUtil.toBean(param, Routing.class);
        routingMapper.updateById(routing);
    }

    @Override
    public void add(RoutingAddDTO param) {
        boolean unique = commonCodeCheckService.isCodeUnique(Routing.class, Routing::getCode, param.getCode());
        if (!unique) {
            throw new BusinessException("工艺路线【" + param.getCode() + "】已存在!");
        }
        Routing routing = BeanUtil.toBean(param, Routing.class);
        routingMapper.insert(routing);
    }

    @Override
    public void binding(String routingId, List<String> operationIds) {
        // 删除旧的关联关系
        relMapper.delete(new LambdaQueryWrapper<RoutingOperationRel>().eq(RoutingOperationRel::getRoutingId, routingId));
        // 新建新的关联关系
        List<RoutingOperationRel> relList = new ArrayList<>();
        for (int i = 0; i < operationIds.size(); i++) {
            RoutingOperationRel rel = new RoutingOperationRel();
            rel.setRoutingId(routingId);
            rel.setOperationId(operationIds.get(i));
            rel.setSort(i + 1);
            relList.add(rel);
        }
        relService.saveBatch(relList);
    }

    @Override
    public void importExcel(MultipartFile file) {
        try {
            // 调用EasyExcel读取文件
            EasyExcel.read(file.getInputStream(), RoutingExcelBO.class, routingExcelListener)
                    .sheet() // 读取第一个sheet
                    .doRead(); // 执行读取操作
        } catch (IOException e) {
            log.error("Excel数据导入失败", e);
            throw new BusinessException("Excel数据导入失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveExcelData(List<RoutingExcelBO> dataList) {
        // 对dataList按productCode和version分组
        Map<RoutingExcelBO.ProductVersionKey, List<RoutingExcelBO>> keyListMap = RoutingExcelBO.groupByProductAndVersion(dataList);

        // 查询产品信息
        Set<String> productCodeSet = dataList.stream().map(RoutingExcelBO::getProductCode).collect(Collectors.toSet());

        // 校验工艺路线是否存在
        List<RoutingBO> routingList = routingMapper.selectJoinList(RoutingBO.class, new MPJLambdaWrapper<Routing>()
                .innerJoin(Product.class, Product::getId, Bom::getProductId)
                .in(Product::getCode, productCodeSet)
                .select(Routing::getVersion, Routing::getProductId)
                .select(Product::getCode, Product::getName)
                .selectAll(Routing.class)
                .selectAs(Product::getCode, BomBO::getProductCode)
                .selectAs(Product::getName, BomBO::getProductName));
        Set<String> routingSet = routingList.stream()
                .map(routing -> routing.getProductCode() + "_" + routing.getVersion())
                .filter(key -> keyListMap.containsKey(new RoutingExcelBO.ProductVersionKey(key.split("_")[0], key.split("_")[1])))
                .collect(Collectors.toSet());
        if (!routingSet.isEmpty()) {
            throw new BusinessException("工艺路线【" + StringUtils.collectionToDelimitedString(routingSet, ",") + "】已存在！");
        }

        List<Product> productList = productMapper.selectList(new LambdaQueryWrapper<Product>()
                .in(Product::getCode, productCodeSet));
        Map<String, Product> productMap = productList.stream().collect(Collectors.toMap(Product::getCode, Product -> Product));
        // 查询工序信息
        Set<String> operationCodeSet = dataList.stream().map(RoutingExcelBO::getOperationCode).collect(Collectors.toSet());
        List<Operation> operationList = operationMapper.selectList(new LambdaQueryWrapper<Operation>()
                .select(Operation::getId, Operation::getCode, Operation::getName)
                .in(Operation::getCode, operationCodeSet));
        Map<String, Operation> operationMap = operationList.stream().collect(Collectors.toMap(Operation::getCode, Operation -> Operation));

        // 保存数据
        List<RoutingOperationRel> relList = new ArrayList<>(dataList.size());
        for (Map.Entry<RoutingExcelBO.ProductVersionKey, List<RoutingExcelBO>> next : keyListMap.entrySet()) {
            RoutingExcelBO.ProductVersionKey key = next.getKey();
            List<RoutingExcelBO> value = next.getValue();
            // 创建工艺路线
            Product product = productMap.get(key.getProductCode());
            Routing routing = new Routing();
            routing.setCode(product.getCode() + "_" + key.getVersion());
            routing.setName(product.getName() + "_" + key.getVersion());
            routing.setProductId(product.getId());
            routing.setVersion(key.getVersion());
            routing.setStatus(1);
            routingMapper.insert(routing);
            // 创建工序
            for (int i = 0; i < value.size(); i++) {
                RoutingExcelBO excelBO = value.get(i);

                Operation operation = null;
                if (operationMap.containsKey(excelBO.getOperationCode())) {
                    operation = operationMap.get(excelBO.getOperationCode());
                } else {
                    operation = new Operation();
                    operation.setCode(excelBO.getOperationCode());
                    operation.setName(excelBO.getOperationName());
                    operation.setWorkTime(excelBO.getWorkTime());
                    operation.setStatus(1);
                    operationMapper.insert(operation);
                }

                // 绑定关系
                RoutingOperationRel rel = new RoutingOperationRel(routing.getId(), operation.getId(), i + 1);
                relList.add(rel);
            }
        }
        relService.saveBatch(relList);
    }
}
