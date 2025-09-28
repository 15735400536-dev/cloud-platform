package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.MaintenanceConfigAddDTO;
import com.maxinhai.platform.dto.MaintenanceConfigEditDTO;
import com.maxinhai.platform.dto.MaintenanceConfigQueryDTO;
import com.maxinhai.platform.mapper.MaintenanceConfigMapper;
import com.maxinhai.platform.po.MaintenanceConfig;
import com.maxinhai.platform.service.MaintenanceConfigService;
import com.maxinhai.platform.vo.MaintenanceConfigVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MaintenanceConfigServiceImpl extends ServiceImpl<MaintenanceConfigMapper, MaintenanceConfig> implements MaintenanceConfigService {
    
    @Resource
    private MaintenanceConfigMapper maintenanceConfigMapper;
    
    @Override
    public Page<MaintenanceConfigVO> searchByPage(MaintenanceConfigQueryDTO param) {
        return maintenanceConfigMapper.selectJoinPage(param.getPage(), MaintenanceConfigVO.class,
                new MPJLambdaWrapper<MaintenanceConfig>()
                        .like(StrUtil.isNotBlank(param.getConfigCode()), MaintenanceConfig::getConfigCode, param.getConfigCode())
                        .like(StrUtil.isNotBlank(param.getConfigName()), MaintenanceConfig::getConfigName, param.getConfigName())
                        .orderByDesc(MaintenanceConfig::getCreateTime));
    }

    @Override
    public MaintenanceConfigVO getInfo(String id) {
        return maintenanceConfigMapper.selectJoinOne(MaintenanceConfigVO.class,
                new MPJLambdaWrapper<MaintenanceConfig>()
                        .eq(StrUtil.isNotBlank(id), MaintenanceConfig::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        maintenanceConfigMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(MaintenanceConfigEditDTO param) {
        MaintenanceConfig config = BeanUtil.toBean(param, MaintenanceConfig.class);
        maintenanceConfigMapper.updateById(config);
    }

    @Override
    public void add(MaintenanceConfigAddDTO param) {
        MaintenanceConfig config = BeanUtil.toBean(param, MaintenanceConfig.class);
        maintenanceConfigMapper.insert(config);
    }
}
