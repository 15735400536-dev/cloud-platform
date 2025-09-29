package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.ApiWhitelistAddDTO;
import com.maxinhai.platform.dto.ApiWhitelistEditDTO;
import com.maxinhai.platform.dto.ApiWhitelistQueryDTO;
import com.maxinhai.platform.mapper.ApiWhitelistMapper;
import com.maxinhai.platform.po.ApiWhitelist;
import com.maxinhai.platform.service.ApiWhitelistService;
import com.maxinhai.platform.vo.ApiWhitelistVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @ClassName：ApiWhitelistServiceImpl
 * @Author: XinHai.Ma
 * @Date: 2025/9/12 14:47
 * @Description: 接口白名单业务层
 */
@Slf4j
@Service
public class ApiWhitelistServiceImpl extends ServiceImpl<ApiWhitelistMapper, ApiWhitelist>
        implements ApiWhitelistService {

    @Resource
    private ApiWhitelistMapper apiWhitelistMapper;

    @Override
    public Page<ApiWhitelistVO> searchByPage(ApiWhitelistQueryDTO param) {
        return apiWhitelistMapper.selectJoinPage(param.getPage(), ApiWhitelistVO.class,
                new MPJLambdaWrapper<ApiWhitelist>()
                        .like(StrUtil.isNotBlank(param.getServiceId()), ApiWhitelist::getServiceId, param.getServiceId())
                        .orderByDesc(ApiWhitelist::getCreateTime));
    }

    @Override
    public ApiWhitelistVO getInfo(String id) {
        return apiWhitelistMapper.selectJoinOne(ApiWhitelistVO.class, new MPJLambdaWrapper<ApiWhitelist>().eq(ApiWhitelist::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        apiWhitelistMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(ApiWhitelistEditDTO param) {
        ApiWhitelist dictType = BeanUtil.toBean(param, ApiWhitelist.class);
        apiWhitelistMapper.updateById(dictType);
    }

    @Override
    public void add(ApiWhitelistAddDTO param) {
        ApiWhitelist dictType = BeanUtil.toBean(param, ApiWhitelist.class);
        apiWhitelistMapper.insert(dictType);
    }
}
