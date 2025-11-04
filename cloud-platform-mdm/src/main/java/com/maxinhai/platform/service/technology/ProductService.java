package com.maxinhai.platform.service.technology;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.bo.ProductExcelBO;
import com.maxinhai.platform.dto.ProductAddDTO;
import com.maxinhai.platform.dto.ProductEditDTO;
import com.maxinhai.platform.dto.ProductQueryDTO;
import com.maxinhai.platform.po.Product;
import com.maxinhai.platform.vo.ProductVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ProductService extends IService<Product> {

    Page<ProductVO> searchByPage(ProductQueryDTO param);

    ProductVO getInfo(String id);

    void remove(String[] ids);

    void edit(ProductEditDTO param);

    void add(ProductAddDTO param);

    void saveExcelData(List<ProductExcelBO> dataList);

    /**
     * 导入Excel数据
     * @param file 上传的Excel文件
     */
    void importExcel(MultipartFile file);

    CompletableFuture<Product> getProductByCode(String productCode);

}
