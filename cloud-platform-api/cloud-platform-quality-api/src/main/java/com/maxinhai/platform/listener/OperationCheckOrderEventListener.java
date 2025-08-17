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
public class OperationCheckOrderEventListener {

    @EventListener
    public void handleCheckOrderEvent(OperationCheckOrderEvent operationCheckOrderEvent) {
        AjaxResult ajaxResult = new RestTemplate()
                .getForObject("http://localhost:10060/checkOrder/generateTaskCheckOrder/" + operationCheckOrderEvent.getTaskOrderId(), AjaxResult.class);
        log.info("generateTaskCheckOrder ajaxResult: {}", ajaxResult.toString());
    }

}
