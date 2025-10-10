package com.maxinhai.platform.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.maxinhai.platform.config.JwtConfig;
import com.maxinhai.platform.dto.LoginDTO;
import com.maxinhai.platform.dto.RegisterDTO;
import com.maxinhai.platform.dto.UserAddDTO;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.feign.SystemFeignClient;
import com.maxinhai.platform.handler.message.event.MsgEvent;
import com.maxinhai.platform.handler.message.impl.MsgHandler;
import com.maxinhai.platform.po.LoginLog;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.ClientInfoUtils;
import com.maxinhai.platform.utils.LoginUserContext;
import com.maxinhai.platform.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * 认证控制器，处理登录、注册等请求
 */
@RestController
@RequestMapping("/api/auth")
@Api(tags = "用户验证管理接口")
public class AuthController {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private JwtConfig jwtConfig;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private SystemFeignClient systemFeignClient;

    @Resource
    private MsgHandler msgHandler;

    @Resource
    @Qualifier("cpuIntensiveExecutor")
    private Executor cpuIntensiveExecutor;

    /**
     * 用户注册
     */
    @ApiOperation(value = "用户注册", notes = "用户注册")
    @PostMapping("/register")
    public AjaxResult<String> register(@RequestBody RegisterDTO param) {
        Boolean flag = systemFeignClient.existByAccount(param.getAccount()).getData();
        if (flag) {
            throw new BusinessException("注册失败，账号【" + param.getAccount() + "】已存在！");
        }

        UserAddDTO userAddDTO = new UserAddDTO();
        userAddDTO.setAccount(param.getAccount());
        userAddDTO.setUsername(param.getUsername());
        // 密码加密
        userAddDTO.setPassword(passwordEncoder.encode(param.getPassword()));
        systemFeignClient.addUser(userAddDTO);
        return AjaxResult.success("注册成功!");
    }

    /**
     * 用户登录
     */
    @ApiOperation(value = "用户登录", notes = "用户登录")
    @PostMapping("/login")
    public AjaxResult<String> login(@RequestBody LoginDTO param) {
        // 查找用户
        UserVO data = systemFeignClient.findByAccount(param.getAccount()).getData();
        if(Objects.isNull(data)) {
            throw new BusinessException("账号不存在");
        }

        // 验证密码
        if (!passwordEncoder.matches(param.getPassword(), data.getPassword())) {
            return AjaxResult.fail("用户名或密码错误");
        }

        // 检查用户状态
//        if (user.getStatus() != 1) {
//            return AjaxResult.fail("账号已被禁用");
//        }

        // 生成 Token
        String token = jwtConfig.generateToken(data.getId(), data.getAccount(), data.getUsername());

        // 将 Token 存入 Redis，设置与 Token 相同的过期时间
        redisTemplate.opsForValue().set(
                "auth:token:" + data.getAccount(),
                token,
                jwtConfig.getExpirationDateFromToken(token).getTime() - System.currentTimeMillis(),
                TimeUnit.MILLISECONDS
        );

        // TODO 已经在线程池装饰器复制原有请求上下文，但是还是获取不到，只能在异步任务在前获取当前请求
        HttpServletRequest request = ClientInfoUtils.getRequest(ClientInfoUtils.getRequestAttributes());
        cpuIntensiveExecutor.execute(() -> {
            //构建用户登录信息
            LoginLog loginLog = new LoginLog();
            loginLog.setAccount(data.getAccount());
            loginLog.setUsername(data.getUsername());
            loginLog.setLoginIp(ClientInfoUtils.getIpAddress(request));
            loginLog.setLoginTime(new Date());
            loginLog.setLoginPlatform(ClientInfoUtils.parseUserAgent(request));
            // 发布用户登录事件
            MsgEvent event = new MsgEvent(UUID.fastUUID().toString(), "user_login", loginLog);
            msgHandler.publish(event);
        });
        return AjaxResult.success("登录成功!", token);
    }

    @ApiOperation(value = "退出登录", notes = "退出登录")
    @PostMapping("/logout")
    public AjaxResult<String> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (StrUtil.isNotEmpty(token)) {
            String account = jwtConfig.getAccountFromToken(token);
            // 删除 Redis 中的 Token，使该用户的 Token 失效
            redisTemplate.delete("auth:token:" + account);
        }
        return AjaxResult.success("退出登录成功!");
    }

    /**
     * 踢人下线
     */
    @ApiOperation(value = "踢人下线", notes = "踢人下线")
    @PostMapping("/kick")
    public AjaxResult<String> kickUser(@RequestParam String account) {
        // 检查用户是否存在
        Boolean flag = systemFeignClient.existByAccount(account).getData();
        if (!flag) {
            return AjaxResult.fail("用户不存在");
        }

        // 删除 Redis 中的 Token，使该用户的 Token 失效
        redisTemplate.delete("auth:token:" + account);

        return AjaxResult.success("踢人成功!");
    }

    /**
     * 获取当前登录用户信息
     */
    @ApiOperation(value = "获取当前登录用户信息", notes = "获取当前登录用户信息")
    @GetMapping("/getCurrentUser")
    public AjaxResult<Map<String, String>> getCurrentUser(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        String userId = jwtConfig.getUserIdFromToken(token);

        // 从 登录用户上下文 中获取当前登录用户
        Map<String, String> userData = LoginUserContext.getValue(userId);
        return AjaxResult.success("获取成功!", userData);
    }

}
