package com.maxinhai.platform.service.alarm;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.alarm.AlarmImageQueryDTO;
import com.maxinhai.platform.vo.alarm.AlarmImageVO;
import com.maxinhai.platform.po.alarm.AlarmImage;

import java.util.List;

public interface AlarmImageService extends IService<AlarmImage> {

    Page<AlarmImageVO> searchByPage(AlarmImageQueryDTO param);

    AlarmImageVO getInfo(String id);

    void remove(String[] ids);

    /**
     * 异步处理图片：Base64转文件 + 批量插入数据库
     *
     * @param alarmId 告警记录ID
     * @param imgs    Base64图片列表
     */
    void asyncHandleAlarmImages(String alarmId, List<String> imgs);

}
