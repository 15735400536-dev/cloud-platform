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
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.po.Role;
import com.maxinhai.platform.po.RoleMenuRel;
import com.maxinhai.platform.mapper.RoleMapper;
import com.maxinhai.platform.service.CommonCodeCheckService;
import com.maxinhai.platform.service.RoleMenuRelService;
import com.maxinhai.platform.service.RoleService;
import com.maxinhai.platform.vo.RoleVO;
import lombok.extern.slf4j.Slf4j;
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
        Page<RoleVO> pageResult = roleMapper.selectJoinPage(param.getPage(), RoleVO.class,
                new MPJLambdaWrapper<Role>()
                        .like(StrUtil.isNotBlank(param.getRoleKey()), Role::getRoleKey, param.getRoleKey())
                        .like(StrUtil.isNotBlank(param.getRoleName()), Role::getRoleName, param.getRoleName())
                        .orderByDesc(Role::getCreateTime));
        return pageResult;
    }

    @Override
    public RoleVO getInfo(String id) {
        return roleMapper.selectJoinOne(RoleVO.class, new MPJLambdaWrapper<Role>().eq(Role::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        roleMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(RoleEditDTO param) {
        Role user = BeanUtil.toBean(param, Role.class);
        roleMapper.updateById(user);
    }

    @Override
    public void add(RoleAddDTO param) {
        boolean unique = commonCodeCheckService.isCodeUnique(Role.class, Role::getRoleKey, param.getRoleKey());
        if (unique) {
            throw new BusinessException("角色【" + param.getRoleKey() + "】已存在!");
        }
        Role user = BeanUtil.toBean(param, Role.class);
        roleMapper.insert(user);
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
