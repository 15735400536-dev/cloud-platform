package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.CodeRuleVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "编码规则分页查询DTO")
public class CodeRuleQueryDTO extends PageSearch<CodeRuleVO> {

    /**
     * 规则编码
     */
    @ApiModelProperty(value = "规则编码")
    private String ruleCode;
    /**
     * 规则名称
     */
    @ApiModelProperty(value = "规则名称")
    private String ruleName;

}
