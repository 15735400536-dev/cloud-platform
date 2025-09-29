package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.CustomDataSourceAddDTO;
import com.maxinhai.platform.dto.CustomDataSourceEditDTO;
import com.maxinhai.platform.dto.CustomDataSourceQueryDTO;
import com.maxinhai.platform.mapper.CustomDataSourceMapper;
import com.maxinhai.platform.po.CustomDataSource;
import com.maxinhai.platform.service.CustomDataSourceService;
import com.maxinhai.platform.vo.CustomDataSourceVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName：CustomDataSourceServiceImpl
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 17:58
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Slf4j
@Service
public class CustomDataSourceServiceImpl extends ServiceImpl<CustomDataSourceMapper, CustomDataSource>
        implements CustomDataSourceService {

    @Resource
    private CustomDataSourceMapper dataSourceMapper;

    @Override
    public Page<CustomDataSourceVO> searchByPage(CustomDataSourceQueryDTO param) {
        return dataSourceMapper.selectJoinPage(param.getPage(), CustomDataSourceVO.class,
                new MPJLambdaWrapper<CustomDataSource>()
                        .like(StrUtil.isNotBlank(param.getKey()), CustomDataSource::getKey, param.getKey())
                        .like(Objects.nonNull(param.getType()), CustomDataSource::getType, param.getType())
                        .orderByDesc(CustomDataSource::getCreateTime));
    }

    @Override
    public CustomDataSourceVO getInfo(String id) {
        return dataSourceMapper.selectJoinOne(CustomDataSourceVO.class, new MPJLambdaWrapper<CustomDataSource>()
                .eq(CustomDataSource::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        dataSourceMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(CustomDataSourceEditDTO param) {
        CustomDataSource dataSource = BeanUtil.toBean(param, CustomDataSource.class);
        dataSourceMapper.updateById(dataSource);
    }

    @Override
    public void add(CustomDataSourceAddDTO param) {
        CustomDataSource dataSource = BeanUtil.toBean(param, CustomDataSource.class);
        dataSourceMapper.insert(dataSource);
    }
}
