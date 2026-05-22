package com.maxinhai.platform.controller;

import com.maxinhai.platform.service.EmqxRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/emqx/rule")
@RequiredArgsConstructor
public class EmqxRuleController {

    private final EmqxRuleService emqxRuleService;

    // 测试EMQX连通性
    @GetMapping("/test/connect")
    public String testConnect() {
        boolean isConnect = emqxRuleService.testEmqxConnect();
        return isConnect ? "EMQX API连接成功" : "EMQX API连接失败";
    }

    // 动态创建规则 - 示例
    @GetMapping("/create")
    public String createRule() {
        // 生成唯一规则ID（业务侧可自定义，比如：biz_sensor_+UUID）
        String ruleId = "springboot_emqx_rule_" + UUID.randomUUID().toString().replace("-", "");
        String ruleName = "动态规则-传感器数据转发";
        // 规则SQL：匹配sensor/#主题的所有消息
        String sql = "SELECT * FROM \"sensor/#\"";
        boolean isSuccess = emqxRuleService.createEmqxRule(ruleId, ruleName, sql);
        return isSuccess ? "规则创建成功，规则ID：" + ruleId : "规则创建失败";
    }

    // 动态删除规则
    @GetMapping("/delete/{ruleId}")
    public String deleteRule(@PathVariable String ruleId) {
        boolean isSuccess = emqxRuleService.deleteEmqxRule(ruleId);
        return isSuccess ? "规则删除成功" : "规则删除失败";
    }

    // 查询所有规则
    @GetMapping("/query/all")
    public String queryAllRules() {
        return emqxRuleService.queryAllEmqxRules();
    }

}
