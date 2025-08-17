package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.ProductAddDTO;
import com.maxinhai.platform.dto.ProductEditDTO;
import com.maxinhai.platform.dto.ProductQueryDTO;
import com.maxinhai.platform.mapper.ProductMapper;
import com.maxinhai.platform.po.Product;
import com.maxinhai.platform.service.ProductService;
import com.maxinhai.platform.vo.ProductVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Resource
    private ProductMapper productMapper;

    @Override
    public Page<ProductVO> searchByPage(ProductQueryDTO param) {
        Page<ProductVO> pageResult = productMapper.selectJoinPage(param.getPage(), ProductVO.class,
                new MPJLambdaWrapper<Product>()
                        // 查询条件
                        .like(StrUtil.isNotBlank(param.getCode()), Product::getCode, param.getCode())
                        .like(StrUtil.isNotBlank(param.getName()), Product::getName, param.getName())
                        // 字段别名
                        .selectAll(Product.class)
                        // 排序
                        .orderByDesc(Product::getCreateTime));
        return pageResult;
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
        productMapper.insert(product);
    }

    //@PostConstruct
    public void initData() {
        Product product = new Product();
        product.setCode(String.format("编码%s", DateUtil.format(new Date(), "yyyyMMddHHmmss")));
        product.setName(String.format("名称%s", DateUtil.format(new Date(), "yyyyMMddHHmmss")));
        productMapper.insert(product);
    }
}
