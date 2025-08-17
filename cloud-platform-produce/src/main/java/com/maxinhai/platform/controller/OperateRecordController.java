package com.maxinhai.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxinhai.platform.dto.OperateRecordQueryDTO;
import com.maxinhai.platform.service.OperateRecordService;
import com.maxinhai.platform.vo.OperateRecordVO;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("/operateRecord")
@Api(tags = "派工单操作记录管理接口")
public class OperateRecordController {

    @Resource
    private OperateRecordService operateRecordService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询派工单操作记录信息", notes = "根据查询条件分页查询派工单操作记录信息")
    public AjaxResult<Page<OperateRecordVO>> searchByPage(@RequestBody OperateRecordQueryDTO param) {
        return AjaxResult.success(PageResult.convert(operateRecordService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取派工单操作记录信息", notes = "根据派工单操作记录ID获取详细信息")
    public AjaxResult<OperateRecordVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(operateRecordService.getInfo(id));
    }

    @PostMapping("/removeOperateRecord")
    @ApiOperation(value = "删除派工单操作记录信息", notes = "根据派工单操作记录ID数组删除派工单操作记录信息")
    public AjaxResult<Void> removeOperateRecord(@RequestBody String[] ids) {
        operateRecordService.remove(ids);
        return AjaxResult.success();
    }

}
