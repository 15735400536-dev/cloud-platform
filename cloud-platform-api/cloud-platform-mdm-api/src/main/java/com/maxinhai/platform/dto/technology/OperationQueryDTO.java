package com.maxinhai.platform.dto.technology;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.technology.OperationVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "标准工序分页查询DTO")
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
