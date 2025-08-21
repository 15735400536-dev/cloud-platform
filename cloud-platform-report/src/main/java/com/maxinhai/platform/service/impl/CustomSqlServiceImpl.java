package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.CustomSqlAddDTO;
import com.maxinhai.platform.dto.CustomSqlEditDTO;
import com.maxinhai.platform.dto.CustomSqlQueryDTO;
import com.maxinhai.platform.mapper.CustomSqlMapper;
import com.maxinhai.platform.po.CustomSql;
import com.maxinhai.platform.service.CustomSqlService;
import com.maxinhai.platform.vo.CustomSqlVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @ClassName：CustomSqlServiceImpl
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 18:00
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Slf4j
@Service
public class CustomSqlServiceImpl extends ServiceImpl<CustomSqlMapper, CustomSql> implements CustomSqlService {

    @Resource
    private CustomSqlMapper sqlMapper;

    @Override
    public Page<CustomSqlVO> searchByPage(CustomSqlQueryDTO param) {
        Page<CustomSqlVO> pageResult = sqlMapper.selectJoinPage(param.getPage(), CustomSqlVO.class,
                new MPJLambdaWrapper<CustomSql>()
                        .like(StrUtil.isNotBlank(param.getSql()), CustomSql::getSql, param.getSql())
                        .eq(StrUtil.isNotBlank(param.getDataSourceId()), CustomSql::getDataSourceId, param.getDataSourceId())
                        .orderByDesc(CustomSql::getCreateTime));
        return pageResult;
    }

    @Override
    public CustomSqlVO getInfo(String id) {
        return sqlMapper.selectJoinOne(CustomSqlVO.class, new MPJLambdaWrapper<CustomSql>()
                .eq(CustomSql::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        sqlMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(CustomSqlEditDTO param) {
        CustomSql sql = BeanUtil.toBean(param, CustomSql.class);
        sqlMapper.updateById(sql);
    }

    @Override
    public void add(CustomSqlAddDTO param) {
        CustomSql sql = BeanUtil.toBean(param, CustomSql.class);
        sqlMapper.insert(sql);
    }
}
