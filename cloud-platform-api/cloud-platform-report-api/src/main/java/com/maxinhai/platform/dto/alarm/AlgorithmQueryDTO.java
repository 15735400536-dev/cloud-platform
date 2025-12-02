package com.maxinhai.platform.dto.alarm;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.alarm.AlgorithmVO;
import lombok.Data;

@Data
public class AlgorithmQueryDTO extends PageSearch<AlgorithmVO> {

    private String key;
    private String name;
    private Boolean enable;

}
