package com.maxinhai.platform.fallback;

import com.maxinhai.platform.feign.MdmFeignClient;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.vo.technology.BomDetailVO;
import com.maxinhai.platform.vo.technology.BomInfoVO;
import com.maxinhai.platform.vo.technology.OperationVO;
import com.maxinhai.platform.vo.technology.RoutingInfoVO;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MdmFeignFallbackFactory implements FallbackFactory<MdmFeignClient> {

    @Override
    public MdmFeignClient create(Throwable cause) {
        return new MdmFeignClient() {

            @Override
            public AjaxResult<String> getConfig() {
                return AjaxResult.fail("服务调用失败!", null);
            }

            @Override
            public AjaxResult<List<BomDetailVO>> queryBomDetail(String productCode, String bomVersion) {
                return AjaxResult.fail("服务调用失败!", null);
            }

            @Override
            public AjaxResult<List<OperationVO>> queryRoutingDetail(String productCode, String routingVersion) {
                return AjaxResult.fail("服务调用失败!", null);
            }

            @Override
            public AjaxResult<RoutingInfoVO> queryRoutingInfo(String productCode, String routingVersion) {
                return AjaxResult.fail("服务调用失败!", null);
            }

            @Override
            public AjaxResult<BomInfoVO> queryBomInfo(String productCode, String bomVersion) {
                return AjaxResult.fail("服务调用失败!", null);
            }
        };
    }
}
