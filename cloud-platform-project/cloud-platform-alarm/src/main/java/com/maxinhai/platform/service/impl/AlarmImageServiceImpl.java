package com.maxinhai.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxinhai.platform.mapper.AlarmImageMapper;
import com.maxinhai.platform.po.AlarmImage;
import com.maxinhai.platform.service.AlarmImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AlarmImageServiceImpl extends ServiceImpl<AlarmImageMapper, AlarmImage> implements AlarmImageService {
}
