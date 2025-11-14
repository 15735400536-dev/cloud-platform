package com.maxinhai.platform.listener;

import com.maxinhai.platform.utils.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 派工单事件监听器
 */
@Slf4j
@Component
public class TaskOrderEventListener {

    @Value("${spring.profiles.active}")
    private String env;

    // 使用@EventListener注解标记监听方法
    @Async
    @EventListener
    public void handleOrderEvent(TaskOrderStartEvent taskOrderEvent) {
        // 创建请求头对象
        HttpHeaders headers = new HttpHeaders();
        // 设置请求头（根据需要添加，例如Token、User-Agent等）
        headers.add("sa-token", "internal");
        // 封装请求头和请求参数（GET请求无请求体，可传null）
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        AjaxResult ajaxResult = new RestTemplate().exchange(
                        "http://" + judgeEnv(env, "produce") + ":10040/taskOrder/startWork/{taskOrderId}",
                        HttpMethod.GET,
                        requestEntity,
                        AjaxResult.class,
                        taskOrderEvent.getTaskOrderId())
                .getBody();

//        AjaxResult ajaxResult = new RestTemplate()
//                .getForObject("http://localhost:10040/taskOrder/startWork/" + taskOrderEvent.getTaskOrderId(), AjaxResult.class);
        log.info("startWork ajaxResult: {}", ajaxResult.toString());
    }

    @Async
    @EventListener
    public void handleOrderEvent(TaskOrderReportEvent taskOrderEvent) {
        // 创建请求头对象
        HttpHeaders headers = new HttpHeaders();
        // 设置请求头（根据需要添加，例如Token、User-Agent等）
        headers.add("sa-token", "internal");
        // 封装请求头和请求参数（GET请求无请求体，可传null）
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        AjaxResult ajaxResult = new RestTemplate().exchange(
                        "http://" + judgeEnv(env, "produce") + ":10040/taskOrder/reportWork/{taskOrderId}",
                        HttpMethod.GET,
                        requestEntity,
                        AjaxResult.class,
                        taskOrderEvent.getTaskOrderId())
                .getBody();

//        AjaxResult ajaxResult = new RestTemplate()
//                .getForObject("http://localhost:10040/taskOrder/reportWork/" + taskOrderEvent.getTaskOrderId(), AjaxResult.class);
        log.info("reportWork ajaxResult: {}", ajaxResult.toString());
    }

    @Async
    @EventListener
    public void handleOrderEvent(TaskOrderResumeEvent taskOrderEvent) {
        // 创建请求头对象
        HttpHeaders headers = new HttpHeaders();
        // 设置请求头（根据需要添加，例如Token、User-Agent等）
        headers.add("sa-token", "internal");
        // 封装请求头和请求参数（GET请求无请求体，可传null）
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        AjaxResult ajaxResult = new RestTemplate().exchange(
                        "http://" + judgeEnv(env, "produce") + ":10040/taskOrder/resumeWork/{taskOrderId}",
                        HttpMethod.GET,
                        requestEntity,
                        AjaxResult.class,
                        taskOrderEvent.getTaskOrderId())
                .getBody();

//        AjaxResult ajaxResult = new RestTemplate()
//                .getForObject("http://localhost:10040/taskOrder/resumeWork/" + taskOrderEvent.getTaskOrderId(), AjaxResult.class);
        log.info("resumeWork ajaxResult: {}", ajaxResult.toString());
    }

    /**
     * 根据环境判断使用localhost还是容器名
     *
     * @param env         环境
     * @param serviceName 服务名称
     * @return
     */
    public String judgeEnv(String env, String serviceName) {
        String container = null;
        switch (env) {
            case "dev":
                container = "localhost";
                break;
            case "prod":
                container = "cloud-platform-" + serviceName;
                break;
            default:
                throw new RuntimeException("Invalid env: " + env);
        }
        return container;
    }

}
