package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.RoleAddDTO;
import com.maxinhai.platform.dto.RoleEditDTO;
import com.maxinhai.platform.dto.RoleMenuDTO;
import com.maxinhai.platform.dto.RoleQueryDTO;
import com.maxinhai.platform.excel.RoleExcel;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.listener.RoleExcelListener;
import com.maxinhai.platform.mapper.RoleMapper;
import com.maxinhai.platform.po.Role;
import com.maxinhai.platform.po.RoleMenuRel;
import com.maxinhai.platform.service.CommonCodeCheckService;
import com.maxinhai.platform.service.RoleMenuRelService;
import com.maxinhai.platform.service.RoleService;
import com.maxinhai.platform.vo.RoleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    private RoleMapper roleMapper;
    @Resource
    private RoleMenuRelService roleMenuRelService;
    @Resource
    private RoleExcelListener roleExcelListener;
    @Resource
    private CommonCodeCheckService commonCodeCheckService;

    @Override
    public Page<RoleVO> searchByPage(RoleQueryDTO param) {
        return roleMapper.selectJoinPage(param.getPage(), RoleVO.class,
                new MPJLambdaWrapper<Role>()
                        .like(StrUtil.isNotBlank(param.getRoleKey()), Role::getRoleKey, param.getRoleKey())
                        .like(StrUtil.isNotBlank(param.getRoleName()), Role::getRoleName, param.getRoleName())
                        .orderByDesc(Role::getCreateTime));
    }

    @Override
    @Cacheable(value = "role", key = "#id", unless = "#result == null") // 根据ID查询用户（缓存：查缓存→无则查库→存缓存）
    public RoleVO getInfo(String id) {
        return roleMapper.selectJoinOne(RoleVO.class, new MPJLambdaWrapper<Role>().eq(Role::getId, id));
    }

    @Override
    @CacheEvict(value = "role", allEntries = true) // 批量删除用户，清除"user"缓存下的所有数据
    public void remove(String[] ids) {
        roleMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    @CachePut(value = "role", key = "#param.id") // 更新用户（缓存：更新后覆盖旧缓存）
    public Role edit(RoleEditDTO param) {
        Role role = BeanUtil.toBean(param, Role.class);
        roleMapper.updateById(role);
        return role;
    }

    @Override
    @CachePut(value = "role", key = "#result.id") // 新增用户（缓存：新增后将结果存入缓存）
    public Role add(RoleAddDTO param) {
        boolean unique = commonCodeCheckService.isCodeUnique(Role.class, Role::getRoleKey, param.getRoleKey());
        if (unique) {
            throw new BusinessException("角色【" + param.getRoleKey() + "】已存在!");
        }
        Role role = BeanUtil.toBean(param, Role.class);
        roleMapper.insert(role);
        return role;
    }

    @Override
    @Cacheable(value = "role", key = "'all'") // 查询所有用户（缓存：key固定为"all"）
    public List<Role> getRoleList() {
        return this.list();
    }

    @Override
    public void binding(RoleMenuDTO param) {
        // 删除旧的数据
        roleMenuRelService.remove(new LambdaQueryWrapper<RoleMenuRel>().eq(RoleMenuRel::getRoleId, param.getRoleId()));
        // 重新绑定
        List<RoleMenuRel> relList = param.getMenuIds().stream()
                .map(menuId -> new RoleMenuRel(param.getRoleId(), menuId))
                .collect(Collectors.toCollection(() ->
                        new ArrayList<>(param.getMenuIds().size())
                ));
        roleMenuRelService.saveBatch(relList);
    }

    @Override
    public void importExcel(MultipartFile file) {
        try {
            // 调用EasyExcel读取文件
            EasyExcel.read(file.getInputStream(), RoleExcel.class, roleExcelListener)
                    .sheet() // 读取第一个sheet
                    .doRead(); // 执行读取操作
        } catch (IOException e) {
            log.error("Excel数据导入失败", e);
            throw new BusinessException("Excel数据导入失败：" + e.getMessage());
        }
    }
}
