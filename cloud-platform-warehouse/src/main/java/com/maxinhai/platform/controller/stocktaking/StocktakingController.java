package com.maxinhai.platform.controller.stocktaking;

import com.maxinhai.platform.dto.stocktaking.StocktakingAddDTO;
import com.maxinhai.platform.dto.stocktaking.StocktakingEditDTO;
import com.maxinhai.platform.dto.stocktaking.StocktakingQueryDTO;
import com.maxinhai.platform.service.stocktaking.StocktakingService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.stocktaking.StocktakingVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/stocktaking")
@Api(tags = "盘点单管理接口")
public class StocktakingController {

    @Resource
    private StocktakingService stocktakingService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询盘点单信息", notes = "根据查询条件分页查询盘点单信息")
    public AjaxResult<PageResult<StocktakingVO>> searchByPage(@RequestBody StocktakingQueryDTO param) {
        return AjaxResult.success(PageResult.convert(stocktakingService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取盘点单信息", notes = "根据盘点单ID获取详细信息")
    public AjaxResult<StocktakingVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(stocktakingService.getInfo(id));
    }

    @PostMapping("/addStocktaking")
    @ApiOperation(value = "添加盘点单信息", notes = "添加盘点单信息")
    public AjaxResult<Void> addStocktaking(@RequestBody StocktakingAddDTO param) {
        stocktakingService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editStocktaking")
    @ApiOperation(value = "编辑盘点单信息", notes = "根据盘点单ID编辑盘点单信息")
    public AjaxResult<Void> editStocktaking(@RequestBody StocktakingEditDTO param) {
        stocktakingService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeStocktaking")
    @ApiOperation(value = "删除盘点单信息", notes = "根据盘点单ID数组删除盘点单信息")
    public AjaxResult<Void> removeStocktaking(@RequestBody String[] ids) {
        stocktakingService.remove(ids);
        return AjaxResult.success();
    }

}
