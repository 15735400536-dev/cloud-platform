package com.maxinhai.platform.service.model.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.bo.WarehouseExcelBO;
import com.maxinhai.platform.dto.model.WarehouseAddDTO;
import com.maxinhai.platform.dto.model.WarehouseEditDTO;
import com.maxinhai.platform.dto.model.WarehouseQueryDTO;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.feign.SystemFeignClient;
import com.maxinhai.platform.listener.WarehouseExcelListener;
import com.maxinhai.platform.mapper.model.WarehouseAreaMapper;
import com.maxinhai.platform.mapper.model.WarehouseLocationMapper;
import com.maxinhai.platform.mapper.model.WarehouseMapper;
import com.maxinhai.platform.mapper.model.WarehouseRackMapper;
import com.maxinhai.platform.po.model.Warehouse;
import com.maxinhai.platform.po.model.WarehouseArea;
import com.maxinhai.platform.po.model.WarehouseLocation;
import com.maxinhai.platform.po.model.WarehouseRack;
import com.maxinhai.platform.service.model.WarehouseService;
import com.maxinhai.platform.utils.TreeNodeUtils;
import com.maxinhai.platform.vo.model.WarehouseTreeVO;
import com.maxinhai.platform.vo.model.WarehouseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WarehouseServiceImpl extends ServiceImpl<WarehouseMapper, Warehouse> implements WarehouseService {

    @Resource
    private WarehouseMapper warehouseMapper;
    @Resource
    private WarehouseAreaMapper areaMapper;
    @Resource
    private WarehouseRackMapper rackMapper;
    @Resource
    private WarehouseLocationMapper locationMapper;
    @Resource
    private SystemFeignClient systemFeignClient;
    @Resource
    private WarehouseExcelListener warehouseExcelListener;
    @Resource
    @Qualifier("ioIntensiveExecutor")
    private Executor ioIntensiveExecutor;

    @Override
    public Page<WarehouseVO> searchByPage(WarehouseQueryDTO param) {
        Page<WarehouseVO> pageResult = warehouseMapper.selectJoinPage(param.getPage(), WarehouseVO.class,
                new MPJLambdaWrapper<Warehouse>()
                        .like(StrUtil.isNotBlank(param.getCode()), Warehouse::getCode, param.getCode())
                        .like(StrUtil.isNotBlank(param.getName()), Warehouse::getName, param.getName())
                        .orderByDesc(Warehouse::getCreateTime));
        return pageResult;
    }

    @Override
    public WarehouseVO getInfo(String id) {
        return warehouseMapper.selectJoinOne(WarehouseVO.class, new MPJLambdaWrapper<Warehouse>().eq(Warehouse::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        warehouseMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(WarehouseEditDTO param) {
        Warehouse warehouse = BeanUtil.toBean(param, Warehouse.class);
        warehouseMapper.updateById(warehouse);
    }

    @Override
    public void add(WarehouseAddDTO param) {
        List<String> codeList = systemFeignClient.generateCode("wms_warehouse", 1).getData();
        Warehouse warehouse = BeanUtil.toBean(param, Warehouse.class);
        warehouse.setCode(codeList.get(0));
        warehouseMapper.insert(warehouse);
    }

    @Override
    public void importExcel(MultipartFile file) {
        try {
            // 调用EasyExcel读取文件
            EasyExcel.read(file.getInputStream(), WarehouseExcelBO.class, warehouseExcelListener)
                    .sheet() // 读取第一个sheet
                    .doRead(); // 执行读取操作
        } catch (IOException e) {
            log.error("Excel数据导入失败", e);
            throw new BusinessException("Excel数据导入失败：" + e.getMessage());
        }
    }

    @Override
    public List<WarehouseTreeVO> getTree() {
        // 1. 并行查询4张表（使用CompletableFuture）
        CompletableFuture<List<Warehouse>> warehouseFuture = CompletableFuture.supplyAsync(
                () -> warehouseMapper.selectList(new LambdaQueryWrapper<Warehouse>()
                        .select(Warehouse::getId, Warehouse::getCode, Warehouse::getName)),
                ioIntensiveExecutor
        );

        CompletableFuture<List<WarehouseArea>> areaFuture = CompletableFuture.supplyAsync(
                () -> areaMapper.selectList(new LambdaQueryWrapper<WarehouseArea>()
                        .select(WarehouseArea::getId, WarehouseArea::getCode, WarehouseArea::getName, WarehouseArea::getWarehouseId)),
                ioIntensiveExecutor
        );

        CompletableFuture<List<WarehouseRack>> rackFuture = CompletableFuture.supplyAsync(
                () -> rackMapper.selectList(new LambdaQueryWrapper<WarehouseRack>()
                        .select(WarehouseRack::getId, WarehouseRack::getCode, WarehouseRack::getName, WarehouseRack::getAreaId)),
                ioIntensiveExecutor
        );

        CompletableFuture<List<WarehouseLocation>> locationFuture = CompletableFuture.supplyAsync(
                () -> locationMapper.selectList(new LambdaQueryWrapper<WarehouseLocation>()
                        .select(WarehouseLocation::getId, WarehouseLocation::getCode, WarehouseLocation::getName, WarehouseLocation::getRackId)),
                ioIntensiveExecutor
        );

        // 2. 等待所有查询完成并获取结果
        CompletableFuture.allOf(warehouseFuture, areaFuture, rackFuture, locationFuture).join();

        // 3.合并数据
        List<WarehouseTreeVO> treeVOList = new ArrayList<>();
        try {
            treeVOList.addAll(warehouseFuture.get().stream().map(WarehouseTreeVO::convert).collect(Collectors.toList()));
            treeVOList.addAll(areaFuture.get().stream().map(WarehouseTreeVO::convert).collect(Collectors.toList()));
            treeVOList.addAll(rackFuture.get().stream().map(WarehouseTreeVO::convert).collect(Collectors.toList()));
            treeVOList.addAll(locationFuture.get().stream().map(WarehouseTreeVO::convert).collect(Collectors.toList()));
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return TreeNodeUtils.buildTree(treeVOList, "0");
    }
}
