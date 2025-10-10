package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.InspectionConfigAddDTO;
import com.maxinhai.platform.dto.InspectionConfigEditDTO;
import com.maxinhai.platform.dto.InspectionConfigQueryDTO;
import com.maxinhai.platform.mapper.InspectionConfigMapper;
import com.maxinhai.platform.po.InspectionConfig;
import com.maxinhai.platform.service.InspectionConfigService;
import com.maxinhai.platform.vo.InspectionConfigVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InspectionConfigServiceImpl extends ServiceImpl<InspectionConfigMapper, InspectionConfig>
        implements InspectionConfigService {

    @Resource
    private InspectionConfigMapper inspectionConfigMapper;

    @Override
    public Page<InspectionConfigVO> searchByPage(InspectionConfigQueryDTO param) {
        return inspectionConfigMapper.selectJoinPage(param.getPage(), InspectionConfigVO.class,
                new MPJLambdaWrapper<InspectionConfig>()
                        .like(StrUtil.isNotBlank(param.getConfigCode()), InspectionConfig::getConfigCode, param.getConfigCode())
                        .like(StrUtil.isNotBlank(param.getConfigName()), InspectionConfig::getConfigName, param.getConfigName())
                        .orderByDesc(InspectionConfig::getCreateTime));
    }

    @Override
    public InspectionConfigVO getInfo(String id) {
        return inspectionConfigMapper.selectJoinOne(InspectionConfigVO.class,
                new MPJLambdaWrapper<InspectionConfig>()
                        .eq(StrUtil.isNotBlank(id), InspectionConfig::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        inspectionConfigMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(InspectionConfigEditDTO param) {
        InspectionConfig config = BeanUtil.toBean(param, InspectionConfig.class);
        inspectionConfigMapper.updateById(config);
    }

    @Override
    public void add(InspectionConfigAddDTO param) {
        InspectionConfig config = BeanUtil.toBean(param, InspectionConfig.class);
        inspectionConfigMapper.insert(config);
    }
}
