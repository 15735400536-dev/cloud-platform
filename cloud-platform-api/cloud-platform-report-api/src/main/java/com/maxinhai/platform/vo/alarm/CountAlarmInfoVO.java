package com.maxinhai.platform.vo.alarm;

import com.maxinhai.platform.bo.AlgorithmAlarmBO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "统计告警情况")
public class CountAlarmInfoVO {

    @ApiModelProperty(value = "告警总数次数")
    private long alarmTotalCount;
    @ApiModelProperty(value = "算法告警次数")
    private List<AlgorithmAlarmBO> algorithmAlarmCount;

}
