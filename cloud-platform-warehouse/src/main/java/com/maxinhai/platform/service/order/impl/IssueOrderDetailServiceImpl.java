package com.maxinhai.platform.service.order.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.order.IssueOrderDetailAddDTO;
import com.maxinhai.platform.dto.order.IssueOrderDetailEditDTO;
import com.maxinhai.platform.dto.order.IssueOrderDetailQueryDTO;
import com.maxinhai.platform.mapper.order.IssueOrderDetailMapper;
import com.maxinhai.platform.mapper.order.IssueOrderMapper;
import com.maxinhai.platform.po.order.IssueOrder;
import com.maxinhai.platform.po.order.IssueOrderDetail;
import com.maxinhai.platform.service.order.IssueOrderDetailService;
import com.maxinhai.platform.vo.order.IssueOrderDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
public class IssueOrderDetailServiceImpl extends ServiceImpl<IssueOrderDetailMapper, IssueOrderDetail> implements IssueOrderDetailService {

    @Resource
    private IssueOrderDetailMapper issueOrderDetailMapper;
    @Resource
    private IssueOrderMapper issueOrderMapper;

    @Override
    public Page<IssueOrderDetailVO> searchByPage(IssueOrderDetailQueryDTO param) {
        Page<IssueOrderDetailVO> pageResult = issueOrderDetailMapper.selectJoinPage(param.getPage(), IssueOrderDetailVO.class,
                new MPJLambdaWrapper<IssueOrderDetail>()
                        .like(StrUtil.isNotBlank(param.getIssueOrderId()), IssueOrderDetail::getIssueOrderId, param.getIssueOrderId())
                        .orderByDesc(IssueOrderDetail::getCreateTime));
        return pageResult;
    }

    @Override
    public IssueOrderDetailVO getInfo(String id) {
        return issueOrderDetailMapper.selectJoinOne(IssueOrderDetailVO.class, new MPJLambdaWrapper<IssueOrderDetail>().eq(IssueOrderDetail::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        issueOrderDetailMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(IssueOrderDetailEditDTO param) {
        IssueOrderDetail detail = BeanUtil.toBean(param, IssueOrderDetail.class);
        issueOrderDetailMapper.updateById(detail);
    }

    @Override
    public void add(IssueOrderDetailAddDTO param) {
        IssueOrderDetail detail = BeanUtil.toBean(param, IssueOrderDetail.class);
        issueOrderDetailMapper.insert(detail);
    }

    @Override
    public void issue(String id) {
        IssueOrderDetail issueOrderDetail = issueOrderDetailMapper.selectById(id);
        IssueOrder issueOrder = issueOrderMapper.selectById(issueOrderDetail.getIssueOrderId());
    }
}
