package com.maxinhai.platform.controller.habit;

import com.maxinhai.platform.dto.habit.UserHabitCheckinDTO;
import com.maxinhai.platform.dto.habit.UserHabitCheckinQueryDTO;
import com.maxinhai.platform.service.habit.UserHabitCheckinService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.habit.UserHabitCheckinVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("userHabitCheckin")
@Api(tags = "用户习惯打卡管理接口")
@RequiredArgsConstructor
public class UserHabitCheckinController {

    private final UserHabitCheckinService userHabitCheckinService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询用户习惯打卡信息", notes = "根据查询条件分页查询用户习惯打卡信息")
    public AjaxResult<PageResult<UserHabitCheckinVO>> searchByPage(@RequestBody UserHabitCheckinQueryDTO param) {
        return AjaxResult.success(PageResult.convert(userHabitCheckinService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取用户习惯打卡信息", notes = "根据用户习惯打卡ID获取详细信息")
    public AjaxResult<UserHabitCheckinVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(userHabitCheckinService.getInfo(id));
    }

    @PostMapping("/checkin")
    @ApiOperation(value = "用户习惯打卡", notes = "用户习惯打卡")
    public AjaxResult<Void> checkin(@RequestBody UserHabitCheckinDTO param) {
        boolean checkin = userHabitCheckinService.checkin(param);
        return checkin ? AjaxResult.success("打卡成功！") : AjaxResult.fail("打卡失败！");
    }

}
