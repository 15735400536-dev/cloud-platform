package com.maxinhai.platform.service.model.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.model.ProductionLineAddDTO;
import com.maxinhai.platform.dto.model.ProductionLineEditDTO;
import com.maxinhai.platform.dto.model.ProductionLineQueryDTO;
import com.maxinhai.platform.feign.SystemFeignClient;
import com.maxinhai.platform.mapper.model.ProductionLineMapper;
import com.maxinhai.platform.po.model.ProductionLine;
import com.maxinhai.platform.po.model.Workshop;
import com.maxinhai.platform.service.model.ProductionLineService;
import com.maxinhai.platform.vo.model.ProductionLineVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductionLineServiceImpl extends ServiceImpl<ProductionLineMapper, ProductionLine> implements ProductionLineService {

    @Resource
    private ProductionLineMapper productionLineMapper;
    @Resource
    private SystemFeignClient systemFeignClient;

    @Override
    public Page<ProductionLineVO> searchByPage(ProductionLineQueryDTO param) {
        return productionLineMapper.selectJoinPage(param.getPage(), ProductionLineVO.class,
                new MPJLambdaWrapper<ProductionLine>()
                        .innerJoin(Workshop.class, Workshop::getId, ProductionLine::getWorkshopId)
                        // 查询条件
                        .like(StrUtil.isNotBlank(param.getCode()), ProductionLine::getCode, param.getCode())
                        .like(StrUtil.isNotBlank(param.getName()), ProductionLine::getName, param.getName())
                        // 字段别名
                        .selectAll(ProductionLine.class)
                        .selectAs(Workshop::getCode, ProductionLineVO::getWorkshopCode)
                        .selectAs(Workshop::getName, ProductionLineVO::getWorkshopName)
                        // 排序
                        .orderByDesc(ProductionLine::getCreateTime));
    }

    @Override
    public ProductionLineVO getInfo(String id) {
        return null;
    }

    @Override
    public void remove(String[] ids) {
        productionLineMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(ProductionLineEditDTO param) {
        ProductionLine productionLine = BeanUtil.toBean(param, ProductionLine.class);
        productionLineMapper.updateById(productionLine);
    }

    @Override
    public void add(ProductionLineAddDTO param) {
        ProductionLine productionLine = BeanUtil.toBean(param, ProductionLine.class);
        List<String> codeList = systemFeignClient.generateCode("productionLine", 1).getData();
        productionLine.setCode(codeList.get(0));
        productionLineMapper.insert(productionLine);
    }
}
