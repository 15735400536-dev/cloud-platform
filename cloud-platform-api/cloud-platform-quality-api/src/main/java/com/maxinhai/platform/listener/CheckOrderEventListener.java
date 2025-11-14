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
 * 检测单事件监听器
 */
@Slf4j
@Component
public class CheckOrderEventListener {

    @Value("${spring.profiles.active}")
    private String env;

    @Async
    @EventListener
    public void handleCheckOrderEvent(CheckOrderEvent checkOrderEvent) {
        // 创建请求头对象
        HttpHeaders headers = new HttpHeaders();
        // 设置请求头（根据需要添加，例如Token、User-Agent等）
        headers.add("sa-token", "internal");
        // 封装请求头和请求参数（GET请求无请求体，可传null）
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        AjaxResult ajaxResult = new RestTemplate().exchange(
                        "http://" + judgeEnv(env, "quality") + ":10060/checkOrder/generate/{workOrderId}",
                        HttpMethod.GET,
                        requestEntity,
                        AjaxResult.class,
                        checkOrderEvent.getWorkOrderId())
                .getBody();

//        AjaxResult ajaxResult = new RestTemplate()
//                .getForObject("http://localhost:10060/checkOrder/generate/" + checkOrderEvent.getWorkOrderId(), AjaxResult.class);
        log.info("generateWorkCheckOrder ajaxResult: {}", ajaxResult.toString());
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
