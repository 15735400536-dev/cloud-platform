package com.maxinhai.platform.controller.alarm;

import com.maxinhai.platform.dto.alarm.AlgorithmAddDTO;
import com.maxinhai.platform.dto.alarm.AlgorithmEditDTO;
import com.maxinhai.platform.dto.alarm.AlgorithmQueryDTO;
import com.maxinhai.platform.service.alarm.AlgorithmService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.alarm.AlgorithmVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @ClassName：AlgorithmController
 * @Author: XinHai.Ma
 * @Date: 2025/12/2 21:00
 * @Description: 算法管理接口
 */
@RestController
@RequestMapping("/algorithm")
@Api(tags = "算法管理接口")
public class AlgorithmController {

    @Resource
    private AlgorithmService algorithmService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询算法", notes = "分页查询算法")
    public AjaxResult<PageResult<AlgorithmVO>> searchByPage(@RequestBody AlgorithmQueryDTO param) {
        return AjaxResult.success(PageResult.convert(algorithmService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取算法", notes = "根据算法ID获取详细信息")
    public AjaxResult<AlgorithmVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(algorithmService.getInfo(id));
    }

    @PostMapping("/addAlgorithm")
    @ApiOperation(value = "添加算法", notes = "添加算法")
    public AjaxResult<Void> addAlgorithm(@RequestBody AlgorithmAddDTO param) {
        algorithmService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editAlgorithm")
    @ApiOperation(value = "编辑算法", notes = "根据算法ID编辑算法")
    public AjaxResult<Void> editAlgorithm(@RequestBody AlgorithmEditDTO param) {
        algorithmService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeAlgorithm")
    @ApiOperation(value = "删除算法", notes = "根据算法ID数组删除算法")
    public AjaxResult<Void> removeAlgorithm(@RequestBody String[] ids) {
        algorithmService.remove(ids);
        return AjaxResult.success();
    }

}
