package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.MaintenanceItemAddDTO;
import com.maxinhai.platform.dto.MaintenanceItemEditDTO;
import com.maxinhai.platform.dto.MaintenanceItemQueryDTO;
import com.maxinhai.platform.mapper.MaintenanceItemMapper;
import com.maxinhai.platform.po.MaintenanceItem;
import com.maxinhai.platform.service.MaintenanceItemService;
import com.maxinhai.platform.vo.MaintenanceItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MaintenanceItemServiceImpl extends ServiceImpl<MaintenanceItemMapper, MaintenanceItem>
        implements MaintenanceItemService {

    @Resource
    private MaintenanceItemMapper maintenanceItemMapper;

    @Override
    public Page<MaintenanceItemVO> searchByPage(MaintenanceItemQueryDTO param) {
        return maintenanceItemMapper.selectJoinPage(param.getPage(), MaintenanceItemVO.class,
                new MPJLambdaWrapper<MaintenanceItem>()
                        .like(StrUtil.isNotBlank(param.getItemCode()), MaintenanceItem::getItemCode, param.getItemCode())
                        .like(StrUtil.isNotBlank(param.getItemName()), MaintenanceItem::getItemName, param.getItemName())
                        .orderByDesc(MaintenanceItem::getCreateTime));
    }

    @Override
    public MaintenanceItemVO getInfo(String id) {
        return maintenanceItemMapper.selectJoinOne(MaintenanceItemVO.class,
                new MPJLambdaWrapper<MaintenanceItem>()
                        .like(StrUtil.isNotBlank(id), MaintenanceItem::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        maintenanceItemMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(MaintenanceItemEditDTO param) {
        MaintenanceItem item = BeanUtil.toBean(param, MaintenanceItem.class);
        maintenanceItemMapper.updateById(item);
    }

    @Override
    public void add(MaintenanceItemAddDTO param) {
        MaintenanceItem item = BeanUtil.toBean(param, MaintenanceItem.class);
        maintenanceItemMapper.insert(item);
    }
}
