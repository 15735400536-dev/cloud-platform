package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.bo.MaterialExcelBO;
import com.maxinhai.platform.dto.MaterialAddDTO;
import com.maxinhai.platform.dto.MaterialEditDTO;
import com.maxinhai.platform.dto.MaterialQueryDTO;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.listener.MaterialExcelListener;
import com.maxinhai.platform.mapper.MaterialMapper;
import com.maxinhai.platform.po.Material;
import com.maxinhai.platform.po.MaterialType;
import com.maxinhai.platform.service.MaterialService;
import com.maxinhai.platform.vo.MaterialVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MaterialServiceImpl extends ServiceImpl<MaterialMapper, Material> implements MaterialService {

    @Resource
    private MaterialMapper materialMapper;
    @Resource
    private MaterialExcelListener materialExcelListener;

    @Override
    public Page<MaterialVO> searchByPage(MaterialQueryDTO param) {
        Page<MaterialVO> pageResult = materialMapper.selectJoinPage(param.getPage(), MaterialVO.class,
                new MPJLambdaWrapper<Material>()
                        .innerJoin(MaterialType.class, MaterialType::getId, Material::getMaterialTypeId)
                        // 查询条件
                        .like(StrUtil.isNotBlank(param.getCode()), Material::getCode, param.getCode())
                        .like(StrUtil.isNotBlank(param.getName()), Material::getName, param.getName())
                        // 字段别名
                        .selectAll(Material.class)
                        .selectAs(MaterialType::getCode, MaterialVO::getMaterialTypeCode)
                        .selectAs(MaterialType::getName, MaterialVO::getMaterialTypeName)
                        // 排序
                        .orderByDesc(Material::getCreateTime));
        return pageResult;
    }

    @Override
    public MaterialVO getInfo(String id) {
        return materialMapper.selectJoinOne(MaterialVO.class,
                new MPJLambdaWrapper<Material>()
                        .innerJoin(MaterialType.class, MaterialType::getId, Material::getMaterialTypeId)
                        // 查询条件
                        .eq(Material::getId, id)
                        // 字段别名
                        .selectAll(Material.class)
                        .selectAs(MaterialType::getCode, MaterialVO::getMaterialTypeCode)
                        .selectAs(MaterialType::getName, MaterialVO::getMaterialTypeName));
    }

    @Override
    public void remove(String[] ids) {
        materialMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(MaterialEditDTO param) {
        Material material = BeanUtil.toBean(param, Material.class);
        materialMapper.updateById(material);
    }

    @Override
    public void add(MaterialAddDTO param) {
        Material material = BeanUtil.toBean(param, Material.class);
        materialMapper.insert(material);
    }

    @Override
    public void importExcel(MultipartFile file) {
        try {
            // 调用EasyExcel读取文件
            EasyExcel.read(file.getInputStream(), MaterialExcelBO.class, materialExcelListener)
                    .sheet() // 读取第一个sheet
                    .doRead(); // 执行读取操作
        } catch (IOException e) {
            log.error("Excel数据导入失败", e);
            throw new BusinessException("Excel数据导入失败：" + e.getMessage());
        }
    }

}
