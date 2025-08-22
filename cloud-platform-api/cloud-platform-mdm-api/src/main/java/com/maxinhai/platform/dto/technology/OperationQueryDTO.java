package com.maxinhai.platform.dto.technology;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.technology.OperationVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "数据字典新增DTO")
public class OperationQueryDTO extends PageSearch<OperationVO> {

    /**
     * 工序编码
     */
    @ApiModelProperty(value = "工序编码")
    private String code;
    /**
     * 工序名称
     */
    @ApiModelProperty(value = "工序名称")
    private String name;

}
