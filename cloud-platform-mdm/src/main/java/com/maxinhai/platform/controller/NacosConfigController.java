package com.maxinhai.platform.controller;

import com.maxinhai.platform.po.Product;
import com.maxinhai.platform.service.technology.BomService;
import com.maxinhai.platform.service.technology.ProductService;
import com.maxinhai.platform.service.technology.RoutingService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.vo.technology.BomVO;
import com.maxinhai.platform.vo.technology.RoutingVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RefreshScope
@RestController
@RequestMapping("/nacos")
@Api(tags = "Nacos配置管理接口")
public class NacosConfigController {

    @Value("${service.name:unknown}")
    private String nacosConfig;
    @Resource
    private ProductService productService;
    @Resource
    private BomService bomService;
    @Resource
    private RoutingService routingService;

    @GetMapping("/getConfig")
    @ApiOperation(value = "获取nacos配置文件配置", notes = "获取nacos配置文件配置")
    public AjaxResult<String> getConfig() {
        return AjaxResult.success(nacosConfig);
    }

    @GetMapping("/asyncGetInfo")
    @ApiOperation(value = "异步获取产品相关信息", notes = "异步获取产品相关信息")
    public AjaxResult<Map<String, Object>> asyncGetInfo() throws ExecutionException, InterruptedException {
        String productCode = "PC00001";
        CompletableFuture<Product> productFuture = productService.getProductByCode(productCode);
        CompletableFuture<BomVO> bomFuture = bomService.getBomByProductCode(productCode);
        CompletableFuture<RoutingVO> routingFuture = routingService.getRoutingByProductCode(productCode);

        Map<String, Object> result = new HashMap<>();
        result.put("productCode", productCode);
        result.put("product", productFuture.get());
        result.put("bom", bomFuture.get());
        result.put("routing", routingFuture.get());
        return AjaxResult.success(result);
    }

}
