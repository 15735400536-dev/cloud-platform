package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.CheckTemplateAddDTO;
import com.maxinhai.platform.dto.CheckTemplateEditDTO;
import com.maxinhai.platform.dto.CheckTemplateQueryDTO;
import com.maxinhai.platform.po.Product;
import com.maxinhai.platform.mapper.CheckTemplateMapper;
import com.maxinhai.platform.po.CheckTemplate;
import com.maxinhai.platform.service.CheckTemplateService;
import com.maxinhai.platform.vo.CheckTemplateVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CheckTemplateServiceImpl extends ServiceImpl<CheckTemplateMapper, CheckTemplate> implements CheckTemplateService {

    @Resource
    private CheckTemplateMapper checkTemplateMapper;

    @Override
    public Page<CheckTemplateVO> searchByPage(CheckTemplateQueryDTO param) {
        Page<CheckTemplateVO> pageResult = checkTemplateMapper.selectJoinPage(param.getPage(), CheckTemplateVO.class,
                new MPJLambdaWrapper<CheckTemplate>()
                        .innerJoin(Product.class, Product::getId, CheckTemplate::getProductId)
                        // 查询字段
                        .like(StrUtil.isNotBlank(param.getTemplateCode()), CheckTemplate::getTemplateCode, param.getTemplateCode())
                        .like(StrUtil.isNotBlank(param.getTemplateName()), CheckTemplate::getTemplateName, param.getTemplateName())
                        .eq(StrUtil.isNotBlank(param.getCheckType()), CheckTemplate::getCheckType, param.getCheckType())
                        .like(StrUtil.isNotBlank(param.getProductCode()), Product::getCode, param.getProductCode())
                        .like(StrUtil.isNotBlank(param.getProductName()), Product::getName, param.getProductName())
                        // 字段别名
                        .selectAll(CheckTemplate.class)
                        .selectAs(Product::getCode, CheckTemplateVO::getProductCode)
                        .selectAs(Product::getName, CheckTemplateVO::getProductName)
                        // 排序
                        .orderByDesc(CheckTemplate::getCreateTime));
        return pageResult;
    }

    @Override
    public CheckTemplateVO getInfo(String id) {
        return checkTemplateMapper.selectJoinOne(CheckTemplateVO.class,
                new MPJLambdaWrapper<CheckTemplate>()
                        .innerJoin(Product.class, Product::getId, CheckTemplate::getProductId)
                        // 查询字段
                        .eq(StrUtil.isNotBlank(id), CheckTemplate::getId, id)
                        // 字段别名
                        .selectAll(CheckTemplate.class)
                        .selectAs(Product::getCode, CheckTemplateVO::getProductCode)
                        .selectAs(Product::getName, CheckTemplateVO::getProductName));
    }

    @Override
    public void remove(String[] ids) {
        checkTemplateMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(CheckTemplateEditDTO param) {
        CheckTemplate template = BeanUtil.toBean(param, CheckTemplate.class);
        checkTemplateMapper.updateById(template);
    }

    @Override
    public void add(CheckTemplateAddDTO param) {
        CheckTemplate template = BeanUtil.toBean(param, CheckTemplate.class);
        checkTemplateMapper.insert(template);
    }
}
