package com.maxinhai.platform.controller.technology;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxinhai.platform.dto.technology.BomDetailAddDTO;
import com.maxinhai.platform.dto.technology.BomDetailEditDTO;
import com.maxinhai.platform.dto.technology.BomDetailQueryDTO;
import com.maxinhai.platform.service.BomDetailService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.technology.BomDetailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RefreshScope
@RestController
@RequestMapping("/bomDetail")
@Api(tags = "BOM明细管理接口")
public class BomDetailController {

    @Resource
    private BomDetailService bomDetailService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询BOM明细信息", notes = "根据查询条件分页查询BOM明细信息")
    public AjaxResult<Page<BomDetailVO>> searchByPage(@RequestBody BomDetailQueryDTO param) {
        return AjaxResult.success(PageResult.convert(bomDetailService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取BOM明细信息", notes = "根据BOM明细ID获取详细信息")
    public AjaxResult<BomDetailVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(bomDetailService.getInfo(id));
    }

    @PostMapping("/addBomDetail")
    @ApiOperation(value = "添加BOM明细信息", notes = "添加BOM明细信息")
    public AjaxResult<Void> addBomDetail(@RequestBody BomDetailAddDTO param) {
        bomDetailService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editBomDetail")
    @ApiOperation(value = "编辑BOM明细信息", notes = "根据BOM明细ID编辑BOM明细信息")
    public AjaxResult<Void> editBomDetail(@RequestBody BomDetailEditDTO param) {
        bomDetailService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeBomDetail")
    @ApiOperation(value = "删除BOM明细信息", notes = "根据BOM明细ID数组删除BOM明细信息")
    public AjaxResult<Void> removeBomDetail(@RequestBody String[] ids) {
        bomDetailService.remove(ids);
        return AjaxResult.success();
    }

}
