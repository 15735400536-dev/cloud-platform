package com.maxinhai.platform;

import com.maxinhai.platform.mapper.alarm.AlarmImageMapper;
import com.maxinhai.platform.po.alarm.AlarmImage;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class BatchInsertTests {

    @Resource
    private AlarmImageMapper alarmImageMapper;

    @Test
    public void testFindUserById() {
        List<AlarmImage> alarmImageList = new ArrayList<>(100);
        for (int i = 1; i <= 100; i++) {
            AlarmImage alarmImage = new AlarmImage();
            alarmImage.setAlarmId("alarmId" + i);
            alarmImage.setImageName("imageName" + i);
            alarmImage.setImageUrl("imageUrl" + i);
            alarmImageList.add(alarmImage);
        }
        alarmImageMapper.batchInsert(alarmImageList);
    }

}
