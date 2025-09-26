package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.CustomerAddDTO;
import com.maxinhai.platform.dto.CustomerEditDTO;
import com.maxinhai.platform.dto.CustomerQueryDTO;
import com.maxinhai.platform.po.Customer;
import com.maxinhai.platform.vo.CustomerVO;

public interface CustomerService extends IService<Customer> {

    Page<CustomerVO> searchByPage(CustomerQueryDTO param);

    CustomerVO getInfo(String id);

    void remove(String[] ids);

    void edit(CustomerEditDTO param);

    void add(CustomerAddDTO param);

}
