package com.maxinhai.platform.controller.technology;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxinhai.platform.dto.technology.BomAddDTO;
import com.maxinhai.platform.dto.technology.BomEditDTO;
import com.maxinhai.platform.dto.technology.BomQueryDTO;
import com.maxinhai.platform.service.BomService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.technology.BomVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RefreshScope
@RestController
@RequestMapping("/bom")
@Api(tags = "BOM管理接口")
public class BomController {

    @Resource
    private BomService bomService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询BOM信息", notes = "根据查询条件分页查询BOM信息")
    public AjaxResult<PageResult<BomVO>> searchByPage(@RequestBody BomQueryDTO param) {
        return AjaxResult.success(PageResult.convert(bomService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取BOM信息", notes = "根据BOMID获取详细信息")
    public AjaxResult<BomVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(bomService.getInfo(id));
    }

    @PostMapping("/addBom")
    @ApiOperation(value = "添加BOM信息", notes = "添加BOM信息")
    public AjaxResult<Void> addBom(@RequestBody BomAddDTO param) {
        bomService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editBom")
    @ApiOperation(value = "编辑BOM信息", notes = "根据BOMID编辑BOM信息")
    public AjaxResult<Void> editBom(@RequestBody BomEditDTO param) {
        bomService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeBom")
    @ApiOperation(value = "删除BOM信息", notes = "根据BOMID数组删除BOM信息")
    public AjaxResult<Void> removeBom(@RequestBody String[] ids) {
        bomService.remove(ids);
        return AjaxResult.success();
    }

}
