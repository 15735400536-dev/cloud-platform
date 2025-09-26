package com.maxinhai.platform.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.maxinhai.platform.dto.MenuAddDTO;
import com.maxinhai.platform.dto.MenuEditDTO;
import com.maxinhai.platform.dto.MenuQueryDTO;
import com.maxinhai.platform.excel.MenuExcel;
import com.maxinhai.platform.po.Menu;
import com.maxinhai.platform.service.MenuService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.EasyExcelUtils;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.MenuTreeVO;
import com.maxinhai.platform.vo.MenuVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/menu")
@Api(tags = "菜单管理接口")
public class MenuController {

    @Resource
    private MenuService menuService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询菜单信息", notes = "根据查询条件分页查询菜单信息")
    public AjaxResult<PageResult<MenuVO>> searchByPage(@RequestBody MenuQueryDTO page) {
        return AjaxResult.success(PageResult.convert(menuService.searchByPage(page)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取菜单信息", notes = "根据菜单ID获取详细信息")
    public AjaxResult<MenuVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(menuService.getInfo(id));
    }

    @PostMapping("/addMenu")
    @ApiOperation(value = "添加菜单信息", notes = "添加菜单信息")
    public AjaxResult<Void> addMenu(@RequestBody MenuAddDTO param) {
        menuService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editMenu")
    @ApiOperation(value = "编辑菜单信息", notes = "根据菜单ID编辑菜单信息")
    public AjaxResult<Void> editMenu(@RequestBody MenuEditDTO param) {
        menuService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeMenu")
    @ApiOperation(value = "删除菜单信息", notes = "根据菜单ID数组删除菜单信息")
    public AjaxResult<Void> removeMenu(@RequestBody String[] ids) {
        menuService.remove(ids);
        return AjaxResult.success();
    }

    @GetMapping("/export")
    @ApiOperation(value = "导出角色excel", notes = "导出角色excel")
    public void exportRoles(HttpServletResponse response) throws IOException {
        // 1.查询菜单数据
        List<Menu> menuList = menuService.list(new LambdaQueryWrapper<Menu>().select(Menu::getId, Menu::getParentId,
                Menu::getMenuName, Menu::getMenuType, Menu::getComponent, Menu::getUrl, Menu::getIcon, Menu::getSort,
                Menu::getStatus, Menu::getIsFrame));
        // 2.导出excel
        EasyExcelUtils.export(
                response,
                "菜单数据表",
                "菜单列表",
                MenuExcel.class,
                BeanUtil.copyToList(menuList, MenuExcel.class)
        );
    }

    @GetMapping("/getMenuTree}")
    @ApiOperation(value = "获取菜单树状结构", notes = "获取菜单树状结构")
    public AjaxResult<List<MenuTreeVO>> getMenuTree() {
        return AjaxResult.success(menuService.getMenuTree());
    }

    @PostMapping("/importExcel")
    @ApiOperation(value = "导入菜单数据", notes = "根据Excel模板导入菜单数据")
    public AjaxResult<String> importExcel(MultipartFile file) {
        // 验证文件是否为空
        if (Objects.isNull(file) || file.isEmpty()) {
            return AjaxResult.fail("请选择要上传的Excel文件！");
        }

        // 验证文件格式
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
            return AjaxResult.fail("请上传Excel格式的文件（.xlsx或.xls）");
        }

        menuService.importExcel(file);
        return AjaxResult.success("导入成功!");
    }
}
