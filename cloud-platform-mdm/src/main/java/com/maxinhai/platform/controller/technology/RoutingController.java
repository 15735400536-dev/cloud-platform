package com.maxinhai.platform.controller.technology;

import com.maxinhai.platform.dto.technology.RoutingAddDTO;
import com.maxinhai.platform.dto.technology.RoutingEditDTO;
import com.maxinhai.platform.dto.technology.RoutingQueryDTO;
import com.maxinhai.platform.service.RoutingService;
import com.maxinhai.platform.vo.technology.RoutingVO;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RefreshScope
@RestController
@RequestMapping("/routing")
@Api(tags = "工艺路线管理接口")
public class RoutingController {

    @Resource
    private RoutingService routingService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询工艺路线信息", notes = "根据查询条件分页查询工艺路线信息")
    public AjaxResult<PageResult<RoutingVO>> searchByPage(@RequestBody RoutingQueryDTO param) {
        return AjaxResult.success(PageResult.convert(routingService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取工艺路线信息", notes = "根据工艺路线ID获取详细信息")
    public AjaxResult<RoutingVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(routingService.getInfo(id));
    }

    @PostMapping("/addRouting")
    @ApiOperation(value = "添加工艺路线信息", notes = "添加工艺路线信息")
    public AjaxResult<Void> addRouting(@RequestBody RoutingAddDTO param) {
        routingService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editRouting")
    @ApiOperation(value = "编辑工艺路线信息", notes = "根据工艺路线ID编辑工艺路线信息")
    public AjaxResult<Void> editRouting(@RequestBody RoutingEditDTO param) {
        routingService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeRouting")
    @ApiOperation(value = "删除工艺路线信息", notes = "根据工艺路线ID数组删除工艺路线信息")
    public AjaxResult<Void> removeRouting(@RequestBody String[] ids) {
        routingService.remove(ids);
        return AjaxResult.success();
    }

}
