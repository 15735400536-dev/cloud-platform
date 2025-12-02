package com.maxinhai.platform.service.alarm;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.alarm.AlarmImageQueryDTO;
import com.maxinhai.platform.vo.alarm.AlarmImageVO;
import com.maxinhai.platform.po.alarm.AlarmImage;

public interface AlarmImageService extends IService<AlarmImage> {

    Page<AlarmImageVO> searchByPage(AlarmImageQueryDTO param);

    AlarmImageVO getInfo(String id);

    void remove(String[] ids);

}
