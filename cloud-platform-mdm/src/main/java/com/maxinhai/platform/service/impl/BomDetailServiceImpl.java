package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.technology.BomDetailAddDTO;
import com.maxinhai.platform.dto.technology.BomDetailEditDTO;
import com.maxinhai.platform.dto.technology.BomDetailQueryDTO;
import com.maxinhai.platform.mapper.BomDetailMapper;
import com.maxinhai.platform.po.Material;
import com.maxinhai.platform.po.technology.BomDetail;
import com.maxinhai.platform.service.BomDetailService;
import com.maxinhai.platform.vo.technology.BomDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BomDetailServiceImpl extends ServiceImpl<BomDetailMapper, BomDetail> implements BomDetailService {

    @Resource
    private BomDetailMapper bomDetailMapper;

    @Override
    public Page<BomDetailVO> searchByPage(BomDetailQueryDTO param) {
        Page<BomDetailVO> pageResult = bomDetailMapper.selectJoinPage(param.getPage(), BomDetailVO.class,
                new MPJLambdaWrapper<BomDetail>()
                        .innerJoin(Material.class, Material::getId, BomDetail::getMaterialId)
                        // 查询条件
                        .eq(StrUtil.isNotBlank(param.getBomId()), BomDetail::getBomId, param.getBomId())
                        .eq(StrUtil.isNotBlank(param.getParentId()), BomDetail::getParentId, param.getParentId())
                        // 字段别名
                        .selectAll(BomDetail.class)
                        .selectAs(Material::getCode, BomDetailVO::getMaterialCode)
                        .selectAs(Material::getName, BomDetailVO::getMaterialName)
                        // 排序
                        .orderByDesc(BomDetail::getCreateTime));
        return pageResult;
    }

    @Override
    public BomDetailVO getInfo(String id) {
        return bomDetailMapper.selectJoinOne(BomDetailVO.class,
                new MPJLambdaWrapper<BomDetail>()
                        .innerJoin(Material.class, Material::getId, BomDetail::getMaterialId)
                        // 查询条件
                        .eq(StrUtil.isNotBlank(id), BomDetail::getId, id)
                        // 字段别名
                        .selectAll(BomDetail.class)
                        .selectAs(Material::getCode, BomDetailVO::getMaterialCode)
                        .selectAs(Material::getName, BomDetailVO::getMaterialName));
    }

    @Override
    public void remove(String[] ids) {
        bomDetailMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(BomDetailEditDTO param) {
        BomDetail user = BeanUtil.toBean(param, BomDetail.class);
        bomDetailMapper.updateById(user);
    }

    @Override
    public void add(BomDetailAddDTO param) {
        BomDetail user = BeanUtil.toBean(param, BomDetail.class);
        bomDetailMapper.insert(user);
    }
}
