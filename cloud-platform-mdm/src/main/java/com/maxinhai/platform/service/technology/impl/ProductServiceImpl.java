package com.maxinhai.platform.service.technology.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.bo.ProductExcelBO;
import com.maxinhai.platform.dto.ProductAddDTO;
import com.maxinhai.platform.dto.ProductEditDTO;
import com.maxinhai.platform.dto.ProductQueryDTO;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.feign.SystemFeignClient;
import com.maxinhai.platform.listener.ProductExcelListener;
import com.maxinhai.platform.mapper.ProductMapper;
import com.maxinhai.platform.po.Product;
import com.maxinhai.platform.service.technology.ProductService;
import com.maxinhai.platform.vo.ProductVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Resource
    private ProductMapper productMapper;
    @Resource
    private SystemFeignClient systemFeignClient;
    @Resource
    private ProductExcelListener productExcelListener;

    @Override
    public Page<ProductVO> searchByPage(ProductQueryDTO param) {
        return productMapper.selectJoinPage(param.getPage(), ProductVO.class,
                new MPJLambdaWrapper<Product>()
                        // 查询条件
                        .like(StrUtil.isNotBlank(param.getCode()), Product::getCode, param.getCode())
                        .like(StrUtil.isNotBlank(param.getName()), Product::getName, param.getName())
                        // 字段别名
                        .selectAll(Product.class)
                        // 排序
                        .orderByDesc(Product::getCreateTime));
    }

    @Override
    public ProductVO getInfo(String id) {
        return productMapper.selectJoinOne(ProductVO.class,
                new MPJLambdaWrapper<Product>()
                        // 查询条件
                        .like(Product::getId, id)
                        // 字段别名
                        .selectAll(Product.class)
                        // 排序
                        .orderByDesc(Product::getCreateTime));
    }

    @Override
    public void remove(String[] ids) {
        productMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(ProductEditDTO param) {
        Product product = BeanUtil.toBean(param, Product.class);
        productMapper.updateById(product);
    }

    @Override
    public void add(ProductAddDTO param) {
        Product product = BeanUtil.toBean(param, Product.class);
        List<String> codeList = systemFeignClient.generateCode("product", 1).getData();
        product.setCode(codeList.get(0));
        productMapper.insert(product);
    }

    @Override
    public void saveExcelData(List<ProductExcelBO> dataList) {
        List<String> codeList = dataList.stream().map(ProductExcelBO::getCode).collect(Collectors.toList());
        List<Product> productList = productMapper.selectList(new LambdaQueryWrapper<Product>()
                .select(Product::getId, Product::getCode)
                .in(Product::getCode, codeList));
        if (!productList.isEmpty()) {
            Set<String> existCodeSet = productList.stream().map(Product::getCode).collect(Collectors.toSet());
            throw new BusinessException("产品【" + StringUtils.collectionToDelimitedString(existCodeSet, ",") + "】已存在！");
        }
        //long count = dataList.stream().filter(data -> StrUtil.isEmpty(data.getCode())).count();
        //List<String> productCodeList = systemFeignClient.generateCode("product", Integer.valueOf(String.valueOf(count))).getData();
        List<Product> saveList = new ArrayList<>(dataList.size());
        for (int i = 0; i < dataList.size(); i++) {
            ProductExcelBO excelBO = dataList.get(i);
            Product product = new Product(excelBO.getCode(), excelBO.getName());
            saveList.add(product);
        }
        this.saveBatch(saveList);
    }

    @Override
    public void importExcel(MultipartFile file) {
        try {
            // 调用EasyExcel读取文件
            EasyExcel.read(file.getInputStream(), ProductExcelBO.class, productExcelListener)
                    .sheet() // 读取第一个sheet
                    .doRead(); // 执行读取操作
        } catch (IOException e) {
            log.error("Excel数据导入失败", e);
            throw new BusinessException("Excel数据导入失败：" + e.getMessage());
        }
    }

    @Override
    public CompletableFuture<Product> getProductByCode(String productCode) {
        Product product = productMapper.selectOne(new LambdaQueryWrapper<Product>()
                .select(Product::getId, Product::getCode, Product::getName)
                .eq(Product::getCode, productCode));
        return CompletableFuture.completedFuture(product);
    }
}
