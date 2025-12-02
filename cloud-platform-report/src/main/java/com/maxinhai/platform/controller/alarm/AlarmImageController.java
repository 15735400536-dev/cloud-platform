package com.maxinhai.platform.controller.alarm;

import com.maxinhai.platform.dto.alarm.AlarmImageQueryDTO;
import com.maxinhai.platform.service.alarm.AlarmImageService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.alarm.AlarmImageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @ClassName：AlarmImageController
 * @Author: XinHai.Ma
 * @Date: 2025/12/2 21:00
 * @Description: 告警图片管理接口
 */
@RestController
@RequestMapping("/alarmImage")
@Api(tags = "告警图片管理接口")
public class AlarmImageController {

    @Resource
    private AlarmImageService alarmImageService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询告警图片", notes = "分页查询告警图片")
    public AjaxResult<PageResult<AlarmImageVO>> searchByPage(@RequestBody AlarmImageQueryDTO param) {
        return AjaxResult.success(PageResult.convert(alarmImageService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取告警图片", notes = "根据告警图片ID获取详细信息")
    public AjaxResult<AlarmImageVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(alarmImageService.getInfo(id));
    }

    @PostMapping("/removeAlarmImage")
    @ApiOperation(value = "删除告警图片", notes = "根据告警图片ID数组删除告警图片")
    public AjaxResult<Void> removeAlarmImage(@RequestBody String[] ids) {
        alarmImageService.remove(ids);
        return AjaxResult.success();
    }

}
