package com.maxinhai.platform.controller;

import com.maxinhai.platform.service.InspectionTaskService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName：InspectionTaskController
 * @Author: XinHai.Ma
 * @Date: 2025/10/10 15:13
 * @Description: 巡检任务管理接口
 */
@RestController
@RequestMapping("/equip")
@Api(tags = "巡检任务管理接口")
public class InspectionTaskController {

    @Resource
    private InspectionTaskService inspectionTaskService;

}
