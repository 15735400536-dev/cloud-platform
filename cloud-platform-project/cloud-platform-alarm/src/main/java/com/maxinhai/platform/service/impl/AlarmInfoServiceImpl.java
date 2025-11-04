package com.maxinhai.platform.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxinhai.platform.dto.AlarmInitiateDTO;
import com.maxinhai.platform.handler.HashHandler;
import com.maxinhai.platform.handler.ListHandler;
import com.maxinhai.platform.handler.StringHandler;
import com.maxinhai.platform.mapper.AlarmImageMapper;
import com.maxinhai.platform.mapper.AlarmInfoMapper;
import com.maxinhai.platform.po.AlarmImage;
import com.maxinhai.platform.po.AlarmInfo;
import com.maxinhai.platform.service.AlarmInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
    @Resource
    private HashHandler hashHandler;

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

    @Override
    public void initiate(AlarmInitiateDTO dto) {
        String key = "alarm:" + dto.getAlgorithmType();
        JSONObject jsonObject = AlarmInitiateDTO.buildJsonObject(dto);
        if (!dto.getAlarmFlag() && hashHandler.hasKey(key)) {
            // 消除告警（更新告警状态、告警结束时间）
            hashHandler.set(key, "alarmFlag", jsonObject.getBool("alarmFlag"));
            hashHandler.set(key, "endTime", jsonObject.getDate("endTime"));

            // 告警生命周期结束，放入告警队列，等待持久化
            Map<Object, Object> dataMap = hashHandler.getAll(key);
            log.info("放入list元素: {}", dataMap.size());
            listHandler.rightPush("alarm:list", JSONUtil.toJsonStr(dataMap));

            // 删除hash
            stringHandler.delete(key);
        } else {
            // 发起告警
            Set<String> keySet = jsonObject.keySet();
            keySet.forEach(itemKey -> {
                hashHandler.set(key, itemKey, jsonObject.get(itemKey));
            });
        }
    }

    private static final Map<String, String> ALGORITHM_MSG_MAP = new HashMap<>();

    static {
        ALGORITHM_MSG_MAP.put("person", "检测到人员");
        ALGORITHM_MSG_MAP.put("hat", "检测到未佩戴安全帽");
        ALGORITHM_MSG_MAP.put("fire", "检测到火灾");
    }

    @Scheduled(initialDelay = 10000, fixedDelay = 3000)
    public void handler() {
        Long size = listHandler.size("alarm:list");
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                String jsonStr = (String) listHandler.leftPop("alarm:list");
                if(StrUtil.isEmpty(jsonStr)) {
                    // TODO 有概率取到空消息
                    Long currentSize = listHandler.size("alarm:list");
                    log.info("alarm:list:{}", currentSize);
                    continue;
                }
                // 保存告警信息
                JSONObject jsonObject = JSONUtil.toBean(jsonStr, JSONObject.class);
                AlarmInfo alarmInfo = new AlarmInfo();
                alarmInfo.setRuleId(jsonObject.getStr("algorithmType"));
                alarmInfo.setAlarmLevel(1);
                alarmInfo.setAlarmContent(ALGORITHM_MSG_MAP.get(jsonObject.getStr("algorithmType")));
                alarmInfo.setAlarmSource("algorithm");
                alarmInfo.setAlarmStatus(2);
                alarmInfo.setAlarmTime(jsonObject.getDate("beginTime"));
                alarmInfo.setProcessTime(jsonObject.getDate("endTime"));
                alarmInfo.setProcessUser("system");
                alarmInfo.setProcessNotes("20251017001");
                alarmInfoMapper.insert(alarmInfo);

                // 保存告警图片
                String filePath = "C:\\Users\\MaXinHai\\Pictures\\" + AlarmInitiateDTO.getImageName(jsonObject);
                AlarmInitiateDTO.writeToFile(jsonObject, filePath);
                AlarmImage alarmImage = new AlarmImage();
                alarmImage.setAlarmId(alarmInfo.getId());
                alarmImage.setImageUrl(filePath);
                alarmImageMapper.insert(alarmImage);
                //log.debug("处理告警报文: {}", alarmInfo);
            }
        }
    }
}
