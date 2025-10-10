package com.maxinhai.platform.service.technology.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.bo.BomBO;
import com.maxinhai.platform.bo.BomExcelBO;
import com.maxinhai.platform.dto.technology.BomAddDTO;
import com.maxinhai.platform.dto.technology.BomEditDTO;
import com.maxinhai.platform.dto.technology.BomQueryDTO;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.listener.BomExcelListener;
import com.maxinhai.platform.mapper.BomMapper;
import com.maxinhai.platform.mapper.MaterialMapper;
import com.maxinhai.platform.mapper.ProductMapper;
import com.maxinhai.platform.po.Material;
import com.maxinhai.platform.po.Product;
import com.maxinhai.platform.po.technology.Bom;
import com.maxinhai.platform.po.technology.BomDetail;
import com.maxinhai.platform.service.technology.BomDetailService;
import com.maxinhai.platform.service.technology.BomService;
import com.maxinhai.platform.service.CommonCodeCheckService;
import com.maxinhai.platform.vo.technology.BomVO;
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
public class BomServiceImpl extends ServiceImpl<BomMapper, Bom> implements BomService {

    @Resource
    private BomMapper bomMapper;
    @Resource
    private CommonCodeCheckService commonCodeCheckService;
    @Resource
    private BomExcelListener bomExcelListener;
    @Resource
    private MaterialMapper materialMapper;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private BomDetailService bomDetailService;

    @Override
    public Page<BomVO> searchByPage(BomQueryDTO param) {
        return bomMapper.selectJoinPage(param.getPage(), BomVO.class,
                new MPJLambdaWrapper<Bom>()
                        .innerJoin(Product.class, Product::getId, Bom::getProductId)
                        // 查询条件
                        .like(StrUtil.isNotBlank(param.getCode()), Bom::getCode, param.getCode())
                        .like(StrUtil.isNotBlank(param.getName()), Bom::getName, param.getName())
                        // 字段别名
                        .selectAll(Bom.class)
                        .selectAs(Product::getCode, BomVO::getProductCode)
                        .selectAs(Product::getName, BomVO::getProductName)
                        // 排序
                        .orderByDesc(Bom::getCreateTime));
    }

    @Override
    public BomVO getInfo(String id) {
        return bomMapper.selectJoinOne(BomVO.class,
                new MPJLambdaWrapper<Bom>()
                        .innerJoin(Product.class, Product::getId, Bom::getProductId)
                        // 查询条件
                        .eq(Bom::getId, id)
                        // 字段别名
                        .selectAll(Bom.class)
                        .selectAs(Product::getCode, BomVO::getProductCode)
                        .selectAs(Product::getName, BomVO::getProductName));
    }

    @Override
    public void remove(String[] ids) {
        List<String> collect = Arrays.stream(ids).collect(Collectors.toList());
        // 删除BOM
        bomMapper.deleteBatchIds(collect);
        // 删除BOM明细
        bomDetailService.remove(new LambdaQueryWrapper<BomDetail>().in(BomDetail::getBomId, collect));
    }

    @Override
    public void edit(BomEditDTO param) {
        Bom bom = BeanUtil.toBean(param, Bom.class);
        bomMapper.updateById(bom);
    }

    @Override
    public void add(BomAddDTO param) {
        boolean unique = commonCodeCheckService.isCodeUnique(Bom.class, Bom::getCode, param.getCode());
        if (!unique) {
            throw new BusinessException("BOM【" + param.getCode() + "】已存在!");
        }
        Bom bom = BeanUtil.toBean(param, Bom.class);
        bomMapper.insert(bom);
    }

    @Override
    public void importExcel(MultipartFile file) {
        try {
            // 调用EasyExcel读取文件
            EasyExcel.read(file.getInputStream(), BomExcelBO.class, bomExcelListener)
                    .sheet() // 读取第一个sheet
                    .doRead(); // 执行读取操作
        } catch (IOException e) {
            log.error("Excel数据导入失败", e);
            throw new BusinessException("Excel数据导入失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveExcelData(List<BomExcelBO> dataList) {
        // 对dataList按productCode和version分组
        Map<BomExcelBO.ProductVersionKey, List<BomExcelBO>> keyListMap = BomExcelBO.groupByProductAndVersion(dataList);
        // 查询产品信息
        Set<String> productCodeSet = dataList.stream().map(BomExcelBO::getProductCode).collect(Collectors.toSet());

        // 校验BOM是否存在
        List<BomBO> bomList = bomMapper.selectJoinList(BomBO.class, new MPJLambdaWrapper<Bom>()
                .innerJoin(Product.class, Product::getId, Bom::getProductId)
                .in(Product::getCode, productCodeSet)
                .select(Bom::getVersion, Bom::getProductId)
                .select(Product::getCode, Product::getName)
                .selectAll(Bom.class)
                .selectAs(Product::getCode, BomBO::getProductCode)
                .selectAs(Product::getName, BomBO::getProductName));
        Set<String> bomSet = bomList.stream()
                .map(bom -> bom.getProductCode() + "_" + bom.getVersion())
                .filter(key -> keyListMap.containsKey(new BomExcelBO.ProductVersionKey(key.split("_")[0], key.split("_")[1])))
                .collect(Collectors.toSet());
        if (!bomSet.isEmpty()) {
            throw new BusinessException("BOM【" + StringUtils.collectionToDelimitedString(bomSet, ",") + "】已存在！");
        }

        List<Product> productList = productMapper.selectList(new LambdaQueryWrapper<Product>()
                .in(Product::getCode, productCodeSet));
        Map<String, Product> productMap = productList.stream().collect(Collectors.toMap(Product::getCode, Product -> Product));
        // 查询物料信息
        Set<String> materialCodeSet = dataList.stream().map(BomExcelBO::getMaterialCode).collect(Collectors.toSet());
        List<Material> materialList = materialMapper.selectList(new LambdaQueryWrapper<Material>()
                .select(Material::getId, Material::getCode)
                .in(Material::getCode, materialCodeSet));
        Map<String, Material> materialMap = materialList.stream().collect(Collectors.toMap(Material::getCode, Material -> Material));

        // 保存数据
        List<BomDetail> bomDetailList = new ArrayList<>(dataList.size());
        for (Map.Entry<BomExcelBO.ProductVersionKey, List<BomExcelBO>> next : keyListMap.entrySet()) {
            BomExcelBO.ProductVersionKey key = next.getKey();
            List<BomExcelBO> value = next.getValue();
            // 创建BOM
            Bom bom = new Bom();
            Product product = productMap.get(key.getProductCode());
            bom.setCode(key.getProductCode() + "_" + key.getVersion());
            bom.setName(product.getName() + "_" + key.getVersion());
            bom.setVersion(key.getVersion());
            bom.setProductId(product.getId());
            bomMapper.insert(bom);
            // 创建BOM明细
            for (BomExcelBO bomExcelBO : value) {
                BomDetail bomDetail = new BomDetail();
                bomDetail.setBomId(bom.getId());
                Material material = materialMap.get(bomExcelBO.getMaterialCode());
                bomDetail.setMaterialId(material.getId());
                bomDetail.setQty(bomExcelBO.getQty());
                bomDetail.setParentId("0");
                bomDetailList.add(bomDetail);
            }
        }
        bomDetailService.saveBatch(bomDetailList);
    }
}
