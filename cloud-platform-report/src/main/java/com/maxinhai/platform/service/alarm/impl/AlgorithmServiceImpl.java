package com.maxinhai.platform.service.alarm.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.google.common.collect.Maps;
import com.maxinhai.platform.dto.alarm.AlgorithmAddDTO;
import com.maxinhai.platform.dto.alarm.AlgorithmEditDTO;
import com.maxinhai.platform.dto.alarm.AlgorithmQueryDTO;
import com.maxinhai.platform.mapper.alarm.AlgorithmMapper;
import com.maxinhai.platform.po.alarm.Algorithm;
import com.maxinhai.platform.service.alarm.AlgorithmService;
import com.maxinhai.platform.vo.alarm.AlgorithmVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class AlgorithmServiceImpl extends ServiceImpl<AlgorithmMapper, Algorithm> implements AlgorithmService {

    private final AlgorithmMapper algorithmMapper;

    @Override
    public Page<AlgorithmVO> searchByPage(AlgorithmQueryDTO param) {
        return algorithmMapper.selectJoinPage(param.getPage(), AlgorithmVO.class,
                new MPJLambdaWrapper<Algorithm>()
                        .eq(Objects.nonNull(param.getEnable()), Algorithm::getEnable, param.getEnable())
                        .like(StrUtil.isNotBlank(param.getKey()), Algorithm::getKey, param.getKey())
                        .like(StrUtil.isNotBlank(param.getName()), Algorithm::getName, param.getName())
                        .orderByDesc(Algorithm::getCreateTime));
    }

    @Override
    public AlgorithmVO getInfo(String id) {
        return algorithmMapper.selectJoinOne(AlgorithmVO.class, new MPJLambdaWrapper<Algorithm>()
                .eq(Algorithm::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        algorithmMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(AlgorithmEditDTO param) {
        algorithmMapper.updateById(BeanUtil.copyProperties(param, Algorithm.class));
    }

    @Override
    public void add(AlgorithmAddDTO param) {
        algorithmMapper.insert(BeanUtil.copyProperties(param, Algorithm.class));
    }

    @PostConstruct
    public void initData() {
        Long count = algorithmMapper.selectCount(null);
        if (count > 0) {
            return;
        }
        Map<String, String> dataMap = Maps.newLinkedHashMap();
        dataMap.put("person", "人员检测");
        dataMap.put("fire", "火灾检测");
        dataMap.put("post", "脱岗检测");
        dataMap.put("hat", "安全帽检测");
        dataMap.forEach((key, name) -> {
            algorithmMapper.insert(new Algorithm(key, name, Boolean.TRUE));
        });
    }

}
