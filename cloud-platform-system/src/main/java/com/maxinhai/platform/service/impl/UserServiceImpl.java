package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.UserAddDTO;
import com.maxinhai.platform.dto.UserEditDTO;
import com.maxinhai.platform.dto.UserQueryDTO;
import com.maxinhai.platform.dto.UserRoleDTO;
import com.maxinhai.platform.excel.UserExcel;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.listener.UserExcelListener;
import com.maxinhai.platform.mapper.RoleMapper;
import com.maxinhai.platform.mapper.UserMapper;
import com.maxinhai.platform.mapper.UserRoleRelMapper;
import com.maxinhai.platform.po.Role;
import com.maxinhai.platform.po.User;
import com.maxinhai.platform.po.UserRoleRel;
import com.maxinhai.platform.service.UserRoleRelService;
import com.maxinhai.platform.service.UserService;
import com.maxinhai.platform.vo.RoleVO;
import com.maxinhai.platform.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private UserRoleRelMapper userRoleRelMapper;
    @Resource
    private UserRoleRelService userRoleRelService;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private UserExcelListener userExcelListener;

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
        Long count = userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getAccount, param.getAccount()));
        if (count > 0) {
            throw new BusinessException("账号【" + param.getAccount() + "】已注册!");
        }

        Role role = roleMapper.selectOne(new LambdaQueryWrapper<Role>()
                .select(Role::getId, Role::getRoleKey, Role::getRoleName)
                .eq(Role::getRoleKey, "USER"));
        if (Objects.isNull(role)) {
            throw new BusinessException("未找到【普通用户】角色!");
        }

        User user = BeanUtil.toBean(param, User.class);
        user.setPassword(passwordEncoder.encode(param.getPassword()));
        userMapper.insert(user);

        UserRoleRel userRoleRel = new UserRoleRel(user.getId(), role.getId());
        userRoleRelMapper.insert(userRoleRel);
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

    @Override
    public void importExcel(MultipartFile file) {
        try {
            // 调用EasyExcel读取文件
            EasyExcel.read(file.getInputStream(), UserExcel.class, userExcelListener)
                    .sheet() // 读取第一个sheet
                    .doRead(); // 执行读取操作
        } catch (IOException e) {
            log.error("Excel数据导入失败", e);
            throw new BusinessException("Excel数据导入失败：" + e.getMessage());
        }
    }
}
