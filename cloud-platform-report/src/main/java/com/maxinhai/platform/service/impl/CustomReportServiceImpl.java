package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.CustomReportAddDTO;
import com.maxinhai.platform.dto.CustomReportEditDTO;
import com.maxinhai.platform.dto.CustomReportQueryDTO;
import com.maxinhai.platform.mapper.CustomReportMapper;
import com.maxinhai.platform.po.CustomReport;
import com.maxinhai.platform.service.CustomReportService;
import com.maxinhai.platform.vo.CustomReportVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @ClassName：CustomReportServiceImpl
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 17:59
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Slf4j
@Service
public class CustomReportServiceImpl extends ServiceImpl<CustomReportMapper, CustomReport>
        implements CustomReportService {

    @Resource
    private CustomReportMapper reportMapper;

    @Override
    public Page<CustomReportVO> searchByPage(CustomReportQueryDTO param) {
        Page<CustomReportVO> pageResult = reportMapper.selectJoinPage(param.getPage(), CustomReportVO.class,
                new MPJLambdaWrapper<CustomReport>()
                        .like(StrUtil.isNotBlank(param.getKey()), CustomReport::getKey, param.getKey())
                        .like(StrUtil.isNotBlank(param.getTitle()), CustomReport::getTitle, param.getTitle())
                        .orderByDesc(CustomReport::getCreateTime));
        return pageResult;
    }

    @Override
    public CustomReportVO getInfo(String id) {
        return reportMapper.selectJoinOne(CustomReportVO.class, new MPJLambdaWrapper<CustomReport>()
                .eq(CustomReport::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        reportMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(CustomReportEditDTO param) {
        CustomReport report = BeanUtil.toBean(param, CustomReport.class);
        reportMapper.updateById(report);
    }

    @Override
    public void add(CustomReportAddDTO param) {
        CustomReport report = BeanUtil.toBean(param, CustomReport.class);
        reportMapper.insert(report);
    }
}
