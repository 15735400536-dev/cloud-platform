package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
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
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EquipmentServiceImpl extends ServiceImpl<EquipmentMapper, Equipment> implements EquipmentService {

    @Resource
    private EquipmentMapper equipmentMapper;

    @Override
    public Page<EquipmentVO> searchByPage(EquipmentQueryDTO param) {
        Page<EquipmentVO> pageResult = equipmentMapper.selectJoinPage(param.getPage(), EquipmentVO.class,
                new MPJLambdaWrapper<Equipment>()
                        .like(StrUtil.isNotBlank(param.getEquipCode()), Equipment::getEquipCode, param.getEquipCode())
                        .like(StrUtil.isNotBlank(param.getEquipName()), Equipment::getEquipName, param.getEquipName())
                        .eq(StrUtil.isNotBlank(param.getEquipType()), Equipment::getEquipType, param.getEquipType())
                        .orderByDesc(Equipment::getCreateTime));
        return pageResult;
    }

    @Override
    public EquipmentVO getInfo(String id) {
        return equipmentMapper.selectJoinOne(EquipmentVO.class,
                new MPJLambdaWrapper<Equipment>()
                        .eq(StrUtil.isNotBlank(id), Equipment::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        equipmentMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(EquipmentEditDTO param) {
        Equipment equipment = BeanUtil.toBean(param, Equipment.class);
        equipmentMapper.updateById(equipment);
    }

    @Override
    public void add(EquipmentAddDTO param) {
        Equipment equipment = BeanUtil.toBean(param, Equipment.class);
        equipmentMapper.insert(equipment);
    }
}
