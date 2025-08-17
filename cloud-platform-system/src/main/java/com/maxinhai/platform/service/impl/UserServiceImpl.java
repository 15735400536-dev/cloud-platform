package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.UserAddDTO;
import com.maxinhai.platform.dto.UserEditDTO;
import com.maxinhai.platform.dto.UserQueryDTO;
import com.maxinhai.platform.dto.UserRoleDTO;
import com.maxinhai.platform.mapper.UserRoleRelMapper;
import com.maxinhai.platform.po.Role;
import com.maxinhai.platform.po.User;
import com.maxinhai.platform.po.UserRoleRel;
import com.maxinhai.platform.mapper.UserMapper;
import com.maxinhai.platform.service.UserRoleRelService;
import com.maxinhai.platform.service.UserService;
import com.maxinhai.platform.vo.RoleVO;
import com.maxinhai.platform.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserRoleRelMapper userRoleRelMapper;
    @Resource
    private UserRoleRelService userRoleRelService;

    @Override
    public Page<UserVO> searchByPage(UserQueryDTO param) {
        Page<UserVO> pageResult = userMapper.selectJoinPage(param.getPage(), UserVO.class,
                new MPJLambdaWrapper<User>()
                .like(StrUtil.isNotBlank(param.getAccount()), User::getAccount, param.getAccount())
                .like(StrUtil.isNotBlank(param.getUsername()), User::getUsername, param.getUsername())
                .orderByDesc(User::getCreateTime));
        return pageResult;
    }

    @Override
    public UserVO getInfo(String id) {
        return userMapper.selectJoinOne(UserVO.class, new MPJLambdaWrapper<User>().eq(User::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        userMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(UserEditDTO param) {
        User user = BeanUtil.toBean(param, User.class);
        userMapper.updateById(user);
    }

    @Override
    public void add(UserAddDTO param) {
        User user = BeanUtil.toBean(param, User.class);
        userMapper.insert(user);
    }

    @Override
    public void binding(UserRoleDTO param) {
        // 删除旧的数据
        userRoleRelService.remove(new LambdaQueryWrapper<UserRoleRel>().eq(UserRoleRel::getUserId, param.getUserId()));
        // 重新绑定
        List<UserRoleRel> relList = param.getRoleIds().stream()
                .map(roleId -> new UserRoleRel(param.getUserId(), roleId))
                .collect(Collectors.toCollection(() ->
                        new ArrayList<>(param.getRoleIds().size())
                ));
        userRoleRelService.saveBatch(relList);
    }

    @Override
    public List<RoleVO> getRoles(String userId) {
        List<RoleVO> roleList = userRoleRelMapper.selectJoinList(RoleVO.class, new MPJLambdaWrapper<UserRoleRel>()
                .innerJoin(User.class, User::getId, UserRoleRel::getUserId)
                .innerJoin(Role.class, Role::getId, UserRoleRel::getRoleId)
                // 查询条件
                .eq(UserRoleRel::getUserId, userId)
                // 字段别名
                .selectAs(Role::getId, RoleVO::getId)
                .selectAs(Role::getRoleKey, RoleVO::getRoleKey)
                .selectAs(Role::getRoleName, RoleVO::getRoleName)
                .selectAs(Role::getRoleDesc, RoleVO::getRoleDesc));
        return roleList;
    }
}
