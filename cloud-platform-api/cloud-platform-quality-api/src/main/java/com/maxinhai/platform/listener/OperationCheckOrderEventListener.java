package com.maxinhai.platform.listener;

import com.maxinhai.platform.utils.AjaxResult;
import lombok.extern.slf4j.Slf4j;
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
public class OperationCheckOrderEventListener {

    @Async
    @EventListener
    public void handleCheckOrderEvent(OperationCheckOrderEvent operationCheckOrderEvent) {
        // 创建请求头对象
        HttpHeaders headers = new HttpHeaders();
        // 设置请求头（根据需要添加，例如Token、User-Agent等）
        headers.add("sa-token", "internal");
        // 封装请求头和请求参数（GET请求无请求体，可传null）
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        AjaxResult ajaxResult = new RestTemplate().exchange(
                        "http://localhost:10060/checkOrder/generateTaskCheckOrder/{taskOrderId}",
                        HttpMethod.GET,
                        requestEntity,
                        AjaxResult.class,
                        operationCheckOrderEvent.getTaskOrderId())
                .getBody();

//        AjaxResult ajaxResult = new RestTemplate()
//                .getForObject("http://localhost:10060/checkOrder/generateTaskCheckOrder/" + operationCheckOrderEvent.getTaskOrderId(), AjaxResult.class);
        log.info("generateTaskCheckOrder ajaxResult: {}", ajaxResult.toString());
    }

}
