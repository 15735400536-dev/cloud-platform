package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.*;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.mapper.DeptMapper;
import com.maxinhai.platform.po.Dept;
import com.maxinhai.platform.po.DeptUserRel;
import com.maxinhai.platform.po.User;
import com.maxinhai.platform.service.CommonCodeCheckService;
import com.maxinhai.platform.service.DeptService;
import com.maxinhai.platform.service.DeptUserRelService;
import com.maxinhai.platform.utils.TreeNodeUtils;
import com.maxinhai.platform.vo.DeptTreeVO;
import com.maxinhai.platform.vo.DeptVO;
import com.maxinhai.platform.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName：DeptServiceImpl
 * @Author: XinHai.Ma
 * @Date: 2025/9/28 14:42
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Slf4j
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

    @Resource
    private DeptMapper deptMapper;
    @Resource
    private DeptUserRelService deptUserRelService;
    @Resource
    private CommonCodeCheckService commonCodeCheckService;

    @Override
    public Page<DeptVO> searchByPage(DeptQueryDTO param) {
        Page<DeptVO> pageResult = deptMapper.selectJoinPage(param.getPage(), DeptVO.class,
                new MPJLambdaWrapper<Dept>()
                        .innerJoin(User.class, User::getId, Dept::getLeaderId)
                        .like(StrUtil.isNotBlank(param.getCode()), Dept::getCode, param.getCode())
                        .like(StrUtil.isNotBlank(param.getName()), Dept::getName, param.getName())
                        .selectAll(Dept.class)
                        .selectAs(User::getUsername, DeptVO::getLeaderName)
                        .orderByDesc(Dept::getCreateTime));
        return pageResult;
    }

    @Override
    public DeptVO getInfo(String id) {
        return deptMapper.selectJoinOne(DeptVO.class, new MPJLambdaWrapper<Dept>()
                .innerJoin(User.class, User::getId, Dept::getLeaderId)
                .eq(Dept::getId, id)
                .selectAll(Dept.class)
                .selectAs(User::getUsername, DeptVO::getLeaderName));
    }

    @Override
    public void remove(String[] ids) {
        deptMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(DeptEditDTO param) {
        Dept user = BeanUtil.toBean(param, Dept.class);
        deptMapper.updateById(user);
    }

    @Override
    public void add(DeptAddDTO param) {
        boolean unique = commonCodeCheckService.isCodeUnique(Dept.class, Dept::getCode, param.getCode());
        if (!unique) {
            throw new BusinessException("部门【" + param.getCode() + "】已存在！");
        }
        Dept user = BeanUtil.toBean(param, Dept.class);
        deptMapper.insert(user);
    }

    @Override
    public void binding(DeptUserDTO param) {
        // 删除旧的数据
        deptUserRelService.remove(new LambdaQueryWrapper<DeptUserRel>().eq(DeptUserRel::getDeptId, param.getDeptId()));
        // 重新绑定
        List<DeptUserRel> relList = param.getUserId().stream()
                .map(userId -> new DeptUserRel(param.getDeptId(), userId))
                .collect(Collectors.toCollection(() ->
                        new ArrayList<>(param.getUserId().size())
                ));
        deptUserRelService.saveBatch(relList);
    }

    @Override
    public List<DeptTreeVO> getTree() {
        // 查询启用菜单集合
        List<Dept> deptList = deptMapper.selectList(new LambdaQueryWrapper<Dept>()
                .select(Dept::getId, Dept::getParentId, Dept::getCode, Dept::getName)
                .eq(Dept::getStatus, 1));
        List<DeptTreeVO> treeVOList = deptList.stream()
                .map(DeptTreeVO::convert)
                .collect(Collectors.toList());
        // 按照父级ID构建树状结构
        return TreeNodeUtils.buildTree(treeVOList, "0");
    }

    @Override
    public Page<UserVO> searchUserByPage(DeptUserQueryDTO param) {
        Page<UserVO> pageResult = deptMapper.selectJoinPage(param.getPage(), UserVO.class,
                new MPJLambdaWrapper<Dept>()
                        .innerJoin(DeptUserRel.class, DeptUserRel::getDeptId, Dept::getId)
                        .innerJoin(User.class, User::getId, DeptUserRel::getUserId)
                        .eq(StrUtil.isNotBlank(param.getDeptId()), Dept::getId, param.getDeptId())
                        .select(User::getId, User::getAccount, User::getUsername)
                        .selectAs(User::getUsername, DeptVO::getLeaderName)
                        .orderByDesc(Dept::getCreateTime));
        return pageResult;
    }
}
