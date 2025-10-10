package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.SupplierAddDTO;
import com.maxinhai.platform.dto.SupplierEditDTO;
import com.maxinhai.platform.dto.SupplierQueryDTO;
import com.maxinhai.platform.mapper.SupplierMapper;
import com.maxinhai.platform.po.Supplier;
import com.maxinhai.platform.service.SupplierService;
import com.maxinhai.platform.vo.SupplierVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName：SupplierServiceImpl
 * @Author: XinHai.Ma
 * @Date: 2025/9/26 16:02
 * @Description: 供应商管理业务层
 */
@Slf4j
@Service
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements SupplierService {

    @Resource
    private SupplierMapper supplierMapper;

    @Override
    public Page<SupplierVO> searchByPage(SupplierQueryDTO param) {
        return supplierMapper.selectJoinPage(param.getPage(), SupplierVO.class,
                new MPJLambdaWrapper<Supplier>()
                        // 查询条件
                        .eq(Objects.nonNull(param.getType()), Supplier::getType, param.getType())
                        .like(StrUtil.isNotBlank(param.getName()), Supplier::getName, param.getName())
                        // 字段别名
                        .selectAll(Supplier.class)
                        // 排序
                        .orderByDesc(Supplier::getCreateTime));
    }

    @Override
    public SupplierVO getInfo(String id) {
        return supplierMapper.selectJoinOne(SupplierVO.class,
                new MPJLambdaWrapper<Supplier>()
                        // 查询条件
                        .eq(Supplier::getId, id)
                        // 字段别名
                        .selectAll(Supplier.class));
    }

    @Override
    public void remove(String[] ids) {
        supplierMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(SupplierEditDTO param) {
        Supplier operation = BeanUtil.toBean(param, Supplier.class);
        supplierMapper.updateById(operation);
    }

    @Override
    public void add(SupplierAddDTO param) {
        Supplier operation = BeanUtil.toBean(param, Supplier.class);
        supplierMapper.insert(operation);
    }
}
