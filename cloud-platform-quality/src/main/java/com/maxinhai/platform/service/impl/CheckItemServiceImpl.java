package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.CheckItemAddDTO;
import com.maxinhai.platform.dto.CheckItemEditDTO;
import com.maxinhai.platform.dto.CheckItemQueryDTO;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.mapper.CheckItemMapper;
import com.maxinhai.platform.po.CheckItem;
import com.maxinhai.platform.service.CheckItemService;
import com.maxinhai.platform.service.CommonCodeCheckService;
import com.maxinhai.platform.vo.CheckItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CheckItemServiceImpl extends ServiceImpl<CheckItemMapper, CheckItem> implements CheckItemService {

    @Resource
    private CheckItemMapper checkItemMapper;
    @Resource
    private CommonCodeCheckService commonCodeCheckService;

    @Override
    public Page<CheckItemVO> searchByPage(CheckItemQueryDTO param) {
        return checkItemMapper.selectJoinPage(param.getPage(), CheckItemVO.class,
                new MPJLambdaWrapper<CheckItem>()
                        // 查询条件
                        .like(StrUtil.isNotBlank(param.getItemCode()), CheckItem::getItemCode, param.getItemCode())
                        .like(StrUtil.isNotBlank(param.getItemName()), CheckItem::getItemName, param.getItemName())
                        .like(Objects.nonNull(param.getControlType()), CheckItem::getControlType, param.getControlType())
                        // 排序
                        .orderByDesc(CheckItem::getCreateTime));
    }

    @Override
    public CheckItemVO getInfo(String id) {
        return checkItemMapper.selectJoinOne(CheckItemVO.class,
                new MPJLambdaWrapper<CheckItem>()
                        // 查询条件
                        .eq(StrUtil.isNotBlank(id), CheckItem::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        checkItemMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(CheckItemEditDTO param) {
        CheckItem user = BeanUtil.toBean(param, CheckItem.class);
        checkItemMapper.updateById(user);
    }

    @Override
    public void add(CheckItemAddDTO param) {
        boolean unique = commonCodeCheckService.isCodeUnique(CheckItem.class, CheckItem::getItemCode, param.getItemCode());
        if (!unique) {
            throw new BusinessException("检测项【" + param.getItemCode() + "】已存在!");
        }
        CheckItem user = BeanUtil.toBean(param, CheckItem.class);
        checkItemMapper.insert(user);
    }
}
