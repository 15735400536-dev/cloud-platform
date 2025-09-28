package com.maxinhai.platform.controller;

import com.maxinhai.platform.dto.DeptAddDTO;
import com.maxinhai.platform.dto.DeptEditDTO;
import com.maxinhai.platform.dto.DeptQueryDTO;
import com.maxinhai.platform.dto.DeptUserDTO;
import com.maxinhai.platform.service.DeptService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.DeptTreeVO;
import com.maxinhai.platform.vo.DeptVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/dept")
@Api(tags = "部门管理接口")
public class DeptController {

    @Resource
    private DeptService deptService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询部门信息", notes = "根据查询条件分页查询部门信息")
    public AjaxResult<PageResult<DeptVO>> searchByPage(@RequestBody DeptQueryDTO param) {
        return AjaxResult.success(PageResult.convert(deptService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取部门信息", notes = "根据部门ID获取详细信息")
    public AjaxResult<DeptVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(deptService.getInfo(id));
    }

    @PostMapping("/addDept")
    @ApiOperation(value = "添加部门信息", notes = "添加部门信息")
    public AjaxResult<Void> addDept(@RequestBody DeptAddDTO param) {
        deptService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editDept")
    @ApiOperation(value = "编辑部门信息", notes = "根据部门ID编辑部门信息")
    public AjaxResult<Void> editDept(@RequestBody DeptEditDTO param) {
        deptService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeDept")
    @ApiOperation(value = "删除部门信息", notes = "根据部门ID数组删除部门信息")
    public AjaxResult<Void> removeDept(@RequestBody String[] ids) {
        deptService.remove(ids);
        return AjaxResult.success();
    }

    @PostMapping("/binding")
    @ApiOperation(value = "部门绑定用户", notes = "根据部门ID、用户ID集合，部门绑定用户")
    public AjaxResult<Void> binding(@RequestBody DeptUserDTO param) {
        deptService.binding(param);
        return AjaxResult.success();
    }

    @GetMapping("/getTree}")
    @ApiOperation(value = "获取部门树状结构", notes = "获取部门树状结构")
    public AjaxResult<List<DeptTreeVO>> getTree() {
        return AjaxResult.success(deptService.getTree());
    }
}
