package com.maxinhai.platform.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxinhai.platform.dto.EquipmentAddDTO;
import com.maxinhai.platform.dto.EquipmentEditDTO;
import com.maxinhai.platform.dto.EquipmentQueryDTO;
import com.maxinhai.platform.mapper.EquipmentMapper;
import com.maxinhai.platform.po.Equipment;
import com.maxinhai.platform.service.EquipmentService;
import com.maxinhai.platform.vo.EquipmentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class EquipmentServiceImpl extends ServiceImpl<EquipmentMapper, Equipment> implements EquipmentService {

    @Resource
    private EquipmentMapper equipmentMapper;

    @Override
    public Page<EquipmentVO> searchByPage(EquipmentQueryDTO param) {
        return null;
    }

    @Override
    public EquipmentVO getInfo(String id) {
        return null;
    }

    @Override
    public void remove(String[] ids) {

    }

    @Override
    public void edit(EquipmentEditDTO param) {

    }

    @Override
    public void add(EquipmentAddDTO param) {

    }
}
