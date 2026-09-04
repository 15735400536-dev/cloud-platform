package com.maxinhai.platform.service.order.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.order.IssueOrderDetailAddDTO;
import com.maxinhai.platform.dto.order.IssueOrderDetailEditDTO;
import com.maxinhai.platform.dto.order.IssueOrderDetailQueryDTO;
import com.maxinhai.platform.mapper.inventory.InventoryMapper;
import com.maxinhai.platform.mapper.order.IssueOrderDetailMapper;
import com.maxinhai.platform.mapper.order.IssueOrderMapper;
import com.maxinhai.platform.po.Material;
import com.maxinhai.platform.po.model.WarehouseLocation;
import com.maxinhai.platform.po.order.IssueOrder;
import com.maxinhai.platform.po.order.IssueOrderDetail;
import com.maxinhai.platform.service.order.IssueOrderDetailService;
import com.maxinhai.platform.vo.order.IssueOrderDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
public class IssueOrderDetailServiceImpl extends ServiceImpl<IssueOrderDetailMapper, IssueOrderDetail> implements IssueOrderDetailService {

    @Resource
    private IssueOrderDetailMapper issueOrderDetailMapper;
    @Resource
    private IssueOrderMapper issueOrderMapper;
    @Resource
    private InventoryMapper inventoryMapper;

    @Override
    public Page<IssueOrderDetailVO> searchByPage(IssueOrderDetailQueryDTO param) {
        return issueOrderDetailMapper.selectJoinPage(param.getPage(), IssueOrderDetailVO.class,
                new MPJLambdaWrapper<IssueOrderDetail>()
                        .leftJoin(Material.class, Material::getId, IssueOrderDetail::getMaterialId)
                        .leftJoin(WarehouseLocation.class, WarehouseLocation::getId, IssueOrderDetail::getLocationId)
                        .like(StrUtil.isNotBlank(param.getIssueOrderId()), IssueOrderDetail::getIssueOrderId, param.getIssueOrderId())
                        .orderByDesc(IssueOrderDetail::getCreateTime)
                        .selectAll(IssueOrderDetail.class)
                        .selectAs(Material::getCode, IssueOrderDetailVO::getMaterialCode)
                        .selectAs(Material::getName, IssueOrderDetailVO::getMaterialName)
                        .selectAs(WarehouseLocation::getCode, IssueOrderDetailVO::getLocationCode)
                        .selectAs(WarehouseLocation::getName, IssueOrderDetailVO::getLocationName));
    }

    @Override
    public IssueOrderDetailVO getInfo(String id) {
        return issueOrderDetailMapper.selectJoinOne(IssueOrderDetailVO.class,
                new MPJLambdaWrapper<IssueOrderDetail>()
                        .leftJoin(Material.class, Material::getId, IssueOrderDetail::getMaterialId)
                        .leftJoin(WarehouseLocation.class, WarehouseLocation::getId, IssueOrderDetail::getLocationId)
                        .eq(IssueOrderDetail::getId, id)
                        .selectAll(IssueOrderDetail.class)
                        .selectAs(Material::getCode, IssueOrderDetailVO::getMaterialCode)
                        .selectAs(Material::getName, IssueOrderDetailVO::getMaterialName)
                        .selectAs(WarehouseLocation::getCode, IssueOrderDetailVO::getLocationCode)
                        .selectAs(WarehouseLocation::getName, IssueOrderDetailVO::getLocationName));
    }

    @Override
    public void remove(String[] ids) {
        issueOrderDetailMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(IssueOrderDetailEditDTO param) {
        IssueOrderDetail detail = BeanUtil.toBean(param, IssueOrderDetail.class);
        issueOrderDetailMapper.updateById(detail);
    }

    @Override
    public void add(IssueOrderDetailAddDTO param) {
        IssueOrderDetail detail = BeanUtil.toBean(param, IssueOrderDetail.class);
        issueOrderDetailMapper.insert(detail);
    }

    @Override
    public void issue(String id) {
        IssueOrderDetail issueOrderDetail = issueOrderDetailMapper.selectById(id);
        IssueOrder issueOrder = issueOrderMapper.selectById(issueOrderDetail.getIssueOrderId());
    }
}
