package com.maxinhai.platform.listener;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.mapper.RoleMapper;
import com.maxinhai.platform.mapper.UserMapper;
import com.maxinhai.platform.po.Role;
import com.maxinhai.platform.po.User;
import com.maxinhai.platform.po.UserRoleRel;
import com.maxinhai.platform.service.CommonCodeCheckService;
import com.maxinhai.platform.service.UserRoleRelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InitDataListener implements CommandLineRunner {

    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private UserRoleRelService userRoleRelService;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private CommonCodeCheckService commonCodeCheckService;

    @Override
    public void run(String... args) throws Exception {
        boolean codeUnique = commonCodeCheckService.isCodeUnique(User.class, User::getAccount, "song");
        log.info("codeUnique: {}", codeUnique);
        codeUnique = commonCodeCheckService.isCodeUnique(Role.class, "role_key", "VIP");
        log.info("codeUnique: {}", codeUnique);
        initUserData();
        repairUserData();
    }

    /**
     * 初始化用户数据
     */
    public void initUserData() {
        Long count = userMapper.selectCount(new QueryWrapper<User>());
        if (count > 0) {
            return;
        }
        String jsonStr = ResourceUtil.readUtf8Str("user.json");
        JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
        JSONArray 四大名著主要人物 = jsonObject.getJSONArray("四大名著主要人物");
        for (Object o : 四大名著主要人物) {
            JSONObject object = (JSONObject) o;
            JSONArray 主要人物 = object.getJSONArray("主要人物");
            for (Object o1 : 主要人物) {
                JSONObject object1 = (JSONObject) o1;
                User user = new User();
                user.setAccount(UUID.fastUUID().toString().replace("-", ""));
                user.setUsername(object1.getStr("name"));
                user.setPassword(passwordEncoder.encode("123456"));
                user.setSex("未知");
                userMapper.insert(user);
            }
        }
    }

    /**
     * 修复用户数据
     */
    public void repairUserData() {
        // 查询未绑定任何角色的用户列表
        List<User> userList = userMapper.findUserListWithoutRole();
        if (CollectionUtils.isEmpty(userList)) {
            return;
        }

        // 查询普通用户角色
        Role role = roleMapper.selectOne(new LambdaQueryWrapper<Role>()
                .select(Role::getId, Role::getRoleKey, Role::getRoleName)
                .eq(Role::getRoleKey, "USER"));
        if (Objects.isNull(role)) {
            throw new BusinessException("未找到【普通用户】角色!");
        }

        // 绑定关系
        List<UserRoleRel> relList = userList.stream().map(user -> {
            return new UserRoleRel(user.getId(), role.getId());
        }).collect(Collectors.toList());
        userRoleRelService.saveBatch(relList);
    }
}
