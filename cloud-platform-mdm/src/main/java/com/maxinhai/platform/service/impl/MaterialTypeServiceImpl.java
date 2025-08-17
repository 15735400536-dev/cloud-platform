package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.MaterialTypeAddDTO;
import com.maxinhai.platform.dto.MaterialTypeEditDTO;
import com.maxinhai.platform.dto.MaterialTypeQueryDTO;
import com.maxinhai.platform.mapper.MaterialTypeMapper;
import com.maxinhai.platform.po.MaterialType;
import com.maxinhai.platform.service.MaterialTypeService;
import com.maxinhai.platform.utils.TreeNodeUtils;
import com.maxinhai.platform.vo.MaterialTypeTreeVO;
import com.maxinhai.platform.vo.MaterialTypeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MaterialTypeServiceImpl extends ServiceImpl<MaterialTypeMapper, MaterialType> implements MaterialTypeService {

    @Resource
    private MaterialTypeMapper materialTypeMapper;

    @Override
    public Page<MaterialTypeVO> searchByPage(MaterialTypeQueryDTO param) {
        Page<MaterialTypeVO> pageResult = materialTypeMapper.selectJoinPage(param.getPage(), MaterialTypeVO.class,
                new MPJLambdaWrapper<MaterialType>()
                        // 查询条件
                        .like(StrUtil.isNotBlank(param.getCode()), MaterialType::getCode, param.getCode())
                        .like(StrUtil.isNotBlank(param.getName()), MaterialType::getName, param.getName())
                        // 字段别名
                        .selectAll(MaterialType.class)
                        // 排序
                        .orderByDesc(MaterialType::getCreateTime));
        return pageResult;
    }

    @Override
    public MaterialTypeVO getInfo(String id) {
        return materialTypeMapper.selectJoinOne(MaterialTypeVO.class,
                new MPJLambdaWrapper<MaterialType>()
                        // 查询条件
                        .eq(MaterialType::getId, id)
                        // 字段别名
                        .selectAll(MaterialType.class));
    }

    @Override
    public void remove(String[] ids) {
        materialTypeMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(MaterialTypeEditDTO param) {
        MaterialType materialType = BeanUtil.toBean(param, MaterialType.class);
        materialTypeMapper.updateById(materialType);
    }

    @Override
    public void add(MaterialTypeAddDTO param) {
        MaterialType materialType = BeanUtil.toBean(param, MaterialType.class);
        materialTypeMapper.insert(materialType);
    }

    @Override
    public List<MaterialTypeTreeVO> getMaterialTypeTree() {
        // 查询启用菜单集合
        List<MaterialType> typeList = materialTypeMapper.selectList(new LambdaQueryWrapper<MaterialType>()
                .select(MaterialType::getId, MaterialType::getParentId, MaterialType::getName));
        List<MaterialTypeTreeVO> treeVOList = typeList.stream()
                .map(type -> MaterialTypeTreeVO.convert(type))
                .collect(Collectors.toList());
        // 按照父级ID构建树状结构
        List<MaterialTypeTreeVO> typeTree = TreeNodeUtils.buildTree(treeVOList, "0");
        return typeTree;
    }

    //@PostConstruct
    public void initData() {
        MaterialType materialType = new MaterialType();
        materialType.setCode(String.format("编码%s", DateUtil.format(new Date(), "yyyyMMddHHmmss")));
        materialType.setName(String.format("名称%s", DateUtil.format(new Date(), "yyyyMMddHHmmss")));
        materialType.setDescription("物料描述");
        materialType.setParentId("0");
        materialTypeMapper.insert(materialType);
    }
}
