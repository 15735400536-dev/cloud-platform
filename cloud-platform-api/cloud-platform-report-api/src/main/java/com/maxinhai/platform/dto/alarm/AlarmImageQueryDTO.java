package com.maxinhai.platform.dto.alarm;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.alarm.AlarmImageVO;
import lombok.Data;

@Data
public class AlarmImageQueryDTO extends PageSearch<AlarmImageVO> {

    private String alarmId;
    private String imageName;

}
