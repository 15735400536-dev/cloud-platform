package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.CheckOrderDetailAddDTO;
import com.maxinhai.platform.dto.CheckOrderDetailEditDTO;
import com.maxinhai.platform.dto.CheckOrderDetailQueryDTO;
import com.maxinhai.platform.mapper.CheckOrderDetailMapper;
import com.maxinhai.platform.po.CheckOrderDetail;
import com.maxinhai.platform.service.CheckOrderDetailService;
import com.maxinhai.platform.vo.CheckOrderDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CheckOrderDetailServiceImpl extends ServiceImpl<CheckOrderDetailMapper, CheckOrderDetail> implements CheckOrderDetailService {

    @Resource
    private CheckOrderDetailMapper checkOrderDetailMapper;

    @Override
    public Page<CheckOrderDetailVO> searchByPage(CheckOrderDetailQueryDTO param) {
        Page<CheckOrderDetailVO> pageResult = checkOrderDetailMapper.selectJoinPage(param.getPage(), CheckOrderDetailVO.class,
                new MPJLambdaWrapper<CheckOrderDetail>()
                        .eq(StrUtil.isNotBlank(param.getCheckOrderId()), CheckOrderDetail::getCheckOrderId, param.getCheckOrderId())
                        .orderByDesc(CheckOrderDetail::getCreateTime));
        return pageResult;
    }

    @Override
    public CheckOrderDetailVO getInfo(String id) {
        return checkOrderDetailMapper.selectJoinOne(CheckOrderDetailVO.class,
                new MPJLambdaWrapper<CheckOrderDetail>()
                        .eq(StrUtil.isNotBlank(id), CheckOrderDetail::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        checkOrderDetailMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(CheckOrderDetailEditDTO param) {
        CheckOrderDetail user = BeanUtil.toBean(param, CheckOrderDetail.class);
        checkOrderDetailMapper.updateById(user);
    }

    @Override
    public void add(CheckOrderDetailAddDTO param) {
        CheckOrderDetail user = BeanUtil.toBean(param, CheckOrderDetail.class);
        checkOrderDetailMapper.insert(user);
    }
}
