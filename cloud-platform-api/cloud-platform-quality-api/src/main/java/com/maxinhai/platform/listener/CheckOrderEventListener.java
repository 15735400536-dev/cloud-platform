package com.maxinhai.platform.listener;

import com.maxinhai.platform.utils.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 检测单事件监听器
 */
@Slf4j
@Component
public class CheckOrderEventListener {

    @EventListener
    public void handleCheckOrderEvent(CheckOrderEvent checkOrderEvent) {
        AjaxResult ajaxResult = new RestTemplate()
                .getForObject("http://localhost:10060/checkOrder/generate/" + checkOrderEvent.getWorkOrderId(), AjaxResult.class);
        log.info("generateWorkCheckOrder ajaxResult: {}", ajaxResult.toString());
    }

}
