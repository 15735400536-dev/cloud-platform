package com.maxinhai.platform.service.model.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.model.FactoryAddDTO;
import com.maxinhai.platform.dto.model.FactoryEditDTO;
import com.maxinhai.platform.dto.model.FactoryQueryDTO;
import com.maxinhai.platform.feign.SystemFeignClient;
import com.maxinhai.platform.mapper.model.FactoryMapper;
import com.maxinhai.platform.po.model.Factory;
import com.maxinhai.platform.service.model.FactoryService;
import com.maxinhai.platform.vo.model.FactoryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FactoryServiceImpl extends ServiceImpl<FactoryMapper, Factory> implements FactoryService {

    @Resource
    private FactoryMapper factoryMapper;
    @Resource
    private SystemFeignClient systemFeignClient;

    @Override
    public Page<FactoryVO> searchByPage(FactoryQueryDTO param) {
        Page<FactoryVO> pageResult = factoryMapper.selectJoinPage(param.getPage(), FactoryVO.class,
                new MPJLambdaWrapper<Factory>()
                        .like(StrUtil.isNotBlank(param.getCode()), Factory::getCode, param.getCode())
                        .like(StrUtil.isNotBlank(param.getName()), Factory::getName, param.getName())
                        .orderByDesc(Factory::getCreateTime));
        return pageResult;
    }

    @Override
    public FactoryVO getInfo(String id) {
        return factoryMapper.selectJoinOne(FactoryVO.class,
                new MPJLambdaWrapper<Factory>()
                        .eq(StrUtil.isNotBlank(id), Factory::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        factoryMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(FactoryEditDTO param) {
        Factory factory = BeanUtil.toBean(param, Factory.class);
        factoryMapper.updateById(factory);
    }

    @Override
    public void add(FactoryAddDTO param) {
        Factory factory = BeanUtil.toBean(param, Factory.class);
        List<String> factoryCodeList = systemFeignClient.generateCode("factory", 1).getData();
        factory.setCode(factoryCodeList.get(0));
        factoryMapper.insert(factory);
    }
}
