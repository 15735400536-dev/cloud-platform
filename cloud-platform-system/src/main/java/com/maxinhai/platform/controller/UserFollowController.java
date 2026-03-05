package com.maxinhai.platform.controller;

import com.maxinhai.platform.dto.follow.CancelDTO;
import com.maxinhai.platform.dto.follow.FollowDTO;
import com.maxinhai.platform.service.UserFollowService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.vo.UserFollowVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/follow")
@RequiredArgsConstructor
@Api(tags = "用户关注列表管理接口")
public class UserFollowController {

    private final UserFollowService userFollowService;

    @ApiOperation(value = "获取关注列表", notes = "根据用户ID查询关注列表")
    @GetMapping("/getFollowList/{userId}")
    public AjaxResult<List<UserFollowVO>> getFollowList(@PathVariable("userId") String userId) {
        return AjaxResult.success(userFollowService.getFollowListByUserId(userId));
    }

    @ApiOperation(value = "获取关注信息", notes = "根据用户ID和关注用户ID查询关注信息")
    @GetMapping("/getFollow/{userId}/{followId}")
    public AjaxResult<Boolean> getFollowList(@PathVariable("userId") String userId,
                                             @PathVariable("followId") String followId) {
        return AjaxResult.success(userFollowService.getFollowByUserIdAndFollowId(userId, followId));
    }

    @ApiOperation(value = "关注用户", notes = "根据用户ID和关注用户ID关注用户")
    @PostMapping("/follow")
    public AjaxResult<Boolean> follow(@RequestBody FollowDTO param) {
        return AjaxResult.success(true);
    }

    @ApiOperation(value = "取消关注用户", notes = "根据用户ID和关注用户ID取消关注用户")
    @PostMapping("/cancel")
    public AjaxResult<Boolean> cancel(@RequestBody CancelDTO param) {
        return AjaxResult.success(true);
    }

}
