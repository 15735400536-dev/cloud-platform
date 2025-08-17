package com.maxinhai.platform.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.maxinhai.platform.dto.RoleAddDTO;
import com.maxinhai.platform.dto.RoleEditDTO;
import com.maxinhai.platform.dto.RoleMenuDTO;
import com.maxinhai.platform.dto.RoleQueryDTO;
import com.maxinhai.platform.po.ComboBox;
import com.maxinhai.platform.po.Role;
import com.maxinhai.platform.excel.RoleExcel;
import com.maxinhai.platform.service.RoleService;
import com.maxinhai.platform.utils.ComboBoxUtils;
import com.maxinhai.platform.vo.RoleVO;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.EasyExcelUtils;
import com.maxinhai.platform.utils.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/role")
@Api(tags = "角色管理接口")
public class RoleController {

    @Resource
    private RoleService roleService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询角色信息", notes = "根据查询条件分页查询角色信息")
    public AjaxResult<RoleVO> searchByPage(@RequestBody RoleQueryDTO param) {
        return AjaxResult.success(PageResult.convert(roleService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取角色信息", notes = "根据角色ID获取详细信息")
    public AjaxResult<RoleVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(roleService.getInfo(id));
    }

    @PostMapping("/addRole")
    @ApiOperation(value = "添加角色信息", notes = "添加角色信息")
    public AjaxResult<Void> addRole(@RequestBody RoleAddDTO param) {
        roleService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editRole")
    @ApiOperation(value = "编辑角色信息", notes = "根据角色ID编辑角色信息")
    public AjaxResult<Void> editRole(@RequestBody RoleEditDTO param) {
        roleService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeRole")
    @ApiOperation(value = "删除角色信息", notes = "根据角色ID数组删除角色信息")
    public AjaxResult<Void> removeRole(@RequestBody String[] ids) {
        roleService.remove(ids);
        return AjaxResult.success();
    }

    @PostMapping("/binding")
    @ApiOperation(value = "角色绑定菜单", notes = "根据角色ID、菜单ID集合，角色绑定菜单")
    public AjaxResult<Void> binding(@RequestBody RoleMenuDTO param) {
        roleService.binding(param);
        return AjaxResult.success();
    }

    @PostMapping("/getRoleMap")
    @ApiOperation(value = "获取角色列表", notes = "获取角色列表")
    public AjaxResult<List<Map<String, String>>> getRoleMap() {
        List<Map<String, String>> resultList = roleService.list(new LambdaQueryWrapper<Role>()
                        .select(Role::getId, Role::getRoleName)
                        .orderByDesc(Role::getCreateTime)).stream()
                .map(role -> Map.of("id", role.getId(), "name", role.getRoleName()))
                .collect(Collectors.toList());
        return AjaxResult.success(resultList);
    }

    private static boolean flag = false;

    @PostMapping("/getRoleComboBox")
    @ApiOperation(value = "获取角色下拉框", notes = "获取角色下拉框")
    public AjaxResult<List<ComboBox>> getRoleComboBox() {
        List<ComboBox> comboBoxList = roleService.list(new LambdaQueryWrapper<Role>()
                        .select(Role::getId, Role::getRoleName)
                        .orderByDesc(Role::getCreateTime)).stream()
                .map(role -> {
                    if(flag) {
                        return ComboBoxUtils.convert(role, "id", "roleName");
                    } else {
                        return ComboBoxUtils.convert(role, Role::getId, Role::getRoleName);
                    }
                })
                .collect(Collectors.toList());
        flag = !flag;
        return AjaxResult.success(comboBoxList);
    }

    @GetMapping("/export")
    @ApiOperation(value = "导出角色excel", notes = "导出角色excel")
    public void exportRoles(HttpServletResponse response) throws IOException {
        // 1.查询角色数据
        List<Role> roleList = roleService.list(new LambdaQueryWrapper<Role>().select(Role::getRoleKey, Role::getRoleName, Role::getRoleDesc));
        // 2.导出excel
        EasyExcelUtils.export(
                response,
                "角色数据表",
                "角色列表",
                RoleExcel.class,
                BeanUtil.copyToList(roleList, RoleExcel.class)
        );
    }
}
