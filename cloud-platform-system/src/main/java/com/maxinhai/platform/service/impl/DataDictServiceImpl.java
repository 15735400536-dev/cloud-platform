package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.DataDictAddDTO;
import com.maxinhai.platform.dto.DataDictEditDTO;
import com.maxinhai.platform.dto.DataDictQueryDTO;
import com.maxinhai.platform.po.DataDict;
import com.maxinhai.platform.mapper.DataDictMapper;
import com.maxinhai.platform.service.DataDictService;
import com.maxinhai.platform.vo.DataDictVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DataDictServiceImpl extends ServiceImpl<DataDictMapper, DataDict> implements DataDictService {

    @Resource
    private DataDictMapper dataDictMapper;

    @Override
    public Page<DataDictVO> searchByPage(DataDictQueryDTO param) {
        return dataDictMapper.selectJoinPage(param.getPage(), DataDictVO.class,
                new MPJLambdaWrapper<DataDict>()
                        .like(StrUtil.isNotBlank(param.getDictType()), DataDict::getDictType, param.getDictType())
                        .orderByDesc(DataDict::getCreateTime));
    }

    @Override
    public DataDictVO getInfo(String id) {
        return dataDictMapper.selectJoinOne(DataDictVO.class, new MPJLambdaWrapper<DataDict>().eq(DataDict::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        dataDictMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(DataDictEditDTO param) {
        DataDict user = BeanUtil.toBean(param, DataDict.class);
        dataDictMapper.updateById(user);
    }

    @Override
    public void add(DataDictAddDTO param) {
        DataDict user = BeanUtil.toBean(param, DataDict.class);
        dataDictMapper.insert(user);
    }
}
