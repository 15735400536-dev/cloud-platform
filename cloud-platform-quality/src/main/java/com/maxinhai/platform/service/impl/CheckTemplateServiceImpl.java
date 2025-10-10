package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.bo.CheckTemplateExcel;
import com.maxinhai.platform.dto.CheckTemplateAddDTO;
import com.maxinhai.platform.dto.CheckTemplateEditDTO;
import com.maxinhai.platform.dto.CheckTemplateQueryDTO;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.listener.CheckTemplateExcelListener;
import com.maxinhai.platform.mapper.CheckItemMapper;
import com.maxinhai.platform.mapper.CheckTemplateMapper;
import com.maxinhai.platform.mapper.OperationMapper;
import com.maxinhai.platform.mapper.ProductMapper;
import com.maxinhai.platform.po.CheckItem;
import com.maxinhai.platform.po.CheckTemplate;
import com.maxinhai.platform.po.CheckTemplateItemRel;
import com.maxinhai.platform.po.Product;
import com.maxinhai.platform.po.technology.Operation;
import com.maxinhai.platform.service.CheckTemplateItemRelService;
import com.maxinhai.platform.service.CheckTemplateService;
import com.maxinhai.platform.service.CommonCodeCheckService;
import com.maxinhai.platform.vo.CheckTemplateVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CheckTemplateServiceImpl extends ServiceImpl<CheckTemplateMapper, CheckTemplate> implements CheckTemplateService {

    @Resource
    private CheckTemplateMapper checkTemplateMapper;
    @Resource
    private CommonCodeCheckService commonCodeCheckService;
    @Resource
    private CheckTemplateExcelListener templateExcelListener;
    @Resource
    private CheckTemplateMapper templateMapper;
    @Resource
    private CheckItemMapper itemMapper;
    @Resource
    private CheckTemplateItemRelService templateItemRelService;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private OperationMapper operationMapper;

    @Override
    public Page<CheckTemplateVO> searchByPage(CheckTemplateQueryDTO param) {
        return checkTemplateMapper.selectJoinPage(param.getPage(), CheckTemplateVO.class,
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
        boolean unique = commonCodeCheckService.isCodeUnique(CheckTemplate.class, CheckTemplate::getTemplateCode, param.getTemplateCode());
        if (!unique) {
            throw new BusinessException("检测模板【" + param.getTemplateCode() + "】已存在!");
        }
        CheckTemplate template = BeanUtil.toBean(param, CheckTemplate.class);
        checkTemplateMapper.insert(template);
    }

    @Override
    public void importExcel(MultipartFile file) {
        try {
            // 调用EasyExcel读取文件
            EasyExcel.read(file.getInputStream(), CheckTemplateExcel.class, templateExcelListener)
                    .sheet() // 读取第一个sheet
                    .doRead(); // 执行读取操作
        } catch (IOException e) {
            log.error("Excel数据导入失败", e);
            throw new BusinessException("Excel数据导入失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveExcelData(List<CheckTemplateExcel> dataList) {
        Set<String> templateCodeSet = dataList.stream().map(CheckTemplateExcel::getTemplateCode).collect(Collectors.toSet());
        Set<String> itemCodeSet = dataList.stream().map(CheckTemplateExcel::getItemCode).collect(Collectors.toSet());
        List<CheckTemplate> templateList = templateMapper.selectList(new LambdaQueryWrapper<CheckTemplate>()
                .select(CheckTemplate::getId, CheckTemplate::getTemplateCode)
                .in(CheckTemplate::getTemplateCode, templateCodeSet));
        if (!templateList.isEmpty()) {
            Set<String> codeSet = templateList.stream().map(CheckTemplate::getTemplateCode).collect(Collectors.toSet());
            throw new BusinessException("检测模板【" + StringUtils.collectionToDelimitedString(codeSet, ",") + "】已存在！");
        }
        List<CheckItem> itemList = itemMapper.selectList(new LambdaQueryWrapper<CheckItem>()
                .select(CheckItem::getId, CheckItem::getItemCode)
                .in(CheckItem::getItemCode, itemCodeSet));
        if (!itemList.isEmpty()) {
            Set<String> codeSet = itemList.stream().map(CheckItem::getItemCode).collect(Collectors.toSet());
            throw new BusinessException("检测项【" + StringUtils.collectionToDelimitedString(codeSet, ",") + "】已存在！");
        }

        // 查询产品信息
        Set<String> productCodeSet = dataList.stream().map(CheckTemplateExcel::getProductCode).collect(Collectors.toSet());
        List<Product> productList = productMapper.selectList(new LambdaQueryWrapper<Product>()
                .select(Product::getId, Product::getCode, Product::getName)
                .in(Product::getCode, productCodeSet));
        Map<String, Product> productMap = productList.stream().collect(Collectors.toMap(Product::getId, Product -> Product));
        // 查询工序信息
        Set<String> operationCodeSet = dataList.stream().map(CheckTemplateExcel::getOperationCode).collect(Collectors.toSet());
        List<Operation> operationList = operationMapper.selectList(new LambdaQueryWrapper<Operation>()
                .select(Operation::getId, Operation::getCode, Operation::getName)
                .in(Operation::getCode, operationCodeSet));
        Map<String, Operation> operationMap = operationList.stream().collect(Collectors.toMap(Operation::getId, Operation -> Operation));

        List<CheckTemplateItemRel> relList = new ArrayList<>(dataList.size());
        Map<String, CheckTemplate> checkTemplateMap = new HashMap<>(templateCodeSet.size());
        for (CheckTemplateExcel excel : dataList) {
            // 检测模板
            CheckTemplate template = null;
            if (!checkTemplateMap.containsKey(excel.getTemplateCode())) {
                template = CheckTemplateExcel.buildCheckTemplate(excel);
                if (productMap.containsKey(excel.getProductCode())) {
                    throw new BusinessException("产品【" + excel.getProductCode() + "】未找到！");
                }
                Product product = productMap.get(excel.getProductCode());
                template.setProductId(product.getId());

                if (operationMap.containsKey(excel.getOperationCode())) {
                    throw new BusinessException("工序【" + excel.getOperationCode() + "】未找到！");
                }
                Operation operation = operationMap.get(excel.getOperationCode());
                template.setOperationId(operation.getId());
                templateMapper.insert(template);
                checkTemplateMap.put(excel.getTemplateCode(), template);
            } else {
                template = checkTemplateMap.get(excel.getTemplateCode());
            }

            // 检测项
            CheckItem item = CheckTemplateExcel.buildCheckItem(excel);
            itemMapper.insert(item);

            // 绑定关联
            CheckTemplateItemRel rel = new CheckTemplateItemRel(template.getId(), item.getId());
            relList.add(rel);
        }
        templateItemRelService.saveBatch(relList);
    }
}
