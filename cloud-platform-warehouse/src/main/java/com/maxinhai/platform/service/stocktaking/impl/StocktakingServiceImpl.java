package com.maxinhai.platform.service.stocktaking.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.stocktaking.StocktakingAddDTO;
import com.maxinhai.platform.dto.stocktaking.StocktakingEditDTO;
import com.maxinhai.platform.dto.stocktaking.StocktakingQueryDTO;
import com.maxinhai.platform.feign.SystemFeignClient;
import com.maxinhai.platform.mapper.stocktaking.StocktakingMapper;
import com.maxinhai.platform.po.stocktaking.Stocktaking;
import com.maxinhai.platform.service.stocktaking.StocktakingService;
import com.maxinhai.platform.vo.stocktaking.StocktakingVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StocktakingServiceImpl extends ServiceImpl<StocktakingMapper, Stocktaking> implements StocktakingService {

    @Resource
    private StocktakingMapper stocktakingMapper;
    @Resource
    private SystemFeignClient systemFeignClient;

    @Override
    public Page<StocktakingVO> searchByPage(StocktakingQueryDTO param) {
        Page<StocktakingVO> pageResult = stocktakingMapper.selectJoinPage(param.getPage(), StocktakingVO.class,
                new MPJLambdaWrapper<Stocktaking>()
                        .like(StrUtil.isNotBlank(param.getStocktakingNo()), Stocktaking::getStocktakingNo, param.getStocktakingNo())
                        .like(StrUtil.isNotBlank(param.getWarehouseId()), Stocktaking::getWarehouseId, param.getWarehouseId())
                        .orderByDesc(Stocktaking::getCreateTime));
        return pageResult;
    }

    @Override
    public StocktakingVO getInfo(String id) {
        return stocktakingMapper.selectJoinOne(StocktakingVO.class, new MPJLambdaWrapper<Stocktaking>().eq(StrUtil.isNotBlank(id), Stocktaking::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        stocktakingMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(StocktakingEditDTO param) {
        Stocktaking stocktaking = BeanUtil.toBean(param, Stocktaking.class);
        stocktakingMapper.updateById(stocktaking);
    }

    @Override
    public void add(StocktakingAddDTO param) {
        List<String> codeList = systemFeignClient.generateCode("stocktaking", 1).getData();
        Stocktaking stocktaking = BeanUtil.toBean(param, Stocktaking.class);
        stocktaking.setStocktakingNo(codeList.get(0));
        stocktakingMapper.insert(stocktaking);
    }
}
