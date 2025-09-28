package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.MaterialTypeAddDTO;
import com.maxinhai.platform.dto.MaterialTypeEditDTO;
import com.maxinhai.platform.dto.MaterialTypeQueryDTO;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.mapper.MaterialTypeMapper;
import com.maxinhai.platform.po.MaterialType;
import com.maxinhai.platform.service.CommonCodeCheckService;
import com.maxinhai.platform.service.MaterialTypeService;
import com.maxinhai.platform.utils.TreeNodeUtils;
import com.maxinhai.platform.vo.MaterialTypeTreeVO;
import com.maxinhai.platform.vo.MaterialTypeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MaterialTypeServiceImpl extends ServiceImpl<MaterialTypeMapper, MaterialType> implements MaterialTypeService {

    @Resource
    private MaterialTypeMapper materialTypeMapper;
    @Resource
    private CommonCodeCheckService commonCodeCheckService;

    @Override
    public Page<MaterialTypeVO> searchByPage(MaterialTypeQueryDTO param) {
        return materialTypeMapper.selectJoinPage(param.getPage(), MaterialTypeVO.class,
                new MPJLambdaWrapper<MaterialType>()
                        // 查询条件
                        .like(StrUtil.isNotBlank(param.getCode()), MaterialType::getCode, param.getCode())
                        .like(StrUtil.isNotBlank(param.getName()), MaterialType::getName, param.getName())
                        // 字段别名
                        .selectAll(MaterialType.class)
                        // 排序
                        .orderByDesc(MaterialType::getCreateTime));
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
        boolean unique = commonCodeCheckService.isCodeUnique(MaterialType.class, MaterialType::getCode, param.getCode());
        if (!unique) {
            throw new BusinessException("物料类型【" + param.getCode() + "】已存在!");
        }
        MaterialType materialType = BeanUtil.toBean(param, MaterialType.class);
        materialTypeMapper.insert(materialType);
    }

    @Override
    public List<MaterialTypeTreeVO> getMaterialTypeTree() {
        // 查询启用菜单集合
        List<MaterialType> typeList = materialTypeMapper.selectList(new LambdaQueryWrapper<MaterialType>()
                .select(MaterialType::getId, MaterialType::getParentId, MaterialType::getName));
        List<MaterialTypeTreeVO> treeVOList = typeList.stream()
                .map(MaterialTypeTreeVO::convert)
                .collect(Collectors.toList());
        // 按照父级ID构建树状结构
        return TreeNodeUtils.buildTree(treeVOList, "0");
    }
}
