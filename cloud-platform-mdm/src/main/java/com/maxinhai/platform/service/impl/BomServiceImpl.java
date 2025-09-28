package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.bo.BomExcelBO;
import com.maxinhai.platform.dto.technology.BomAddDTO;
import com.maxinhai.platform.dto.technology.BomEditDTO;
import com.maxinhai.platform.dto.technology.BomQueryDTO;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.listener.BomExcelListener;
import com.maxinhai.platform.mapper.BomMapper;
import com.maxinhai.platform.po.Product;
import com.maxinhai.platform.po.technology.Bom;
import com.maxinhai.platform.service.BomService;
import com.maxinhai.platform.service.CommonCodeCheckService;
import com.maxinhai.platform.vo.technology.BomVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BomServiceImpl extends ServiceImpl<BomMapper, Bom> implements BomService {

    @Resource
    private BomMapper bomMapper;
    @Resource
    private CommonCodeCheckService commonCodeCheckService;
    @Resource
    private BomExcelListener bomExcelListener;

    @Override
    public Page<BomVO> searchByPage(BomQueryDTO param) {
        Page<BomVO> pageResult = bomMapper.selectJoinPage(param.getPage(), BomVO.class,
                new MPJLambdaWrapper<Bom>()
                        .innerJoin(Product.class, Product::getId, Bom::getProductId)
                        // 查询条件
                        .like(StrUtil.isNotBlank(param.getCode()), Bom::getCode, param.getCode())
                        .like(StrUtil.isNotBlank(param.getName()), Bom::getName, param.getName())
                        // 字段别名
                        .selectAll(Bom.class)
                        .selectAs(Product::getCode, BomVO::getProductCode)
                        .selectAs(Product::getName, BomVO::getProductName)
                        // 排序
                        .orderByDesc(Bom::getCreateTime));
        return pageResult;
    }

    @Override
    public BomVO getInfo(String id) {
        return bomMapper.selectJoinOne(BomVO.class,
                new MPJLambdaWrapper<Bom>()
                        .innerJoin(Product.class, Product::getId, Bom::getProductId)
                        // 查询条件
                        .eq(Bom::getId, id)
                        // 字段别名
                        .selectAll(Bom.class)
                        .selectAs(Product::getCode, BomVO::getProductCode)
                        .selectAs(Product::getName, BomVO::getProductName));
    }

    @Override
    public void remove(String[] ids) {
        bomMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(BomEditDTO param) {
        Bom bom = BeanUtil.toBean(param, Bom.class);
        bomMapper.updateById(bom);
    }

    @Override
    public void add(BomAddDTO param) {
        boolean unique = commonCodeCheckService.isCodeUnique(Bom.class, Bom::getCode, param.getCode());
        if (!unique) {
            throw new BusinessException("BOM【" + param.getCode() + "】已存在!");
        }
        Bom bom = BeanUtil.toBean(param, Bom.class);
        bomMapper.insert(bom);
    }

    @Override
    public void importExcel(MultipartFile file) {
        try {
            // 调用EasyExcel读取文件
            EasyExcel.read(file.getInputStream(), BomExcelBO.class, bomExcelListener)
                    .sheet() // 读取第一个sheet
                    .doRead(); // 执行读取操作
        } catch (IOException e) {
            log.error("Excel数据导入失败", e);
            throw new BusinessException("Excel数据导入失败：" + e.getMessage());
        }
    }
}
