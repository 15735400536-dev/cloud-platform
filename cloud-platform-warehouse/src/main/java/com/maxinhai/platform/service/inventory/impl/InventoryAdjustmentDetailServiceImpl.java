package com.maxinhai.platform.service.inventory.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.inventory.InventoryAdjustmentDetailAddDTO;
import com.maxinhai.platform.dto.inventory.InventoryAdjustmentDetailEditDTO;
import com.maxinhai.platform.dto.inventory.InventoryAdjustmentDetailQueryDTO;
import com.maxinhai.platform.mapper.inventory.InventoryAdjustmentDetailMapper;
import com.maxinhai.platform.po.inventory.InventoryAdjustmentDetail;
import com.maxinhai.platform.service.inventory.InventoryAdjustmentDetailService;
import com.maxinhai.platform.vo.inventory.InventoryAdjustmentDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InventoryAdjustmentDetailServiceImpl extends ServiceImpl<InventoryAdjustmentDetailMapper, InventoryAdjustmentDetail>
        implements InventoryAdjustmentDetailService {

    @Resource
    private InventoryAdjustmentDetailMapper inventoryAdjustmentDetailMapper;

    @Override
    public Page<InventoryAdjustmentDetailVO> searchByPage(InventoryAdjustmentDetailQueryDTO param) {
        return inventoryAdjustmentDetailMapper.selectJoinPage(param.getPage(), InventoryAdjustmentDetailVO.class,
                new MPJLambdaWrapper<InventoryAdjustmentDetail>()
                        .eq(StrUtil.isNotBlank(param.getAdjustmentId()), InventoryAdjustmentDetail::getAdjustmentId, param.getAdjustmentId())
                        .orderByDesc(InventoryAdjustmentDetail::getCreateTime));
    }

    @Override
    public InventoryAdjustmentDetailVO getInfo(String id) {
        return inventoryAdjustmentDetailMapper.selectJoinOne(InventoryAdjustmentDetailVO.class, new MPJLambdaWrapper<InventoryAdjustmentDetail>().eq(InventoryAdjustmentDetail::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        inventoryAdjustmentDetailMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(InventoryAdjustmentDetailEditDTO param) {
        InventoryAdjustmentDetail detail = BeanUtil.toBean(param, InventoryAdjustmentDetail.class);
        inventoryAdjustmentDetailMapper.updateById(detail);
    }

    @Override
    public void add(InventoryAdjustmentDetailAddDTO param) {
        InventoryAdjustmentDetail detail = BeanUtil.toBean(param, InventoryAdjustmentDetail.class);
        inventoryAdjustmentDetailMapper.insert(detail);
    }

    @Override
    public void adjustment(String id) {

    }
}
