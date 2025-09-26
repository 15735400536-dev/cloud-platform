package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.technology.RoutingAddDTO;
import com.maxinhai.platform.dto.technology.RoutingEditDTO;
import com.maxinhai.platform.dto.technology.RoutingQueryDTO;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.mapper.RoutingMapper;
import com.maxinhai.platform.mapper.RoutingOperationRelMapper;
import com.maxinhai.platform.po.Product;
import com.maxinhai.platform.po.technology.Routing;
import com.maxinhai.platform.po.technology.RoutingOperationRel;
import com.maxinhai.platform.service.CommonCodeCheckService;
import com.maxinhai.platform.service.RoutingOperationRelService;
import com.maxinhai.platform.service.RoutingService;
import com.maxinhai.platform.vo.technology.RoutingVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RoutingServiceImpl extends ServiceImpl<RoutingMapper, Routing> implements RoutingService {

    @Resource
    private RoutingMapper routingMapper;
    @Resource
    private RoutingOperationRelMapper relMapper;
    @Resource
    private RoutingOperationRelService relService;
    @Resource
    private CommonCodeCheckService commonCodeCheckService;

    @Override
    public Page<RoutingVO> searchByPage(RoutingQueryDTO param) {
        Page<RoutingVO> pageResult = routingMapper.selectJoinPage(param.getPage(), RoutingVO.class,
                new MPJLambdaWrapper<Routing>()
                        .innerJoin(Product.class, Product::getId, Routing::getProductId)
                        // 查询条件
                        .like(StrUtil.isNotBlank(param.getCode()), Routing::getCode, param.getCode())
                        .like(StrUtil.isNotBlank(param.getName()), Routing::getName, param.getName())
                        // 字段别名
                        .selectAll(Routing.class)
                        .selectAs(Product::getCode, RoutingVO::getProductCode)
                        .selectAs(Product::getName, RoutingVO::getProductName)
                        // 排序
                        .orderByDesc(Routing::getCreateTime));
        return pageResult;
    }

    @Override
    public RoutingVO getInfo(String id) {
        return routingMapper.selectJoinOne(RoutingVO.class,
                new MPJLambdaWrapper<Routing>()
                        .innerJoin(Product.class, Product::getId, Routing::getProductId)
                        // 查询条件
                        .like(Routing::getId, id)
                        // 字段别名
                        .selectAll(Routing.class)
                        .selectAs(Product::getCode, RoutingVO::getProductCode)
                        .selectAs(Product::getName, RoutingVO::getProductName));
    }

    @Override
    public void remove(String[] ids) {
        routingMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(RoutingEditDTO param) {
        Routing routing = BeanUtil.toBean(param, Routing.class);
        routingMapper.updateById(routing);
    }

    @Override
    public void add(RoutingAddDTO param) {
        boolean unique = commonCodeCheckService.isCodeUnique(Routing.class, Routing::getCode, param.getCode());
        if (!unique) {
            throw new BusinessException("工艺路线【" + param.getCode() + "】已存在!");
        }
        Routing routing = BeanUtil.toBean(param, Routing.class);
        routingMapper.insert(routing);
    }

    @Override
    public void binding(String routingId, List<String> operationIds) {
        // 删除旧的关联关系
        relMapper.delete(new LambdaQueryWrapper<RoutingOperationRel>().eq(RoutingOperationRel::getRoutingId, routingId));
        // 新建新的关联关系
        List<RoutingOperationRel> relList = new ArrayList<>();
        for (int i = 0; i < operationIds.size(); i++) {
            RoutingOperationRel rel = new RoutingOperationRel();
            rel.setRoutingId(routingId);
            rel.setOperationId(operationIds.get(i));
            rel.setSort(i + 1);
            relList.add(rel);
        }
        relService.saveBatch(relList);
    }
}
