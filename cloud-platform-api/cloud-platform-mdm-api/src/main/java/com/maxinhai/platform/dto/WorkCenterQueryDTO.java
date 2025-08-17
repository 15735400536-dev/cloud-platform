package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.WorkCenterVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("数据字典新增DTO")
public class WorkCenterQueryDTO extends PageSearch<WorkCenterVO> {

    /**
     * 加工中心编码
     */
    private String code;
    /**
     * 加工中心名称
     */
    private String name;

}
