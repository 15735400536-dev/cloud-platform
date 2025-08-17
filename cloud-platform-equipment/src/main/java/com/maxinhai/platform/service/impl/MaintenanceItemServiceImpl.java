package com.maxinhai.platform.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxinhai.platform.dto.MaintenanceItemAddDTO;
import com.maxinhai.platform.dto.MaintenanceItemEditDTO;
import com.maxinhai.platform.dto.MaintenanceItemQueryDTO;
import com.maxinhai.platform.mapper.MaintenanceItemMapper;
import com.maxinhai.platform.po.MaintenanceItem;
import com.maxinhai.platform.service.MaintenanceItemService;
import com.maxinhai.platform.vo.MaintenanceItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MaintenanceItemServiceImpl extends ServiceImpl<MaintenanceItemMapper, MaintenanceItem> implements MaintenanceItemService {
    @Override
    public Page<MaintenanceItemVO> searchByPage(MaintenanceItemQueryDTO param) {
        return null;
    }

    @Override
    public MaintenanceItemVO getInfo(String id) {
        return null;
    }

    @Override
    public void remove(String[] ids) {

    }

    @Override
    public void edit(MaintenanceItemEditDTO param) {

    }

    @Override
    public void add(MaintenanceItemAddDTO param) {

    }
}
