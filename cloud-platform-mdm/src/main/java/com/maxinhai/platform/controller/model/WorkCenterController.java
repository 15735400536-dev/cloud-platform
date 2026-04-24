package com.maxinhai.platform.controller.model;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.maxinhai.platform.dto.WorkCenterAddDTO;
import com.maxinhai.platform.dto.WorkCenterEditDTO;
import com.maxinhai.platform.dto.WorkCenterQueryDTO;
import com.maxinhai.platform.po.ComboBox;
import com.maxinhai.platform.po.model.WorkCenter;
import com.maxinhai.platform.service.model.WorkCenterService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.ComboBoxUtils;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.model.WorkCenterVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RefreshScope
@RestController
@RequestMapping("/workCenter")
@Api(tags = "加工中心管理接口")
public class WorkCenterController {

    @Resource
    private WorkCenterService workCenterService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询加工中心信息", notes = "根据查询条件分页查询加工中心信息")
    public AjaxResult<PageResult<WorkCenterVO>> searchByPage(@RequestBody WorkCenterQueryDTO param) {
        return AjaxResult.success(PageResult.convert(workCenterService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取加工中心信息", notes = "根据加工中心ID获取详细信息")
    public AjaxResult<WorkCenterVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(workCenterService.getInfo(id));
    }

    @PostMapping("/addWorkCenter")
    @ApiOperation(value = "添加加工中心信息", notes = "添加加工中心信息")
    public AjaxResult<Void> addWorkCenter(@RequestBody WorkCenterAddDTO param) {
        workCenterService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editWorkCenter")
    @ApiOperation(value = "编辑加工中心信息", notes = "根据加工中心ID编辑加工中心信息")
    public AjaxResult<Void> editWorkCenter(@RequestBody WorkCenterEditDTO param) {
        workCenterService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeWorkCenter")
    @ApiOperation(value = "删除加工中心信息", notes = "根据加工中心ID数组删除加工中心信息")
    public AjaxResult<Void> removeWorkCenter(@RequestBody String[] ids) {
        workCenterService.remove(ids);
        return AjaxResult.success();
    }

    @GetMapping("/getComboBox")
    @ApiOperation(value = "获取加工中心下拉框", notes = "根据车间ID获取加工中心下拉框")
    public AjaxResult<List<ComboBox>> getComboBox(@RequestParam(value = "workshopId",required = false) String workshopId) {
        List<WorkCenter> workCenterList = workCenterService.list(new LambdaQueryWrapper<WorkCenter>()
                .select(WorkCenter::getId, WorkCenter::getName)
                .eq(StrUtil.isNotBlank(workshopId), WorkCenter::getWorkshopId, workshopId)
                .orderByDesc(WorkCenter::getCreateTime));
        List<ComboBox> comboBoxList = workCenterList.stream().map(workCenter -> {
            return ComboBoxUtils.convert(workCenter, WorkCenter::getId, WorkCenter::getName);
        }).collect(Collectors.toList());
        return AjaxResult.success(comboBoxList);
    }

}
