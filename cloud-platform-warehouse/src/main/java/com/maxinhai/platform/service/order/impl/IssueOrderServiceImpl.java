package com.maxinhai.platform.service.order.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.order.IssueOrderAddDTO;
import com.maxinhai.platform.dto.order.IssueOrderEditDTO;
import com.maxinhai.platform.dto.order.IssueOrderQueryDTO;
import com.maxinhai.platform.mapper.order.IssueOrderMapper;
import com.maxinhai.platform.po.order.IssueOrder;
import com.maxinhai.platform.service.order.IssueOrderService;
import com.maxinhai.platform.vo.order.IssueOrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
public class IssueOrderServiceImpl extends ServiceImpl<IssueOrderMapper, IssueOrder> implements IssueOrderService {

    @Resource
    private IssueOrderMapper issueOrderMapper;

    @Override
    public Page<IssueOrderVO> searchByPage(IssueOrderQueryDTO param) {
        Page<IssueOrderVO> pageResult = issueOrderMapper.selectJoinPage(param.getPage(), IssueOrderVO.class,
                new MPJLambdaWrapper<IssueOrder>()
                        .like(StrUtil.isNotBlank(param.getOrderNo()), IssueOrder::getOrderNo, param.getOrderNo())
                        .like(StrUtil.isNotBlank(param.getWarehouseId()), IssueOrder::getWarehouseId, param.getWarehouseId())
                        .orderByDesc(IssueOrder::getCreateTime));
        return pageResult;
    }

    @Override
    public IssueOrderVO getInfo(String id) {
        return issueOrderMapper.selectJoinOne(IssueOrderVO.class, new MPJLambdaWrapper<IssueOrder>().eq(StrUtil.isNotBlank(id), IssueOrder::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        issueOrderMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(IssueOrderEditDTO param) {
        IssueOrder order = BeanUtil.toBean(param, IssueOrder.class);
        issueOrderMapper.updateById(order);
    }

    @Override
    public void add(IssueOrderAddDTO param) {
        IssueOrder order = BeanUtil.toBean(param, IssueOrder.class);
        issueOrderMapper.insert(order);
    }
}
