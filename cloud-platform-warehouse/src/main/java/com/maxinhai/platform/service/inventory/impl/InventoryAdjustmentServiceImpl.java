package com.maxinhai.platform.service.inventory.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.inventory.InventoryAdjustmentQueryDTO;
import com.maxinhai.platform.mapper.inventory.InventoryAdjustmentMapper;
import com.maxinhai.platform.po.inventory.InventoryAdjustment;
import com.maxinhai.platform.service.inventory.InventoryAdjustmentService;
import com.maxinhai.platform.vo.inventory.InventoryAdjustmentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class InventoryAdjustmentServiceImpl extends ServiceImpl<InventoryAdjustmentMapper, InventoryAdjustment>
        implements InventoryAdjustmentService {

    @Resource
    private InventoryAdjustmentMapper inventoryAdjustmentMapper;

    @Override
    public Page<InventoryAdjustmentVO> searchByPage(InventoryAdjustmentQueryDTO param) {
        Page<InventoryAdjustmentVO> pageResult = inventoryAdjustmentMapper.selectJoinPage(param.getPage(), InventoryAdjustmentVO.class,
                new MPJLambdaWrapper<InventoryAdjustment>()
                        .like(StrUtil.isNotBlank(param.getAdjustmentNo()), InventoryAdjustment::getAdjustmentNo, param.getAdjustmentNo())
                        .eq(StrUtil.isNotBlank(param.getWarehouseId()), InventoryAdjustment::getWarehouseId, param.getWarehouseId())
                        .orderByDesc(InventoryAdjustment::getCreateTime));
        return pageResult;
    }

    @Override
    public InventoryAdjustmentVO getInfo(String id) {
        return inventoryAdjustmentMapper.selectJoinOne(InventoryAdjustmentVO.class, new MPJLambdaWrapper<InventoryAdjustment>().eq(InventoryAdjustment::getId, id));
    }
}
