package com.maxinhai.platform.service;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class EmqxRuleService {

    @Resource
    private RestTemplate mqttRestTemplate;

    @Value("${emqx.base-url:http://127.0.0.1:18083}")
    private String emqxBaseUrl;

    @Value("${emqx.rule-enabled:true}")
    private Boolean ruleEnabled;

    // EMQX规则API基础路径
    private static final String RULE_API = "/api/v5/rules";

    /**
     * 测试EMQX API连通性（先调用此方法，确认连通后再操作规则）
     */
    public boolean testEmqxConnect() {
        try {
            String url = emqxBaseUrl + "/api/v5/status";
            ResponseEntity<String> response = mqttRestTemplate.getForEntity(url, String.class);
            JSONObject result = JSONObject.parseObject(response.getBody());
            return result.getInteger("code") == 0;
        } catch (Exception e) {
            log.error("EMQX API连接失败", e);
            return false;
        }
    }

    /**
     * 核心方法：动态创建EMQX规则
     * @param ruleId 规则ID（自定义，唯一标识，建议用业务前缀+UUID）
     * @param ruleName 规则名称（自定义）
     * @param sql EMQX规则SQL（核心，筛选MQTT消息的条件）
     * @return 创建结果：true=成功，false=失败
     */
    public boolean createEmqxRule(String ruleId, String ruleName, String sql) {
        try {
            String url = emqxBaseUrl + RULE_API;
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            // 1. 构建规则的请求体（核心参数，完整可配）
            Map<String, Object> requestBody = new HashMap<>(8);
            requestBody.put("id", ruleId);          // 规则ID，唯一，必填
            requestBody.put("name", ruleName);      // 规则名称，必填
            requestBody.put("sql", sql);            // 规则SQL，必填，核心筛选条件
            requestBody.put("enabled", ruleEnabled); // 是否启用规则
            requestBody.put("description", "动态创建的规则-来自SpringBoot服务"); // 规则描述

            // 2. 配置规则的【动作(Action)】：消息匹配后执行的操作（可配置多个动作）
            List<Map<String, Object>> actions = new ArrayList<>();
            // ============ 示例1：动作1 - 将匹配的消息转发到另一个MQTT主题（最常用） ============
            Map<String, Object> action1 = new HashMap<>(4);
            action1.put("name", "republish"); // 动作类型：转发MQTT消息
            Map<String, Object> params1 = new HashMap<>(2);
            params1.put("topic", "forward/topic"); // 转发的目标主题
            params1.put("qos", 1); // QoS等级，0/1/2
            action1.put("params", params1);
            actions.add(action1);

            // ============ 示例2：动作2 - 将匹配的消息推送到指定HTTP接口（业务常用） ============
            Map<String, Object> action2 = new HashMap<>(4);
            action2.put("name", "http:post"); // 动作类型：发送POST请求
            Map<String, Object> params2 = new HashMap<>(3);
            params2.put("url", "http://127.0.0.1:8080/mqtt/message/receive"); // 业务接口地址
            params2.put("headers", new HashMap<String, String>(){{put("Content-Type","application/json");}});
            params2.put("body", "${payload}"); // 传递的消息体：直接传递MQTT的原始消息体
            action2.put("params", params2);
            actions.add(action2);

            // 将动作集合放入请求体
            requestBody.put("actions", actions);

            // 3. 发送请求
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = mqttRestTemplate.postForEntity(url, request, String.class);
            JSONObject result = JSONObject.parseObject(response.getBody());

            // 4. 判断结果：code=0 表示创建成功
            if (result.getInteger("code") == 0) {
                log.info("EMQX规则创建成功，规则ID：{}", ruleId);
                return true;
            } else {
                log.error("EMQX规则创建失败，规则ID：{}，错误信息：{}", ruleId, result.getString("message"));
                return false;
            }
        } catch (Exception e) {
            log.error("EMQX规则创建异常，规则ID：{}", ruleId, e);
            return false;
        }
    }

    /**
     * 动态删除EMQX规则
     * @param ruleId 要删除的规则ID
     * @return 删除结果：true=成功，false=失败
     */
    public boolean deleteEmqxRule(String ruleId) {
        try {
            String url = emqxBaseUrl + RULE_API + "/" + ruleId;
            ResponseEntity<String> response = mqttRestTemplate.exchange(url, HttpMethod.DELETE, null, String.class);
            JSONObject result = JSONObject.parseObject(response.getBody());
            if (result.getInteger("code") == 0) {
                log.info("EMQX规则删除成功，规则ID：{}", ruleId);
                return true;
            } else {
                log.error("EMQX规则删除失败，规则ID：{}，错误信息：{}", ruleId, result.getString("message"));
                return false;
            }
        } catch (Exception e) {
            log.error("EMQX规则删除异常，规则ID：{}", ruleId, e);
            return false;
        }
    }

    /**
     * 查询所有EMQX规则
     * @return 规则列表JSON字符串
     */
    public String queryAllEmqxRules() {
        try {
            String url = emqxBaseUrl + RULE_API;
            ResponseEntity<String> response = mqttRestTemplate.getForEntity(url, String.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("查询EMQX规则列表异常", e);
            return null;
        }
    }

}
