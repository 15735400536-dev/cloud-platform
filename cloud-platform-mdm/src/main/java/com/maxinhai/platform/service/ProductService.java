package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.ProductAddDTO;
import com.maxinhai.platform.dto.ProductEditDTO;
import com.maxinhai.platform.dto.ProductQueryDTO;
import com.maxinhai.platform.po.Product;
import com.maxinhai.platform.vo.ProductVO;

public interface ProductService extends IService<Product> {

    Page<ProductVO> searchByPage(ProductQueryDTO param);

    ProductVO getInfo(String id);

    void remove(String[] ids);

    void edit(ProductEditDTO param);

    void add(ProductAddDTO param);

}
