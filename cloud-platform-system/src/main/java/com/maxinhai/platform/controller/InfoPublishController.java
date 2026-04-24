package com.maxinhai.platform.controller;

import com.maxinhai.platform.dto.InfoPublishDTO;
import com.maxinhai.platform.dto.InfoPublishQueryDTO;
import com.maxinhai.platform.service.InfoPublishService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.InfoPublishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/infoPublish")
@Api(tags = "信息发布管理接口")
@RequiredArgsConstructor
public class InfoPublishController {

    private final InfoPublishService infoPublishService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询信息发布记录", notes = "根据查询条件分页查询信息发布记录")
    public AjaxResult<PageResult<InfoPublishVO>> searchByPage(@RequestBody InfoPublishQueryDTO param) {
        return AjaxResult.success(PageResult.convert(infoPublishService.searchPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取信息发布记录", notes = "根据菜单ID获取详细信息")
    public AjaxResult<InfoPublishVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(infoPublishService.getInfo(id));
    }

    @PostMapping("/addInfo")
    @ApiOperation(value = "添加信息发布记录", notes = "添加信息发布记录")
    public AjaxResult<Void> addInfo(@RequestBody InfoPublishDTO param) {
        infoPublishService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editInfo")
    @ApiOperation(value = "编辑信息发布记录", notes = "根据菜单ID编辑信息发布记录")
    public AjaxResult<Void> editInfo(@RequestBody InfoPublishDTO param) {
        infoPublishService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeInfo")
    @ApiOperation(value = "删除信息发布记录", notes = "根据菜单ID数组删除信息发布记录")
    public AjaxResult<Void> removeInfo(@RequestBody String[] ids) {
        infoPublishService.remove(ids);
        return AjaxResult.success();
    }

    @PostMapping("/uploadFile")
    @ApiOperation(value = "上传文件", notes = "上传文件")
    public AjaxResult<List<Map<String, Object>>> uploadFile(@RequestParam("files") List<MultipartFile> files) {
        List<Map<String, Object>> resultList = infoPublishService.uploadFile(files);
        return AjaxResult.success(resultList);
    }

    @PostMapping("/publish/{id}")
    @ApiOperation(value = "发布信息", notes = "根据信息ID发布信息")
    public AjaxResult<Void> publish(@PathVariable("id") String id) {
        infoPublishService.publish(id);
        return AjaxResult.success();
    }

    @PostMapping("/cancel/{id}")
    @ApiOperation(value = "撤销信息", notes = "根据信息ID撤销信息")
    public AjaxResult<Void> cancel(@PathVariable("id") String id) {
        infoPublishService.cancel(id);
        return AjaxResult.success();
    }

}
