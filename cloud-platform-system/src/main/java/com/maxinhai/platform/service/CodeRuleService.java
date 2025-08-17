package com.maxinhai.platform.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.CodeRuleAddDTO;
import com.maxinhai.platform.dto.CodeRuleEditDTO;
import com.maxinhai.platform.dto.CodeRuleQueryDTO;
import com.maxinhai.platform.po.CodeRule;
import com.maxinhai.platform.vo.CodeRuleVO;

import java.util.List;

public interface CodeRuleService extends IService<CodeRule> {

    Page<CodeRuleVO> searchByPage(CodeRuleQueryDTO param);

    CodeRuleVO getInfo(String id);

    void remove(String[] ids);

    void edit(CodeRuleEditDTO param);

    void add(CodeRuleAddDTO param);

    /**
     * 批量生成编码
     * @param ruleCode
     * @param batchSize
     * @return
     */
    List<String> generateCode(String ruleCode, int batchSize);

}
