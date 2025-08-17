package com.maxinhai.platform.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maxinhai.platform.dto.UserAddDTO;
import com.maxinhai.platform.dto.UserEditDTO;
import com.maxinhai.platform.dto.UserQueryDTO;
import com.maxinhai.platform.dto.UserRoleDTO;
import com.maxinhai.platform.excel.UserExcel;
import com.maxinhai.platform.po.User;
import com.maxinhai.platform.service.UserService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.EasyExcelUtils;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.RoleVO;
import com.maxinhai.platform.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Api(tags = "用户管理接口")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询用户信息", notes = "根据查询条件分页查询用户信息")
    public AjaxResult<Page<UserVO>> searchByPage(@RequestBody UserQueryDTO param) {
        return AjaxResult.success(PageResult.convert(userService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取用户信息", notes = "根据用户ID获取详细信息")
    public AjaxResult<UserVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(userService.getInfo(id));
    }

    @PostMapping("/addUser")
    @ApiOperation(value = "添加用户信息", notes = "添加用户信息")
    public AjaxResult<Void> addUser(@RequestBody UserAddDTO param) {
        userService.add(param);
        return AjaxResult.success();
    }

    @PostMapping("/editUser")
    @ApiOperation(value = "编辑用户信息", notes = "根据用户ID编辑用户信息")
    public AjaxResult<Void> editUser(@RequestBody UserEditDTO param) {
        userService.edit(param);
        return AjaxResult.success();
    }

    @PostMapping("/removeUser")
    @ApiOperation(value = "删除用户信息", notes = "根据用户ID数组删除用户信息")
    public AjaxResult<Void> removeUser(@RequestBody String[] ids) {
        userService.remove(ids);
        return AjaxResult.success();
    }

    @PostMapping("/binding")
    @ApiOperation(value = "用户绑定角色", notes = "根据用户ID、角色ID集合，用户绑定角色")
    public AjaxResult<Void> binding(@RequestBody UserRoleDTO param) {
        userService.binding(param);
        return AjaxResult.success();
    }

    @GetMapping("/export")
    @ApiOperation(value = "导出用户excel", notes = "导出用户excel")
    public void exportUsers(HttpServletResponse response) throws IOException {
        // 1.查询用户数据
        List<User> userList = userService.list(new LambdaQueryWrapper<User>()
                .select(User::getAccount, User::getUsername, User::getPassword, User::getSex, User::getPhone, User::getEmail));
        // 2.导出excel
        EasyExcelUtils.export(
                response,
                "用户数据表",
                "用户列表",
                UserExcel.class,
                BeanUtil.copyToList(userList, UserExcel.class)
        );
    }

    @GetMapping("/getRoles/{userId}")
    @ApiOperation(value = "根据userId查询绑定角色", notes = "根据userId查询绑定角色")
    public AjaxResult<RoleVO> getRoles(@PathVariable("userId") String userId) {
        return AjaxResult.success(userService.getRoles(userId));
    }

    @Resource
    private JdbcTemplate jdbcTemplate;

    //@Scheduled(initialDelay = 5000, fixedDelay = 60000)
    //@Scheduled(cron = "0 30 22 * * ?")
    public void initUserData() throws InterruptedException {
        Map<String, Date> dateMap = jdbcTemplate.queryForObject("SELECT " +
                "  MAX(create_time AT TIME ZONE 'Asia/Shanghai') AS max_time, " +
                "  MIN(create_time AT TIME ZONE 'Asia/Shanghai') AS min_time " +
                "FROM sys_user", (rs, rowNum) -> {
            HashMap<String, Date> result = new HashMap<>();
            result.put("maxTime", rs.getDate("max_time"));
            result.put("minTime", rs.getDate("min_time"));
            return result;
        });

        DateRange range = new DateRange(DateTime.of(dateMap.get("minTime")), DateTime.of(dateMap.get("maxTime")), DateField.DAY_OF_YEAR);
        for (DateTime dateTime : range) {
            System.out.println(DateUtil.format(dateTime, DatePattern.NORM_DATETIME_PATTERN));
            DateTime beginTime = DateUtil.beginOfDay(dateTime);
            DateTime endTime = DateUtil.endOfDay(dateTime);
            List<User> userList = jdbcTemplate.query("SELECT DISTINCT ON (nickname) nickname, account " +
                            "FROM sys_user " +
                            "WHERE create_time >= ? " +
                            "AND create_time <= ? " +
                            "ORDER BY nickname, create_time DESC",
                    new Object[]{ beginTime, endTime },
                    (rs, rowNum) -> {
                        User user = new User();
                        user.setAccount(rs.getString("account"));
                        user.setUsername(rs.getString("nickname"));
                        user.setPassword("123456");
                        return user;
                    });
            userService.saveBatch(userList);
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
