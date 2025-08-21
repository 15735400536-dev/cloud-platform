package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.CustomConditionAddDTO;
import com.maxinhai.platform.dto.CustomConditionEditDTO;
import com.maxinhai.platform.dto.CustomConditionQueryDTO;
import com.maxinhai.platform.mapper.CustomConditionMapper;
import com.maxinhai.platform.po.CustomCondition;
import com.maxinhai.platform.service.CustomConditionService;
import com.maxinhai.platform.vo.CustomConditionVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @ClassName：CustomConditionServiceImpl
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 17:57
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Slf4j
@Service
public class CustomConditionServiceImpl extends ServiceImpl<CustomConditionMapper, CustomCondition>
        implements CustomConditionService {

    @Resource
    private CustomConditionMapper conditionMapper;

    @Override
    public Page<CustomConditionVO> searchByPage(CustomConditionQueryDTO param) {
        Page<CustomConditionVO> pageResult = conditionMapper.selectJoinPage(param.getPage(), CustomConditionVO.class,
                new MPJLambdaWrapper<CustomCondition>()
                        .like(StrUtil.isNotBlank(param.getField()), CustomCondition::getField, param.getField())
                        .orderByDesc(CustomCondition::getCreateTime));
        return pageResult;
    }

    @Override
    public CustomConditionVO getInfo(String id) {
        return conditionMapper.selectJoinOne(CustomConditionVO.class, new MPJLambdaWrapper<CustomCondition>()
                .eq(CustomCondition::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        conditionMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(CustomConditionEditDTO param) {
        CustomCondition condition = BeanUtil.toBean(param, CustomCondition.class);
        conditionMapper.updateById(condition);
    }

    @Override
    public void add(CustomConditionAddDTO param) {
        CustomCondition condition = BeanUtil.toBean(param, CustomCondition.class);
        conditionMapper.insert(condition);
    }
}
