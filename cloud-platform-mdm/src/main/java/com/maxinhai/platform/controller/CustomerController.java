package com.maxinhai.platform.controller;

import com.maxinhai.platform.dto.CustomerAddDTO;
import com.maxinhai.platform.dto.CustomerEditDTO;
import com.maxinhai.platform.dto.CustomerQueryDTO;
import com.maxinhai.platform.service.CustomerService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.CustomerVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RefreshScope
@RestController
@RequestMapping("/customer")
@Api(tags = "客户管理接口")
public class CustomerController {

    @Resource
    private CustomerService customerService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询客户信息", notes = "根据查询条件分页查询客户信息")
    public AjaxResult<PageResult<CustomerVO>> searchByPage(@RequestBody CustomerQueryDTO param) {
        return AjaxResult.success(PageResult.convert(customerService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取客户信息", notes = "根据客户ID获取详细信息")
    public AjaxResult<CustomerVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(customerService.getInfo(id));
    }

    @PostMapping("/addCustomer")
    @ApiOperation(value = "添加客户信息", notes = "添加客户信息")
    public AjaxResult<Void> addOperation(@RequestBody CustomerAddDTO param) {
        customerService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editCustomer")
    @ApiOperation(value = "编辑客户信息", notes = "根据客户ID编辑客户信息")
    public AjaxResult<Void> editOperation(@RequestBody CustomerEditDTO param) {
        customerService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeCustomer")
    @ApiOperation(value = "删除客户信息", notes = "根据客户ID数组删除客户信息")
    public AjaxResult<Void> removeOperation(@RequestBody String[] ids) {
        customerService.remove(ids);
        return AjaxResult.success();
    }

}
