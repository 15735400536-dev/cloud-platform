package com.maxinhai.platform.mapper.alarm;

import com.github.yulichang.base.MPJBaseMapper;
import com.maxinhai.platform.po.alarm.AlarmImage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlarmImageMapper extends MPJBaseMapper<AlarmImage> {

    void batchInsert(@Param("list") List<AlarmImage> alarmImageList);

}
