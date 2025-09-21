package com.maxinhai.platform.service.model.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
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
import com.maxinhai.platform.mapper.model.WarehouseMapper;
import com.maxinhai.platform.po.model.Warehouse;
import com.maxinhai.platform.service.model.WarehouseService;
import com.maxinhai.platform.vo.model.WarehouseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WarehouseServiceImpl extends ServiceImpl<WarehouseMapper, Warehouse> implements WarehouseService {

    @Resource
    private WarehouseMapper warehouseMapper;
    @Resource
    private SystemFeignClient systemFeignClient;
    @Resource
    private WarehouseExcelListener warehouseExcelListener;

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
}
