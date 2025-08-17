package com.maxinhai.platform.listener;

import com.maxinhai.platform.utils.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 派工单事件监听器
 */
@Slf4j
@Component
public class TaskOrderEventListener {

    // 使用@EventListener注解标记监听方法
    @EventListener
    public void handleOrderEvent(TaskOrderStartEvent taskOrderEvent) {
        AjaxResult ajaxResult = new RestTemplate()
                .getForObject("http://localhost:10040/taskOrder/startWork/" + taskOrderEvent.getTaskOrderId(), AjaxResult.class);
        log.info("startWork ajaxResult: {}", ajaxResult.toString());
    }

    @EventListener
    public void handleOrderEvent(TaskOrderReportEvent taskOrderEvent) {
        AjaxResult ajaxResult = new RestTemplate()
                .getForObject("http://localhost:10040/taskOrder/reportWork/" + taskOrderEvent.getTaskOrderId(), AjaxResult.class);
        log.info("reportWork ajaxResult: {}", ajaxResult.toString());
    }

    @EventListener
    public void handleOrderEvent(TaskOrderResumeEvent taskOrderEvent) {
        AjaxResult ajaxResult = new RestTemplate()
                .getForObject("http://localhost:10040/taskOrder/resumeWork/" + taskOrderEvent.getTaskOrderId(), AjaxResult.class);
        log.info("resumeWork ajaxResult: {}", ajaxResult.toString());
    }

}
