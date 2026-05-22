package com.maxinhai.platform.service.habit.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.habit.UserHabitCheckinDTO;
import com.maxinhai.platform.dto.habit.UserHabitCheckinQueryDTO;
import com.maxinhai.platform.mapper.habit.HabitTypeMapper;
import com.maxinhai.platform.mapper.habit.UserHabitCheckinMapper;
import com.maxinhai.platform.po.User;
import com.maxinhai.platform.po.habit.HabitType;
import com.maxinhai.platform.po.habit.UserHabitCheckin;
import com.maxinhai.platform.service.habit.UserHabitCheckinService;
import com.maxinhai.platform.utils.JwtUtils;
import com.maxinhai.platform.vo.habit.UserHabitCheckinVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class UserHabitCheckinImpl extends ServiceImpl<UserHabitCheckinMapper, UserHabitCheckin>
        implements UserHabitCheckinService {

    private final HabitTypeMapper habitTypeMapper;
    private final UserHabitCheckinMapper userHabitCheckinMapper;
    private final JwtUtils jwtUtils;

    @Override
    public Page<UserHabitCheckinVO> searchByPage(UserHabitCheckinQueryDTO param) {
        return userHabitCheckinMapper.selectJoinPage(param.getPage(), UserHabitCheckinVO.class, new MPJLambdaWrapper<UserHabitCheckin>()
                .leftJoin(User.class, User::getId, UserHabitCheckin::getUserId)
                .leftJoin(HabitType.class, HabitType::getId, UserHabitCheckin::getHabitTypeId)
                // 查询条件
                .eq(StrUtil.isNotBlank(param.getUserId()), UserHabitCheckin::getUserId, param.getUserId())
                .eq(StrUtil.isNotBlank(param.getHabitTypeId()), UserHabitCheckin::getHabitTypeId, param.getHabitTypeId())
                // 查询字段
                .selectAll(UserHabitCheckin.class)
                .selectAs(User::getAccount, UserHabitCheckinVO::getAccount)
                .selectAs(User::getUsername, UserHabitCheckinVO::getUsername)
                .selectAs(HabitType::getTypeCode, UserHabitCheckinVO::getHabitTypeCode)
                .selectAs(HabitType::getTypeName, UserHabitCheckinVO::getHabitTypeName)
                .selectAs(UserHabitCheckin::getCreateTime, UserHabitCheckinVO::getCheckinTime)
                // 字段排序
                .orderByDesc(UserHabitCheckin::getCreateTime));
    }

    @Override
    public UserHabitCheckinVO getInfo(String id) {
        return userHabitCheckinMapper.selectJoinOne(UserHabitCheckinVO.class, new MPJLambdaWrapper<UserHabitCheckin>()
                .leftJoin(User.class, User::getId, UserHabitCheckin::getUserId)
                .leftJoin(HabitType.class, HabitType::getId, UserHabitCheckin::getHabitTypeId)
                // 查询字段
                .selectAll(UserHabitCheckin.class)
                .selectAs(User::getAccount, UserHabitCheckinVO::getAccount)
                .selectAs(User::getUsername, UserHabitCheckinVO::getUsername)
                .selectAs(HabitType::getTypeCode, UserHabitCheckinVO::getHabitTypeCode)
                .selectAs(HabitType::getTypeName, UserHabitCheckinVO::getHabitTypeName)
                // 查询条件
                .eq(UserHabitCheckin::getId, id));
    }

    @Override
    public boolean checkin(UserHabitCheckinDTO param) {
        UserHabitCheckin userHabitCheckin = new UserHabitCheckin(jwtUtils.getUserIdFromToken(jwtUtils.getToken()), param.getHabitTypeId(), param.getRemark());
        int row = userHabitCheckinMapper.insert(userHabitCheckin);
        return row > 0;
    }
}
