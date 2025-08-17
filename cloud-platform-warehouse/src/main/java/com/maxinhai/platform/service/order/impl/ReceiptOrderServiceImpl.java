package com.maxinhai.platform.service.order.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.order.ReceiptOrderAddDTO;
import com.maxinhai.platform.dto.order.ReceiptOrderEditDTO;
import com.maxinhai.platform.dto.order.ReceiptOrderQueryDTO;
import com.maxinhai.platform.mapper.order.ReceiptOrderMapper;
import com.maxinhai.platform.po.order.ReceiptOrder;
import com.maxinhai.platform.service.order.ReceiptOrderService;
import com.maxinhai.platform.vo.order.ReceiptOrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReceiptOrderServiceImpl extends ServiceImpl<ReceiptOrderMapper, ReceiptOrder> implements ReceiptOrderService {

    @Resource
    private ReceiptOrderMapper receiptOrderMapper;

    @Override
    public Page<ReceiptOrderVO> searchByPage(ReceiptOrderQueryDTO param) {
        Page<ReceiptOrderVO> pageResult = receiptOrderMapper.selectJoinPage(param.getPage(), ReceiptOrderVO.class,
                new MPJLambdaWrapper<ReceiptOrder>()
                        .like(StrUtil.isNotBlank(param.getOrderNo()), ReceiptOrder::getOrderNo, param.getOrderNo())
                        .eq(StrUtil.isNotBlank(param.getWarehouseId()), ReceiptOrder::getWarehouseId, param.getWarehouseId())
                        .orderByDesc(ReceiptOrder::getCreateTime));
        return pageResult;
    }

    @Override
    public ReceiptOrderVO getInfo(String id) {
        return receiptOrderMapper.selectJoinOne(ReceiptOrderVO.class,new MPJLambdaWrapper<ReceiptOrder>().eq(StrUtil.isNotBlank(id), ReceiptOrder::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        receiptOrderMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(ReceiptOrderEditDTO param) {
        ReceiptOrder order = BeanUtil.toBean(param, ReceiptOrder.class);
        receiptOrderMapper.updateById(order);
    }

    @Override
    public void add(ReceiptOrderAddDTO param) {
        ReceiptOrder order = BeanUtil.toBean(param, ReceiptOrder.class);
        receiptOrderMapper.insert(order);
    }
}
