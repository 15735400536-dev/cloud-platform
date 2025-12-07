package com.maxinhai.platform.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Lists;
import com.maxinhai.platform.bo.CheckResult;
import com.maxinhai.platform.dto.RealTimeCheckDTO;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.service.RealTimeCheckService;
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
public class RealTimeCheckServiceImpl implements RealTimeCheckService {

    private final RestTemplate restTemplate;

    // 随机检测异常次数
    private static int randomCount = 0;
    // 接口调用次数
    private static int callCount = 0;
    // 重试次数
    private static int retryCount;

    @Override
    public CheckResult realTimeCheck(RealTimeCheckDTO param) {
        if (randomCount == 0) {
            randomCount = RandomUtil.randomInt(1, 3);
            callCount = 0;
        }
        callCount++;
        log.info("随机检测异常次数:{}, 接口调用次数:{}", randomCount, callCount);
        if (randomCount >= callCount) {
            throw new BusinessException("算法检测异常");
        }
        if (randomCount == callCount) {
            randomCount = 0;
        }
        if (callCount > randomCount) {
            callCount = 0;
        }
        CheckResult checkResult = new CheckResult(
                RandomUtil.randomBoolean(),
                RandomUtil.randomBoolean(),
                RandomUtil.randomBoolean(),
                RandomUtil.randomBoolean(),
                RandomUtil.randomBoolean(),
                RandomUtil.randomBoolean(),
                RandomUtil.randomBoolean(),
                RandomUtil.randomBoolean(),
                RandomUtil.randomBoolean());
        return checkResult;
    }

    @Override
    public void callRealTimeCheck() {
        RealTimeCheckDTO param = new RealTimeCheckDTO();
        param.setImages(Lists.newArrayList("1.jpg", "2.jpg", "3.jpg"));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "internal");
        HttpEntity<RealTimeCheckDTO> request = new HttpEntity<>(param, headers);
        ResponseEntity<AjaxResult> response = restTemplate.postForEntity("http://localhost:10090/algorithm/realTimeCheck", request, AjaxResult.class);
        if (response.getBody().getCode() == HttpStatus.OK.value()) {
            log.debug("调用算法检测:{}", response.getBody());
        }
    }

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
        retryCount++;
        log.info("随机检测异常次数:{}, 重试次数:{}", randomCount, retryCount);
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
        log.error("调用算法接口失败（重试3次后仍失败）：{}", e.getMessage());
    }
}
