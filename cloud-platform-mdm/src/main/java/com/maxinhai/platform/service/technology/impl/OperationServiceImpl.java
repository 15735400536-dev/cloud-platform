package com.maxinhai.platform.service.technology.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.bo.OperationExcelBO;
import com.maxinhai.platform.dto.technology.OperationAddDTO;
import com.maxinhai.platform.dto.technology.OperationEditDTO;
import com.maxinhai.platform.dto.technology.OperationQueryDTO;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.listener.OperationExcelListener;
import com.maxinhai.platform.mapper.OperationMapper;
import com.maxinhai.platform.po.technology.Operation;
import com.maxinhai.platform.service.CommonCodeCheckService;
import com.maxinhai.platform.service.technology.OperationService;
import com.maxinhai.platform.vo.technology.OperationVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OperationServiceImpl extends ServiceImpl<OperationMapper, Operation> implements OperationService {

    @Resource
    private OperationMapper operationMapper;
    @Resource
    private CommonCodeCheckService commonCodeCheckService;
    @Resource
    private OperationExcelListener operationExcelListener;

    @Override
    public Page<OperationVO> searchByPage(OperationQueryDTO param) {
        return operationMapper.selectJoinPage(param.getPage(), OperationVO.class,
                new MPJLambdaWrapper<Operation>()
                        // 查询条件
                        .like(StrUtil.isNotBlank(param.getCode()), Operation::getCode, param.getCode())
                        .like(StrUtil.isNotBlank(param.getName()), Operation::getName, param.getName())
                        // 字段别名
                        .selectAll(Operation.class)
                        // 排序
                        .orderByDesc(Operation::getCreateTime));
    }

    @Override
    public OperationVO getInfo(String id) {
        return operationMapper.selectJoinOne(OperationVO.class,
                new MPJLambdaWrapper<Operation>()
                        // 查询条件
                        .eq(Operation::getId, id)
                        // 字段别名
                        .selectAll(Operation.class));
    }

    @Override
    public void remove(String[] ids) {
        operationMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(OperationEditDTO param) {
        Operation operation = BeanUtil.toBean(param, Operation.class);
        operationMapper.updateById(operation);
    }

    @Override
    public void add(OperationAddDTO param) {
        boolean unique = commonCodeCheckService.isCodeUnique(Operation.class, Operation::getCode, param.getCode());
        if (!unique) {
            throw new BusinessException("工序【" + param.getCode() + "】已存在!");
        }
        Operation operation = BeanUtil.toBean(param, Operation.class);
        operationMapper.insert(operation);
    }

    @Override
    public void importExcel(MultipartFile file) {
        try {
            // 调用EasyExcel读取文件
            EasyExcel.read(file.getInputStream(), OperationExcelBO.class, operationExcelListener)
                    .sheet() // 读取第一个sheet
                    .doRead(); // 执行读取操作
        } catch (IOException e) {
            log.error("Excel数据导入失败", e);
            throw new BusinessException("Excel数据导入失败：" + e.getMessage());
        }
    }
}
