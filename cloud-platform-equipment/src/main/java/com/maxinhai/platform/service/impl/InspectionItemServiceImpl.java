package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.InspectionItemAddDTO;
import com.maxinhai.platform.dto.InspectionItemEditDTO;
import com.maxinhai.platform.dto.InspectionItemQueryDTO;
import com.maxinhai.platform.mapper.InspectionItemMapper;
import com.maxinhai.platform.po.InspectionItem;
import com.maxinhai.platform.service.InspectionItemService;
import com.maxinhai.platform.vo.InspectionItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InspectionItemServiceImpl extends ServiceImpl<InspectionItemMapper, InspectionItem> implements InspectionItemService {

    @Resource
    private InspectionItemMapper inspectionItemMapper;

    @Override
    public Page<InspectionItemVO> searchByPage(InspectionItemQueryDTO param) {
        Page<InspectionItemVO> pageResult = inspectionItemMapper.selectJoinPage(param.getPage(), InspectionItemVO.class,
                new MPJLambdaWrapper<InspectionItem>()
                        .like(StrUtil.isNotBlank(param.getItemCode()), InspectionItem::getItemCode, param.getItemCode())
                        .like(StrUtil.isNotBlank(param.getItemName()), InspectionItem::getItemName, param.getItemName())
                        .orderByDesc(InspectionItem::getCreateTime));
        return pageResult;
    }

    @Override
    public InspectionItemVO getInfo(String id) {
        return inspectionItemMapper.selectJoinOne(InspectionItemVO.class,
                new MPJLambdaWrapper<InspectionItem>()
                        .eq(StrUtil.isNotBlank(id), InspectionItem::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        inspectionItemMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(InspectionItemEditDTO param) {
        InspectionItem item = BeanUtil.toBean(param, InspectionItem.class);
        inspectionItemMapper.updateById(item);
    }

    @Override
    public void add(InspectionItemAddDTO param) {
        InspectionItem item = BeanUtil.toBean(param, InspectionItem.class);
        inspectionItemMapper.insert(item);
    }
}
