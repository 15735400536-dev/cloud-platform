package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.DictTypeAddDTO;
import com.maxinhai.platform.dto.DictTypeEditDTO;
import com.maxinhai.platform.dto.DictTypeQueryDTO;
import com.maxinhai.platform.excel.DataDictExcel;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.listener.DataDictExcelListener;
import com.maxinhai.platform.mapper.DictTypeMapper;
import com.maxinhai.platform.po.DictType;
import com.maxinhai.platform.service.DictTypeService;
import com.maxinhai.platform.vo.DictTypeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DictTypeServiceImpl extends ServiceImpl<DictTypeMapper, DictType> implements DictTypeService {

    @Resource
    private DictTypeMapper dictTypeMapper;
    @Resource
    private DataDictExcelListener dataDictExcelListener;

    @Override
    public Page<DictTypeVO> searchByPage(DictTypeQueryDTO param) {
        Page<DictTypeVO> pageResult = dictTypeMapper.selectJoinPage(param.getPage(), DictTypeVO.class,
                new MPJLambdaWrapper<DictType>()
                        .like(StrUtil.isNotBlank(param.getDictType()), DictType::getDictType, param.getDictType())
                        .like(StrUtil.isNotBlank(param.getDictLabel()), DictType::getDictLabel, param.getDictLabel())
                        .eq(Objects.nonNull(param.getStatus()), DictType::getStatus, param.getStatus())
                        .orderByDesc(DictType::getCreateTime));
        return pageResult;
    }

    @Override
    public DictTypeVO getInfo(String id) {
        return dictTypeMapper.selectJoinOne(DictTypeVO.class, new MPJLambdaWrapper<DictType>().eq(DictType::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        dictTypeMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(DictTypeEditDTO param) {
        DictType dictType = BeanUtil.toBean(param, DictType.class);
        dictTypeMapper.updateById(dictType);
    }

    @Override
    public void add(DictTypeAddDTO param) {
        DictType dictType = BeanUtil.toBean(param, DictType.class);
        dictTypeMapper.insert(dictType);
    }

    @Override
    public void importExcel(MultipartFile file) {
        try {
            // 调用EasyExcel读取文件
            EasyExcel.read(file.getInputStream(), DataDictExcel.class, dataDictExcelListener)
                    .sheet() // 读取第一个sheet
                    .doRead(); // 执行读取操作
        } catch (IOException e) {
            log.error("Excel数据导入失败", e);
            throw new BusinessException("Excel数据导入失败：" + e.getMessage());
        }
    }
}
