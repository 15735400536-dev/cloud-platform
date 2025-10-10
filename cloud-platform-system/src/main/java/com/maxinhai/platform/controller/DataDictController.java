package com.maxinhai.platform.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.maxinhai.platform.dto.DataDictAddDTO;
import com.maxinhai.platform.dto.DataDictEditDTO;
import com.maxinhai.platform.dto.DataDictQueryDTO;
import com.maxinhai.platform.enums.Status;
import com.maxinhai.platform.po.DataDict;
import com.maxinhai.platform.service.DataDictService;
import com.maxinhai.platform.vo.DataDictVO;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/dataDict")
@Api(tags = "数据字典管理接口")
public class DataDictController {

    @Resource
    private DataDictService dataDictService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询数据字典信息", notes = "根据查询条件分页查询数据字典信息")
    public AjaxResult<PageResult<DataDictVO>> searchByPage(@RequestBody DataDictQueryDTO param) {
        return AjaxResult.success(PageResult.convert(dataDictService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取数据字典信息", notes = "根据数据字典ID获取详细信息")
    public AjaxResult<DataDictVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(dataDictService.getInfo(id));
    }

    @PostMapping("/addDataDict")
    @ApiOperation(value = "添加数据字典信息", notes = "添加数据字典信息")
    public AjaxResult<Void> addDataDict(@RequestBody DataDictAddDTO param) {
        dataDictService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editDataDict")
    @ApiOperation(value = "编辑数据字典信息", notes = "根据数据字典ID编辑数据字典信息")
    public AjaxResult<Void> editDataDict(@RequestBody DataDictEditDTO param) {
        dataDictService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeDataDict")
    @ApiOperation(value = "删除数据字典信息", notes = "根据数据字典ID数组删除数据字典信息")
    public AjaxResult<Void> removeDataDict(@RequestBody String[] ids) {
        dataDictService.remove(ids);
        return AjaxResult.success();
    }

    @GetMapping("/getDataDict/{dictType}")
    @ApiOperation(value = "获取数据字典", notes = "根据字典类型获取数据字典")
    public AjaxResult<List<DataDictVO>> getDataDict(@PathVariable("dictType") String dictType) {
        List<DataDict> dataDictList = dataDictService.list(new LambdaQueryWrapper<DataDict>()
                .select(DataDict::getId, DataDict::getDictType, DataDict::getDictKey, DataDict::getDictValue)
                .eq(DataDict::getDictType, dictType)
                .eq(DataDict::getStatus, Status.Enable)
                .orderByAsc(DataDict::getSort));
        return AjaxResult.success(BeanUtil.copyToList(dataDictList, DataDictVO.class));
    }
}
