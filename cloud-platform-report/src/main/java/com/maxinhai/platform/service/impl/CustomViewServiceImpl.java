package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.CustomViewAddDTO;
import com.maxinhai.platform.dto.CustomViewEditDTO;
import com.maxinhai.platform.dto.CustomViewQueryDTO;
import com.maxinhai.platform.mapper.CustomViewMapper;
import com.maxinhai.platform.po.CustomView;
import com.maxinhai.platform.service.CustomViewService;
import com.maxinhai.platform.vo.CustomViewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName：CustomViewServiceImpl
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 18:01
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Slf4j
@Service
public class CustomViewServiceImpl extends ServiceImpl<CustomViewMapper, CustomView> implements CustomViewService {

    @Resource
    private CustomViewMapper viewMapper;

    @Override
    public Page<CustomViewVO> searchByPage(CustomViewQueryDTO param) {
        Page<CustomViewVO> pageResult = viewMapper.selectJoinPage(param.getPage(), CustomViewVO.class,
                new MPJLambdaWrapper<CustomView>()
                        .like(StrUtil.isNotBlank(param.getKey()), CustomView::getKey, param.getKey())
                        .like(StrUtil.isNotBlank(param.getTitle()), CustomView::getTitle, param.getTitle())
                        .like(Objects.nonNull(param.getType()), CustomView::getType, param.getType())
                        .orderByDesc(CustomView::getCreateTime));
        return pageResult;
    }

    @Override
    public CustomViewVO getInfo(String id) {
        return viewMapper.selectJoinOne(CustomViewVO.class, new MPJLambdaWrapper<CustomView>()
                .eq(CustomView::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        viewMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(CustomViewEditDTO param) {
        CustomView view = BeanUtil.toBean(param, CustomView.class);
        viewMapper.updateById(view);
    }

    @Override
    public void add(CustomViewAddDTO param) {
        CustomView view = BeanUtil.toBean(param, CustomView.class);
        viewMapper.insert(view);
    }
}
