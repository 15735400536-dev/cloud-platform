package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.DiaryAddDTO;
import com.maxinhai.platform.dto.DiaryEditDTO;
import com.maxinhai.platform.dto.DiaryQueryDTO;
import com.maxinhai.platform.mapper.DiaryMapper;
import com.maxinhai.platform.po.Diary;
import com.maxinhai.platform.po.DiaryType;
import com.maxinhai.platform.service.DiaryService;
import com.maxinhai.platform.vo.DiaryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DiaryServiceImpl extends ServiceImpl<DiaryMapper, Diary> implements DiaryService {

    @Resource
    private DiaryMapper diaryMapper;

    @Override
    public Page<DiaryVO> searchByPage(DiaryQueryDTO param) {
        Page<DiaryVO> pageResult = diaryMapper.selectJoinPage(param.getPage(), DiaryVO.class,
                new MPJLambdaWrapper<Diary>()
                        .innerJoin(DiaryType.class, DiaryType::getId, Diary::getDiaryTypeId)
                        // 查询条件
                        .like(StrUtil.isNotBlank(param.getTitle()), Diary::getTitle, param.getTitle())
                        .eq(StrUtil.isNotBlank(param.getDiaryTypeId()), Diary::getDiaryTypeId, param.getDiaryTypeId())
                        // 字段别名
                        .selectAll(Diary.class)
                        .selectAs(DiaryType::getName, DiaryVO::getDiaryTypeName)
                        // 排序
                        .orderByDesc(Diary::getCreateTime));
        return pageResult;
    }

    @Override
    public DiaryVO getInfo(String id) {
        return diaryMapper.selectJoinOne(DiaryVO.class,
                new MPJLambdaWrapper<Diary>()
                        .innerJoin(DiaryType.class, DiaryType::getId, Diary::getDiaryTypeId)
                        // 查询条件
                        .eq(StrUtil.isNotBlank(id), Diary::getId, id)
                        // 字段别名
                        .selectAll(Diary.class)
                        .selectAs(DiaryType::getName, DiaryVO::getDiaryTypeName));
    }

    @Override
    public void remove(String[] ids) {
        diaryMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(DiaryEditDTO param) {
        Diary user = BeanUtil.toBean(param, Diary.class);
        diaryMapper.updateById(user);
    }

    @Override
    public void add(DiaryAddDTO param) {
        Diary user = BeanUtil.toBean(param, Diary.class);
        diaryMapper.insert(user);
    }
}
