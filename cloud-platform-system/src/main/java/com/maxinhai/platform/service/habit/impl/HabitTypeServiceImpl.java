package com.maxinhai.platform.service.habit.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.habit.HabitTypeAddDTO;
import com.maxinhai.platform.dto.habit.HabitTypeEditDTO;
import com.maxinhai.platform.dto.habit.HabitTypeQueryDTO;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.mapper.habit.HabitTypeMapper;
import com.maxinhai.platform.mapper.habit.UserHabitCheckinMapper;
import com.maxinhai.platform.po.habit.HabitType;
import com.maxinhai.platform.service.CommonCodeCheckService;
import com.maxinhai.platform.service.habit.HabitTypeService;
import com.maxinhai.platform.vo.habit.HabitTypeVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class HabitTypeServiceImpl extends ServiceImpl<HabitTypeMapper, HabitType> implements HabitTypeService {

    private final HabitTypeMapper habitTypeMapper;
    private final UserHabitCheckinMapper userHabitCheckinMapper;
    private final CommonCodeCheckService commonCodeCheckService;

    @Override
    public Page<HabitTypeVO> searchByPage(HabitTypeQueryDTO param) {
        return habitTypeMapper.selectJoinPage(param.getPage(), HabitTypeVO.class, new MPJLambdaWrapper<HabitType>()
                // 查询条件
                .selectAll(HabitType.class)
                .like(StrUtil.isNotBlank(param.getTypeCode()), HabitType::getTypeCode, param.getTypeCode())
                .like(StrUtil.isNotBlank(param.getTypeName()), HabitType::getTypeName, param.getTypeName())
                // 字段排序
                .orderByDesc(HabitType::getCreateTime));
    }

    @Override
    public HabitTypeVO getInfo(String id) {
        return habitTypeMapper.selectJoinOne(HabitTypeVO.class, new MPJLambdaWrapper<HabitType>()
                .selectAll(HabitType.class)
                .eq(HabitType::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        habitTypeMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(HabitTypeEditDTO param) {
        HabitType habitType = BeanUtil.toBean(param, HabitType.class);
        habitTypeMapper.updateById(habitType);
    }

    @Override
    public void add(HabitTypeAddDTO param) {
        boolean unique = commonCodeCheckService.isCodeUnique(HabitType.class, HabitType::getTypeCode, param.getTypeCode());
        if (!unique) {
            throw new BusinessException("习惯编码【" + param.getTypeCode() + "】已存在！");
        }
        HabitType habitType = BeanUtil.toBean(param, HabitType.class);
        habitTypeMapper.insert(habitType);
    }
}
