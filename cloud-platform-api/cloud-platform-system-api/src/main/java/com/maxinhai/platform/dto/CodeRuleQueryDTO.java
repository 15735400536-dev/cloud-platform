package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.CodeRuleVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("DTO")
public class CodeRuleQueryDTO extends PageSearch<CodeRuleVO> {

    /**
     * 规则编码
     */
    private String ruleCode;
    /**
     * 规则名称
     */
    private String ruleName;

}
