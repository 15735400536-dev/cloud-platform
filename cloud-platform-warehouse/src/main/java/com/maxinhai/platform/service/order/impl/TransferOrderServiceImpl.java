package com.maxinhai.platform.service.order.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.order.TransferOrderAddDTO;
import com.maxinhai.platform.dto.order.TransferOrderEditDTO;
import com.maxinhai.platform.dto.order.TransferOrderQueryDTO;
import com.maxinhai.platform.mapper.order.TransferOrderMapper;
import com.maxinhai.platform.po.order.TransferOrder;
import com.maxinhai.platform.service.order.TransferOrderService;
import com.maxinhai.platform.vo.order.TransferOrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TransferOrderServiceImpl extends ServiceImpl<TransferOrderMapper, TransferOrder> implements TransferOrderService {

    @Resource
    private TransferOrderMapper transferOrderMapper;

    @Override
    public Page<TransferOrderVO> searchByPage(TransferOrderQueryDTO param) {
        Page<TransferOrderVO> pageResult = transferOrderMapper.selectJoinPage(param.getPage(), TransferOrderVO.class,
                new MPJLambdaWrapper<TransferOrder>()
                        .like(StrUtil.isNotBlank(param.getTransferNo()), TransferOrder::getTransferNo, param.getTransferNo())
                        .eq(StrUtil.isNotBlank(param.getSourceWarehouseId()), TransferOrder::getSourceWarehouseId, param.getSourceWarehouseId())
                        .eq(StrUtil.isNotBlank(param.getTargetWarehouseId()), TransferOrder::getTargetWarehouseId, param.getTargetWarehouseId())
                        .orderByDesc(TransferOrder::getCreateTime));
        return pageResult;
    }

    @Override
    public TransferOrderVO getInfo(String id) {
        return transferOrderMapper.selectJoinOne(TransferOrderVO.class, new MPJLambdaWrapper<TransferOrder>().eq(StrUtil.isNotBlank(id), TransferOrder::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        transferOrderMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(TransferOrderEditDTO param) {
        TransferOrder order = BeanUtil.toBean(param, TransferOrder.class);
        transferOrderMapper.updateById(order);
    }

    @Override
    public void add(TransferOrderAddDTO param) {
        TransferOrder order = BeanUtil.toBean(param, TransferOrder.class);
        transferOrderMapper.insert(order);
    }
}
