package com.maxinhai.platform.service.order.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.order.ReceiptOrderDetailAddDTO;
import com.maxinhai.platform.dto.order.ReceiptOrderDetailEditDTO;
import com.maxinhai.platform.dto.order.ReceiptOrderDetailQueryDTO;
import com.maxinhai.platform.mapper.order.ReceiptOrderDetailMapper;
import com.maxinhai.platform.po.order.ReceiptOrderDetail;
import com.maxinhai.platform.service.order.ReceiptOrderDetailService;
import com.maxinhai.platform.vo.order.ReceiptOrderDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReceiptOrderDetailServiceImpl extends ServiceImpl<ReceiptOrderDetailMapper, ReceiptOrderDetail>
        implements ReceiptOrderDetailService {
    
    @Resource
    private ReceiptOrderDetailMapper receiptOrderDetailMapper;
    
    @Override
    public Page<ReceiptOrderDetailVO> searchByPage(ReceiptOrderDetailQueryDTO param) {
        Page<ReceiptOrderDetailVO> pageResult = receiptOrderDetailMapper.selectJoinPage(param.getPage(), ReceiptOrderDetailVO.class,
                new MPJLambdaWrapper<ReceiptOrderDetail>()
                        .like(StrUtil.isNotBlank(param.getReceiptOrderId()), ReceiptOrderDetail::getReceiptOrderId, param.getReceiptOrderId())
                        .orderByDesc(ReceiptOrderDetail::getCreateTime));
        return pageResult;
    }

    @Override
    public ReceiptOrderDetailVO getInfo(String id) {
        return receiptOrderDetailMapper.selectJoinOne(ReceiptOrderDetailVO.class, new MPJLambdaWrapper<ReceiptOrderDetail>().eq(ReceiptOrderDetail::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        receiptOrderDetailMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(ReceiptOrderDetailEditDTO param) {
        ReceiptOrderDetail detail = BeanUtil.toBean(param, ReceiptOrderDetail.class);
        receiptOrderDetailMapper.updateById(detail);
    }

    @Override
    public void add(ReceiptOrderDetailAddDTO param) {
        ReceiptOrderDetail detail = BeanUtil.toBean(param, ReceiptOrderDetail.class);
        receiptOrderDetailMapper.insert(detail);
    }

    @Override
    public void receipt(String id) {

    }
}
