package com.maxinhai.platform.service.stocktaking.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.stocktaking.StocktakingDetailAddDTO;
import com.maxinhai.platform.dto.stocktaking.StocktakingDetailEditDTO;
import com.maxinhai.platform.dto.stocktaking.StocktakingDetailQueryDTO;
import com.maxinhai.platform.mapper.stocktaking.StocktakingDetailMapper;
import com.maxinhai.platform.po.stocktaking.StocktakingDetail;
import com.maxinhai.platform.service.stocktaking.StocktakingDetailService;
import com.maxinhai.platform.vo.stocktaking.StocktakingDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StocktakingDetailServiceImpl extends ServiceImpl<StocktakingDetailMapper, StocktakingDetail>
        implements StocktakingDetailService {
    
    @Resource
    private StocktakingDetailMapper stocktakingDetailMapper;
    
    @Override
    public Page<StocktakingDetailVO> searchByPage(StocktakingDetailQueryDTO param) {
        Page<StocktakingDetailVO> pageResult = stocktakingDetailMapper.selectJoinPage(param.getPage(), StocktakingDetailVO.class,
                new MPJLambdaWrapper<StocktakingDetail>()
                        .eq(StrUtil.isNotBlank(param.getStocktakingId()), StocktakingDetail::getStocktakingId, param.getStocktakingId())
                        .orderByDesc(StocktakingDetail::getCreateTime));
        return pageResult;
    }

    @Override
    public StocktakingDetailVO getInfo(String id) {
        return stocktakingDetailMapper.selectJoinOne(StocktakingDetailVO.class, new MPJLambdaWrapper<StocktakingDetail>().eq(StocktakingDetail::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        stocktakingDetailMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(StocktakingDetailEditDTO param) {
        StocktakingDetail detail = BeanUtil.toBean(param, StocktakingDetail.class);
        stocktakingDetailMapper.updateById(detail);
    }

    @Override
    public void add(StocktakingDetailAddDTO param) {
        StocktakingDetail detail = BeanUtil.toBean(param, StocktakingDetail.class);
        stocktakingDetailMapper.insert(detail);
    }
}
