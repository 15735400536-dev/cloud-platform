package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.CustomerAddDTO;
import com.maxinhai.platform.dto.CustomerEditDTO;
import com.maxinhai.platform.dto.CustomerQueryDTO;
import com.maxinhai.platform.mapper.CustomerMapper;
import com.maxinhai.platform.po.Customer;
import com.maxinhai.platform.service.CustomerService;
import com.maxinhai.platform.vo.CustomerVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName：CustomerServiceImpl
 * @Author: XinHai.Ma
 * @Date: 2025/9/26 16:01
 * @Description: 客户管理业务层
 */
@Slf4j
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

    @Resource
    private CustomerMapper customerMapper;

    @Override
    public Page<CustomerVO> searchByPage(CustomerQueryDTO param) {
        Page<CustomerVO> pageResult = customerMapper.selectJoinPage(param.getPage(), CustomerVO.class,
                new MPJLambdaWrapper<Customer>()
                        // 查询条件
                        .like(StrUtil.isNotBlank(param.getName()), Customer::getName, param.getName())
                        .eq(Objects.nonNull(param.getType()), Customer::getType, param.getType())
                        // 字段别名
                        .selectAll(Customer.class)
                        // 排序
                        .orderByDesc(Customer::getCreateTime));
        return pageResult;
    }

    @Override
    public CustomerVO getInfo(String id) {
        return customerMapper.selectJoinOne(CustomerVO.class,
                new MPJLambdaWrapper<Customer>()
                        // 查询条件
                        .eq(Customer::getId, id)
                        // 字段别名
                        .selectAll(Customer.class));
    }

    @Override
    public void remove(String[] ids) {
        customerMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(CustomerEditDTO param) {
        Customer operation = BeanUtil.toBean(param, Customer.class);
        customerMapper.updateById(operation);
    }

    @Override
    public void add(CustomerAddDTO param) {
        Customer operation = BeanUtil.toBean(param, Customer.class);
        customerMapper.insert(operation);
    }
}
