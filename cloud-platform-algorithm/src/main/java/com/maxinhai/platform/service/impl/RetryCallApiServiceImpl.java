package com.maxinhai.platform.service.impl;

import com.maxinhai.platform.dto.RealTimeCheckDTO;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.service.RetryCallApiService;
import com.maxinhai.platform.utils.AjaxResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class RetryCallApiServiceImpl implements RetryCallApiService {

    private final RestTemplate restTemplate;

    @Override
    @Retryable(
            // 触发重试的异常类型（这里自定义一个500状态码异常）
            value = {BusinessException.class},
            // 最大重试次数（包含首次调用，所以这里设为3，实际重试2次，总调用3次）
            maxAttempts = 3,
            // 重试延迟：首次1秒，后续每次延迟翻倍（指数退避）
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void retryCallApi(String apiUrl, RealTimeCheckDTO param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "internal");
        HttpEntity<RealTimeCheckDTO> request = new HttpEntity<>(param, headers);
        ResponseEntity<AjaxResult> response = restTemplate.postForEntity(apiUrl, request, AjaxResult.class);
        int resultCode = response.getBody().getCode();
        if (resultCode == HttpStatus.OK.value()) {
            log.info("调用算法检测: {}", response.getBody());
        } else if (resultCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            throw new BusinessException("调用算法检测失败");
        } else {
            throw new BusinessException("算法接口返回非预期状态码：" + resultCode);
        }
    }

    /**
     * 重试次数耗尽后的兜底方法（必须与重试方法返回值、参数一致）
     */
    @Recover
    public void recover(Exception e, String apiUrl, RealTimeCheckDTO param) {
        log.error("调用算法接口失败（重试3次后仍失败）-> 接口地址：{},\n 接口参数：{}，\n 异常信息：{}", apiUrl, param, e.getMessage());
    }
}
