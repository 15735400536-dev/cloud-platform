package com.maxinhai.platform.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.LoginDTO;
import com.maxinhai.platform.mapper.UserMapper;
import com.maxinhai.platform.mapper.UserRoleRelMapper;
import com.maxinhai.platform.po.Role;
import com.maxinhai.platform.po.User;
import com.maxinhai.platform.po.UserRoleRel;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.LoginUserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
@Api(tags = "用户登录接口")
public class LoginController {

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserRoleRelMapper userRoleRelMapper;

    @GetMapping("/register")
    @ApiOperation(value = "注册账户", notes = "注册账户")
    public AjaxResult<String> register(@RequestParam("account") String account,
                                       @RequestParam("nickname") String nickname,
                                       @RequestParam("password") String password) {
        Long count = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getAccount, account));
        if (count > 0) {
            throw new RuntimeException("账号已存在!");
        }
        User user = new User();
        user.setAccount(account);
        user.setUsername(nickname);
        user.setPassword(password);
        userMapper.insert(user);
        return AjaxResult.success("register success");
    }

    @GetMapping("/login")
    @ApiOperation(value = "登录账户", notes = "登录账户")
    public AjaxResult<String> login(@RequestParam("account") String account,
                                    @RequestParam("password") String password) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getAccount, account));
        if(user == null){
            throw new RuntimeException("账号不存在!");
        }
        if(!user.getPassword().equals(password)){
            throw new RuntimeException("密码错误!");
        }
        // 调用Sa-Token登录方法
        StpUtil.login(user.getId());
        LoginUserContext.setItemKey("account", user.getAccount());
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        return AjaxResult.success("login success", tokenInfo.getTokenValue());
    }

    @PostMapping("/safeLogin")
    @ApiOperation(value = "安全登录账户", notes = "安全登录账户")
    public AjaxResult<String> safeLogin(@Valid @RequestBody LoginDTO param) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getAccount, param.getAccount()));
        if(user == null){
            throw new RuntimeException("账号不存在!");
        }
        if(!user.getPassword().equals(param.getPassword())){
            throw new RuntimeException("密码错误!");
        }
        StpUtil.login(user.getId());
        LoginUserContext.setItemKey("account", user.getAccount());
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        return AjaxResult.success("login success", tokenInfo.getTokenValue());
    }

    @GetMapping("/logout")
    @ApiOperation(value = "退出登录", notes = "退出登录")
    public AjaxResult<String> logout() {
        // 检测是否登录
        if(StpUtil.isLogin()) {
            LoginUserContext.remove();
            // 退出登录
            StpUtil.logout();
        }
        return AjaxResult.success("logout success");
    }

    @GetMapping("/isLogin")
    @ApiOperation(value = "获取当前会话是否已经登录", notes = "获取当前会话是否已经登录")
    public AjaxResult<Boolean> isLogin() {
        return AjaxResult.success(StpUtil.isLogin());
    }

    @GetMapping("getTokenInfo")
    @ApiOperation(value = "获取Token信息", notes = "获取Token信息")
    public SaResult getTokenInfo() {
        return SaResult.data(StpUtil.getTokenInfo());
    }

    @GetMapping("kickout/{userId}")
    @ApiOperation(value = "踢人下线", notes = "根据用户ID将指定账号踢下线")
    public AjaxResult<String> kickout(@PathVariable("userId") String userId) {
        StpUtil.kickout(userId);
        return AjaxResult.success("kickout success");
    }

    @GetMapping("replaced/{userId}")
    @ApiOperation(value = "顶人下线", notes = "根据用户ID将指定账号顶下线")
    public AjaxResult<String> replaced(@PathVariable("userId") String userId) {
        StpUtil.replaced(userId);
        return AjaxResult.success("kickout success");
    }

    @GetMapping("getLoginUser")
    @ApiOperation(value = "获取当前登录用户信息", notes = "获取当前登录用户信息")
    public AjaxResult<User> getLoginUser() {
        String userId = StpUtil.getLoginIdAsString();
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getId, User::getAccount, User::getUsername, User::getEmail, User::getPhone)
                .eq(User::getId, userId));
        List<Role> roleList = userRoleRelMapper.selectJoinList(Role.class, new MPJLambdaWrapper<UserRoleRel>()
                .innerJoin(User.class, User::getId, UserRoleRel::getUserId)
                .innerJoin(Role.class, Role::getId, UserRoleRel::getRoleId)
                // 查询条件
                .eq(UserRoleRel::getUserId, userId)
                // 字段别名
                .selectAll(Role.class));
        return AjaxResult.success();
    }

}
