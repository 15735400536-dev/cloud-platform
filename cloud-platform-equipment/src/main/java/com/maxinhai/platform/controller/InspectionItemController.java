package com.maxinhai.platform.controller;

import com.maxinhai.platform.dto.InspectionItemAddDTO;
import com.maxinhai.platform.dto.InspectionItemEditDTO;
import com.maxinhai.platform.dto.InspectionItemQueryDTO;
import com.maxinhai.platform.service.InspectionItemService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.InspectionItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @ClassName：InspectionItemController
 * @Author: XinHai.Ma
 * @Date: 2025/10/10 15:13
 * @Description: 巡检项目管理接口
 */
@RestController
@RequestMapping("/equip")
@Api(tags = "巡检项目管理接口")
public class InspectionItemController {

    @Resource
    private InspectionItemService inspectionItemService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询巡检项目信息", notes = "根据查询条件分页查询巡检项目信息")
    public AjaxResult<PageResult<InspectionItemVO>> searchByPage(@RequestBody InspectionItemQueryDTO page) {
        return AjaxResult.success(PageResult.convert(inspectionItemService.searchByPage(page)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取巡检项目信息", notes = "根据巡检项目ID获取详细信息")
    public AjaxResult<InspectionItemVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(inspectionItemService.getInfo(id));
    }

    @PostMapping("/addItem")
    @ApiOperation(value = "添加巡检项目信息", notes = "添加巡检项目信息")
    public AjaxResult<Void> addInspectionItem(@RequestBody InspectionItemAddDTO param) {
        inspectionItemService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editItem")
    @ApiOperation(value = "编辑巡检项目信息", notes = "根据巡检项目ID编辑巡检项目信息")
    public AjaxResult<Void> editInspectionItem(@RequestBody InspectionItemEditDTO param) {
        inspectionItemService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeItem")
    @ApiOperation(value = "删除巡检项目信息", notes = "根据巡检项目ID数组删除巡检项目信息")
    public AjaxResult<Void> removeInspectionItem(@RequestBody String[] ids) {
        inspectionItemService.remove(ids);
        return AjaxResult.success();
    }

}
