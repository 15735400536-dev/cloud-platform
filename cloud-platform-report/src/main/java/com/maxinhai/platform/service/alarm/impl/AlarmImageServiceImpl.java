package com.maxinhai.platform.service.alarm.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.alarm.AlarmImageQueryDTO;
import com.maxinhai.platform.mapper.alarm.AlarmImageMapper;
import com.maxinhai.platform.po.alarm.AlarmImage;
import com.maxinhai.platform.service.alarm.AlarmImageService;
import com.maxinhai.platform.vo.alarm.AlarmImageVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class AlarmImageServiceImpl extends ServiceImpl<AlarmImageMapper, AlarmImage> implements AlarmImageService {

    private final AlarmImageMapper alarmImageMapper;

    @Override
    public Page<AlarmImageVO> searchByPage(AlarmImageQueryDTO param) {
        return alarmImageMapper.selectJoinPage(param.getPage(), AlarmImageVO.class, new MPJLambdaWrapper<AlarmImage>()
                .eq(StrUtil.isNotBlank(param.getAlarmId()), AlarmImage::getAlarmId, param.getAlarmId())
                .like(StrUtil.isNotBlank(param.getImageName()), AlarmImage::getImageName, param.getImageName())
                .orderByDesc(AlarmImage::getCreateTime));
    }

    @Override
    public AlarmImageVO getInfo(String id) {
        return alarmImageMapper.selectJoinOne(AlarmImageVO.class, new MPJLambdaWrapper<AlarmImage>()
                .eq(AlarmImage::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        alarmImageMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }
}
