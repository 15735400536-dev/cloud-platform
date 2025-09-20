package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.DiaryTypeAddDTO;
import com.maxinhai.platform.dto.DiaryTypeEditDTO;
import com.maxinhai.platform.dto.DiaryTypeQueryDTO;
import com.maxinhai.platform.mapper.DiaryTypeMapper;
import com.maxinhai.platform.po.DiaryType;
import com.maxinhai.platform.service.DiaryTypeService;
import com.maxinhai.platform.vo.DiaryTypeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DiaryTypeServiceImpl extends ServiceImpl<DiaryTypeMapper, DiaryType> implements DiaryTypeService {

    @Resource
    private DiaryTypeMapper diaryTypeMapper;

    @Override
    public Page<DiaryTypeVO> searchByPage(DiaryTypeQueryDTO param) {
        Page<DiaryTypeVO> pageResult = diaryTypeMapper.selectJoinPage(param.getPage(), DiaryTypeVO.class,
                new MPJLambdaWrapper<DiaryType>()
                        .like(StrUtil.isNotBlank(param.getName()), DiaryType::getName, param.getName())
                        .orderByDesc(DiaryType::getCreateTime));
        return pageResult;
    }

    @Override
    public DiaryTypeVO getInfo(String id) {
        return diaryTypeMapper.selectJoinOne(DiaryTypeVO.class, new MPJLambdaWrapper<DiaryType>().eq(DiaryType::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        diaryTypeMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(DiaryTypeEditDTO param) {
        DiaryType user = BeanUtil.toBean(param, DiaryType.class);
        diaryTypeMapper.updateById(user);
    }

    @Override
    public void add(DiaryTypeAddDTO param) {
        DiaryType user = BeanUtil.toBean(param, DiaryType.class);
        diaryTypeMapper.insert(user);
    }
}
