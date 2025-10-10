package com.maxinhai.platform.controller;

import com.maxinhai.platform.po.FileStorage;
import com.maxinhai.platform.service.FileStorageService;
import com.maxinhai.platform.utils.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName：FileStorageController
 * @Author: XinHai.Ma
 * @Date: 2025/9/12 10:58
 * @Description: 文件存储管理接口
 */
@RestController
@RequestMapping("/fileStorage")
@Slf4j
@Api(tags = "文件存储管理接口")
public class FileStorageController {

    @Resource
    private FileStorageService fileStorageService;

    @PostMapping("/upload")
    @ApiOperation(value = "上传文件", notes = "支持多种类型文件上传")
    public AjaxResult<FileStorage> uploadFile(
            @ApiParam(value = "上传的文件", required = true) @RequestParam("file") MultipartFile file) {
        FileStorage fileStorage = fileStorageService.uploadFile(file);
        return AjaxResult.success("文件上传成功", fileStorage);
    }

    @GetMapping("/download/{fileId}")
    @ApiOperation(value = "下载文件", notes = "根据文件ID下载文件")
    public AjaxResult<Void> downloadFile(
            @ApiParam(value = "文件ID", required = true) @PathVariable String fileId,
            HttpServletResponse response) {
        fileStorageService.downloadFile(fileId, response);
        return AjaxResult.success();
    }

    @GetMapping("/getFileInfo/{fileId}")
    @ApiOperation(value = "获取文件信息", notes = "根据文件ID获取文件详细信息")
    public AjaxResult<FileStorage> getFileInfo(@ApiParam(value = "文件ID", required = true) @PathVariable String fileId) {
        FileStorage fileStorage = fileStorageService.getFileInfo(fileId);
        boolean flag = Objects.nonNull(fileStorage);
        return AjaxResult.build(flag, flag ? "获取文件信息成功" : "文件不存在或已过期", fileStorage);
    }

    @DeleteMapping("/deleteFile/{fileId}")
    @ApiOperation(value = "删除文件", notes = "根据文件ID删除文件")
    public AjaxResult<Map<String, Object>> deleteFile(@ApiParam(value = "文件ID", required = true) @PathVariable String fileId) {
        boolean deleted = fileStorageService.deleteFile(fileId);
        return AjaxResult.build(deleted, deleted ? "文件删除成功" : "文件删除失败");
    }

}
