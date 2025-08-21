package com.maxinhai.platform.controller;

import com.maxinhai.platform.service.UploadFileService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName：UploadFileController
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 14:20
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@RestController
@RequestMapping("/file")
@Api(tags = "上传文件接口")
public class UploadFileController {

    @Resource
    private UploadFileService uploadFileService;

}
