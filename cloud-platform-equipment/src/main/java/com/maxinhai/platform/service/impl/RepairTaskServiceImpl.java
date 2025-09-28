package com.maxinhai.platform.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.RepairTaskQueryDTO;
import com.maxinhai.platform.mapper.RepairTaskMapper;
import com.maxinhai.platform.po.RepairTask;
import com.maxinhai.platform.service.RepairTaskService;
import com.maxinhai.platform.vo.RepairTaskVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName：RepairTaskServiceImpl
 * @Author: XinHai.Ma
 * @Date: 2025/8/28 13:39
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Slf4j
@Service
public class RepairTaskServiceImpl extends ServiceImpl<RepairTaskMapper, RepairTask> implements RepairTaskService {
    @Resource
    private RepairTaskMapper repairTaskMapper;

    @Override
    public Page<RepairTaskVO> searchByPage(RepairTaskQueryDTO param) {
        return repairTaskMapper.selectJoinPage(param.getPage(), RepairTaskVO.class,
                new MPJLambdaWrapper<RepairTask>()
                        .like(StrUtil.isNotBlank(param.getTaskCode()), RepairTask::getTaskCode, param.getTaskCode())
                        .orderByDesc(RepairTask::getCreateTime));
    }

    @Override
    public RepairTaskVO getInfo(String id) {
        return repairTaskMapper.selectJoinOne(RepairTaskVO.class,
                new MPJLambdaWrapper<RepairTask>()
                        .like(StrUtil.isNotBlank(id), RepairTask::getId, id));
    }
}
