package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.AlarmInitiateDTO;
import com.maxinhai.platform.po.AlarmInfo;
import org.springframework.web.multipart.MultipartFile;

public interface AlarmInfoService extends IService<AlarmInfo> {

    /**
     * 发起告警
     * @param key
     */
    void initiateAlarm(String key);

    /**
     * 取消告警
     * @param key
     */
    void cancelAlarm(String key);

    /**
     * 发起告警
     * @param key
     * @param file
     */
    void initiateAlarm(String key, MultipartFile file);

    /**
     * 取消告警
     * @param key
     */
    void cancelAlarmEx(String key);

    void initiate(AlarmInitiateDTO dto);

}
