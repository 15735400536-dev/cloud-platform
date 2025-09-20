package com.maxinhai.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxinhai.platform.handler.ListHandler;
import com.maxinhai.platform.handler.StringHandler;
import com.maxinhai.platform.mapper.AlarmImageMapper;
import com.maxinhai.platform.mapper.AlarmInfoMapper;
import com.maxinhai.platform.po.AlarmImage;
import com.maxinhai.platform.po.AlarmInfo;
import com.maxinhai.platform.service.AlarmInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Service
public class AlarmInfoServiceImpl extends ServiceImpl<AlarmInfoMapper, AlarmInfo> implements AlarmInfoService {

    @Resource
    private AlarmInfoMapper alarmInfoMapper;
    @Resource
    private AlarmImageMapper alarmImageMapper;
    @Resource
    private StringHandler stringHandler;
    @Resource
    private ListHandler listHandler;

    @Override
    public void initiateAlarm(String key) {
        if (Boolean.TRUE.equals(stringHandler.hasKey(String.format("alarm:%s", key)))) {
            return;
        }
        AlarmInfo alarmInfo = new AlarmInfo();
        alarmInfo.setAlarmLevel(2);
        alarmInfo.setAlarmContent("[" + key + "]发起告警");
        alarmInfo.setAlarmSource("系统告警");
        alarmInfo.setAlarmStatus(0);
        alarmInfo.setAlarmTime(new Date());
        stringHandler.set(String.format("alarm:%s", key), alarmInfo);
    }

    @Override
    public void cancelAlarm(String key) {
        if (Boolean.FALSE.equals(stringHandler.hasKey(String.format("alarm:%s", key)))) {
            return;
        }
        AlarmInfo alarmInfo = (AlarmInfo) stringHandler.get(String.format("alarm:%s", key));
        alarmInfo.setAlarmStatus(2);
        alarmInfo.setProcessTime(new Date());
        alarmInfoMapper.insert(alarmInfo);
    }

    @Override
    public void initiateAlarm(String key, MultipartFile file) {
        if (Boolean.TRUE.equals(stringHandler.hasKey(String.format("alarm:%s", key)))) {
            // 保存图片到缓存
            listHandler.rightPush(String.format("alarm:%s:image", key), file.getOriginalFilename());
            return;
        }
        // 保存告警信息到缓存
        AlarmInfo alarmInfo = new AlarmInfo();
        alarmInfo.setAlarmLevel(2);
        alarmInfo.setAlarmContent("[" + key + "]发起告警");
        alarmInfo.setAlarmSource("系统告警");
        alarmInfo.setAlarmStatus(0);
        alarmInfo.setAlarmTime(new Date());
        stringHandler.set(String.format("alarm:%s", key), alarmInfo);
        // 保存图片到缓存
        listHandler.rightPush(String.format("alarm:%s:image", key), file.getOriginalFilename());
    }

    @Override
    public void cancelAlarmEx(String key) {
        if (Boolean.FALSE.equals(stringHandler.hasKey(String.format("alarm:%s", key)))) {
            return;
        }
        // 保存告警信息到数据库
        AlarmInfo alarmInfo = (AlarmInfo) stringHandler.get(String.format("alarm:%s", key));
        alarmInfo.setAlarmStatus(2);
        alarmInfo.setProcessTime(new Date());
        alarmInfoMapper.insert(alarmInfo);

        // 保存图片到数据库
        Long size = listHandler.size(String.format("alarm:%s:image", key));
        for (Long count = 0L; count < size; count++) {
            Object object = listHandler.leftPop(String.format("alarm:%s:image", key));
            AlarmImage alarmImage = new AlarmImage();
            alarmImage.setAlarmId(alarmInfo.getId());
            alarmImage.setImageUrl(object.toString());
            alarmImageMapper.insert(alarmImage);
        }
    }
}
