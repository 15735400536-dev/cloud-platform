package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.DictTypeAddDTO;
import com.maxinhai.platform.dto.DictTypeEditDTO;
import com.maxinhai.platform.dto.DictTypeQueryDTO;
import com.maxinhai.platform.po.DictType;
import com.maxinhai.platform.mapper.DictTypeMapper;
import com.maxinhai.platform.service.DictTypeService;
import com.maxinhai.platform.vo.DictTypeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DictTypeServiceImpl extends ServiceImpl<DictTypeMapper, DictType> implements DictTypeService {

    @Resource
    private DictTypeMapper dictTypeMapper;

    @Override
    public Page<DictTypeVO> searchByPage(DictTypeQueryDTO param) {
        Page<DictTypeVO> pageResult = dictTypeMapper.selectJoinPage(param.getPage(), DictTypeVO.class,
                new MPJLambdaWrapper<DictType>()
                        .like(StrUtil.isNotBlank(param.getDictType()), DictType::getDictType, param.getDictType())
                        .like(StrUtil.isNotBlank(param.getDictLabel()), DictType::getDictLabel, param.getDictLabel())
                        .eq(Objects.nonNull(param.getStatus()), DictType::getStatus, param.getStatus())
                        .orderByDesc(DictType::getCreateTime));
        return pageResult;
    }

    @Override
    public DictTypeVO getInfo(String id) {
        return dictTypeMapper.selectJoinOne(DictTypeVO.class, new MPJLambdaWrapper<DictType>().eq(DictType::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        dictTypeMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(DictTypeEditDTO param) {
        DictType dictType = BeanUtil.toBean(param, DictType.class);
        dictTypeMapper.updateById(dictType);
    }

    @Override
    public void add(DictTypeAddDTO param) {
        DictType dictType = BeanUtil.toBean(param, DictType.class);
        dictTypeMapper.insert(dictType);
    }
}
