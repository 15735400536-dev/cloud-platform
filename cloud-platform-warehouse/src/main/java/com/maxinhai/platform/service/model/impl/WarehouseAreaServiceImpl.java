package com.maxinhai.platform.service.model.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.model.WarehouseAreaAddDTO;
import com.maxinhai.platform.dto.model.WarehouseAreaEditDTO;
import com.maxinhai.platform.dto.model.WarehouseAreaQueryDTO;
import com.maxinhai.platform.mapper.model.WarehouseAreaMapper;
import com.maxinhai.platform.po.model.Warehouse;
import com.maxinhai.platform.po.model.WarehouseArea;
import com.maxinhai.platform.service.model.WarehouseAreaService;
import com.maxinhai.platform.vo.model.WarehouseAreaVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WarehouseAreaServiceImpl extends ServiceImpl<WarehouseAreaMapper, WarehouseArea>
        implements WarehouseAreaService {

    @Resource
    private WarehouseAreaMapper areaMapper;

    @Override
    public Page<WarehouseAreaVO> searchByPage(WarehouseAreaQueryDTO param) {
        Page<WarehouseAreaVO> pageResult = areaMapper.selectJoinPage(param.getPage(), WarehouseAreaVO.class,
                new MPJLambdaWrapper<WarehouseArea>()
                        .innerJoin(Warehouse.class, Warehouse::getId, WarehouseArea::getWarehouseId)
                        // 查询条件
                        .eq(StrUtil.isNotBlank(param.getWarehouseId()), WarehouseArea::getWarehouseId, param.getWarehouseId())
                        .like(StrUtil.isNotBlank(param.getCode()), WarehouseArea::getCode, param.getCode())
                        .like(StrUtil.isNotBlank(param.getName()), WarehouseArea::getName, param.getName())
                        // 字段别名
                        .selectAll(WarehouseArea.class)
                        .selectAs(Warehouse::getCode, WarehouseAreaVO::getWarehouseCode)
                        .selectAs(Warehouse::getName, WarehouseAreaVO::getWarehouseName)
                        // 排序
                        .orderByDesc(WarehouseArea::getCreateTime));
        return pageResult;
    }

    @Override
    public WarehouseAreaVO getInfo(String id) {
        return areaMapper.selectJoinOne(WarehouseAreaVO.class, new MPJLambdaWrapper<WarehouseArea>()
                .innerJoin(Warehouse.class, Warehouse::getId, WarehouseArea::getWarehouseId)
                // 字段别名
                .selectAll(WarehouseArea.class)
                .selectAs(Warehouse::getCode, WarehouseAreaVO::getWarehouseCode)
                .selectAs(Warehouse::getName, WarehouseAreaVO::getWarehouseName)
                // 查询条件
                .eq(WarehouseArea::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        areaMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(WarehouseAreaEditDTO param) {
        WarehouseArea area = BeanUtil.toBean(param, WarehouseArea.class);
        areaMapper.updateById(area);
    }

    @Override
    public void add(WarehouseAreaAddDTO param) {
        WarehouseArea area = BeanUtil.toBean(param, WarehouseArea.class);
        areaMapper.insert(area);
    }
}
