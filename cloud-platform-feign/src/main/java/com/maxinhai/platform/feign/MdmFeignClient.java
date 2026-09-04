package com.maxinhai.platform.feign;

import com.maxinhai.platform.fallback.MdmFeignFallbackFactory;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.vo.technology.BomDetailVO;
import com.maxinhai.platform.vo.technology.BomInfoVO;
import com.maxinhai.platform.vo.technology.OperationVO;
import com.maxinhai.platform.vo.technology.RoutingInfoVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "cloud-platform-mdm",
        //configuration = FeignConfig.class,
        fallbackFactory = MdmFeignFallbackFactory.class)
public interface MdmFeignClient {

    @GetMapping("/nacos/getConfig")
    AjaxResult<String> getConfig();

    @GetMapping("/bomDetail/queryBomDetail/{productCode}/{bomVersion}")
    @ApiOperation(value = "根据产品编码和版本号查询BOM明细", notes = "根据产品编码和版本号查询BOM明细")
    AjaxResult<List<BomDetailVO>> queryBomDetail(@PathVariable("productCode") String productCode,
                                                 @PathVariable("bomVersion") String bomVersion);

    @PostMapping("/routing/queryRoutingDetail/{productCode}/{routingVersion}")
    @ApiOperation(value = "根据产品编码和版本号查询工艺路线明细", notes = "根据产品编码和版本号查询工艺路线明细")
    AjaxResult<List<OperationVO>> queryRoutingDetail(@PathVariable("productCode") String productCode,
                                                     @PathVariable("routingVersion") String routingVersion);

    @GetMapping(value = "/routing/queryRoutingInfo/{productCode}/{routingVersion}")
    @ApiOperation(value = "根据产品编码和版本号查询工艺路线信息", notes = "根据产品编码和版本号查询工艺路线信息")
    AjaxResult<RoutingInfoVO> queryRoutingInfo(@PathVariable("productCode") String productCode,
                                               @PathVariable("routingVersion") String routingVersion);

    @GetMapping("/bom/queryBomInfo/{productCode}/{bomVersion}")
    @ApiOperation(value = "根据产品编码和版本号查询BOM信息", notes = "根据产品编码和版本号查询BOM信息")
    AjaxResult<BomInfoVO> queryBomInfo(@PathVariable("productCode") String productCode,
                                       @PathVariable("bomVersion") String bomVersion);

}
