package com.maxinhai.platform.service.order.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.order.TransferOrderDetailAddDTO;
import com.maxinhai.platform.dto.order.TransferOrderDetailEditDTO;
import com.maxinhai.platform.dto.order.TransferOrderDetailQueryDTO;
import com.maxinhai.platform.mapper.order.TransferOrderDetailMapper;
import com.maxinhai.platform.po.order.TransferOrderDetail;
import com.maxinhai.platform.service.order.TransferOrderDetailService;
import com.maxinhai.platform.vo.order.TransferOrderDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TransferOrderDetailServiceImpl extends ServiceImpl<TransferOrderDetailMapper, TransferOrderDetail>
        implements TransferOrderDetailService {
    
    @Resource
    private TransferOrderDetailMapper transferOrderDetailMapper;
    
    @Override
    public Page<TransferOrderDetailVO> searchByPage(TransferOrderDetailQueryDTO param) {
        return transferOrderDetailMapper.selectJoinPage(param.getPage(), TransferOrderDetailVO.class,
                new MPJLambdaWrapper<TransferOrderDetail>()
                        .eq(StrUtil.isNotBlank(param.getTransferOrderId()), TransferOrderDetail::getTransferOrderId, param.getTransferOrderId())
                        .orderByDesc(TransferOrderDetail::getCreateTime));
    }

    @Override
    public TransferOrderDetailVO getInfo(String id) {
        return transferOrderDetailMapper.selectJoinOne(TransferOrderDetailVO.class, new MPJLambdaWrapper<TransferOrderDetail>().eq(TransferOrderDetail::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        transferOrderDetailMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(TransferOrderDetailEditDTO param) {
        TransferOrderDetail detail = BeanUtil.toBean(param, TransferOrderDetail.class);
        transferOrderDetailMapper.updateById(detail);
    }

    @Override
    public void add(TransferOrderDetailAddDTO param) {
        TransferOrderDetail detail = BeanUtil.toBean(param, TransferOrderDetail.class);
        transferOrderDetailMapper.insert(detail);
    }

    @Override
    public void transfer(String id) {

    }
}
