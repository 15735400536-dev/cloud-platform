package com.maxinhai.platform.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxinhai.platform.dto.InspectionConfigAddDTO;
import com.maxinhai.platform.dto.InspectionConfigEditDTO;
import com.maxinhai.platform.dto.InspectionConfigQueryDTO;
import com.maxinhai.platform.mapper.InspectionConfigMapper;
import com.maxinhai.platform.po.InspectionConfig;
import com.maxinhai.platform.service.InspectionConfigService;
import com.maxinhai.platform.vo.InspectionConfigVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InspectionConfigServiceImpl extends ServiceImpl<InspectionConfigMapper, InspectionConfig> implements InspectionConfigService {
    @Override
    public Page<InspectionConfigVO> searchByPage(InspectionConfigQueryDTO param) {
        return null;
    }

    @Override
    public InspectionConfigVO getInfo(String id) {
        return null;
    }

    @Override
    public void remove(String[] ids) {

    }

    @Override
    public void edit(InspectionConfigEditDTO param) {

    }

    @Override
    public void add(InspectionConfigAddDTO param) {

    }
}
