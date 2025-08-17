package com.maxinhai.platform.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxinhai.platform.dto.MaintenanceConfigAddDTO;
import com.maxinhai.platform.dto.MaintenanceConfigEditDTO;
import com.maxinhai.platform.dto.MaintenanceConfigQueryDTO;
import com.maxinhai.platform.mapper.MaintenanceConfigMapper;
import com.maxinhai.platform.po.MaintenanceConfig;
import com.maxinhai.platform.service.MaintenanceConfigService;
import com.maxinhai.platform.vo.MaintenanceConfigVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MaintenanceConfigServiceImpl extends ServiceImpl<MaintenanceConfigMapper, MaintenanceConfig> implements MaintenanceConfigService {
    @Override
    public Page<MaintenanceConfigVO> searchByPage(MaintenanceConfigQueryDTO param) {
        return null;
    }

    @Override
    public MaintenanceConfigVO getInfo(String id) {
        return null;
    }

    @Override
    public void remove(String[] ids) {

    }

    @Override
    public void edit(MaintenanceConfigEditDTO param) {

    }

    @Override
    public void add(MaintenanceConfigAddDTO param) {

    }
}
