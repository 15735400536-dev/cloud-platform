package com.maxinhai.platform.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.maxinhai.platform.service.AlarmInfoService;
import com.maxinhai.platform.utils.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/alarmInfo")
public class AlarmInfoController {

    @Resource
    private AlarmInfoService alarmInfoService;
    @Resource
    private RestTemplate restTemplate;
    @Value("${server.port}")
    private String port;

    @GetMapping("/initiateAlarm/{key}")
    public AjaxResult<Void> initiateAlarm(@PathVariable("key") String key) {
        alarmInfoService.initiateAlarm(key);
        return AjaxResult.success();
    }

    @GetMapping("/cancelAlarm/{key}")
    public AjaxResult<Void> cancelAlarm(@PathVariable("key") String key) {
        alarmInfoService.cancelAlarm(key);
        return AjaxResult.success();
    }

    @PostMapping("/initiateAlarmEx")
    public AjaxResult<Void> initiateAlarmEx(@RequestParam("key") String key, @RequestParam("file") MultipartFile file) {
        alarmInfoService.initiateAlarm(key, file);
        return AjaxResult.success();
    }

    @GetMapping("/cancelAlarmEx/{key}")
    public AjaxResult<Void> cancelAlarmEx(@PathVariable("key") String key) {
        alarmInfoService.cancelAlarmEx(key);
        return AjaxResult.success();
    }

    //@Scheduled(initialDelay = 5000, fixedDelay = 60000)
    public void callApi() throws InterruptedException {
        String key = DateUtil.format(new Date(), "yyyyMMddHHmmss");
        int count = RandomUtil.randomInt(1, 11);
        for (int i = 0; i < count; i++) {
            AjaxResult ajaxResult = restTemplate.getForObject("http://localhost:" + port + "/alarmInfo/initiateAlarm/" + key, AjaxResult.class);
            log.info("发起告警 -> 调用结果: {}", ajaxResult.toString());
            TimeUnit.SECONDS.sleep(5L);
        }

        AjaxResult ajaxResult = restTemplate.getForObject("http://localhost:" + port + "/alarmInfo/cancelAlarm/" + key, AjaxResult.class);
        log.info("取消告警 -> 调用结果: {}", ajaxResult.toString());
    }

    @Scheduled(initialDelay = 5000, fixedDelay = 60000)
    public void callPostApi() throws InterruptedException {
        String key = DateUtil.format(new Date(), "yyyyMMddHHmmss");
        int count = RandomUtil.randomInt(1, 11);
        for (int i = 0; i < count; i++) {
            callInitiateAlarmEx(key);
            TimeUnit.SECONDS.sleep(5L);
        }

        callCancelAlarmEx(key);
    }

    /**
     * 调用发起告警接口
     * @param key
     */
    public void callInitiateAlarmEx(String key) {
        try {
            // 1. 构建请求参数（multipart/form-data格式）
            MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();

            // 添加图片文件（使用FileSystemResource读取本地文件）
            String imagePath = "C:\\Users\\MaXinHai\\Pictures\\Camera Roll\\v2-0ab1a359a67746a7baa991e2499fb0fd_1440w.jpg";
            File imageFile = new File(imagePath);
            if (!imageFile.exists()) {
                throw new RuntimeException("图片文件不存在：" + imagePath);
            }
            FileSystemResource imageResource = new FileSystemResource(imageFile);
            params.add("file", imageResource); // 对应接口的@RequestParam("image")

            // 添加JSON参数（转为字符串）
            params.add("key", key); // 对应接口的@RequestParam("key")

            // 2. 设置请求头（multipart/form-data）
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // 3. 构建请求实体
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(params, headers);

            // 4. 发送POST请求
            String url = "http://localhost:" + port + "/alarmInfo/initiateAlarmEx"; // 目标接口地址
            ResponseEntity<AjaxResult> response = restTemplate.postForEntity(url, requestEntity, AjaxResult.class);

            log.info("发起告警 -> 调用结果: {}", response.getBody());
        } catch (Exception e) {
            log.error("调用接口报错: {}", e.getMessage());
        }
    }

    /**
     * 调用取消告警接口
     * @param key
     */
    public void callCancelAlarmEx(String key) {
        AjaxResult ajaxResult = restTemplate.getForObject("http://localhost:" + port + "/alarmInfo/cancelAlarmEx/" + key, AjaxResult.class);
        log.info("取消告警 -> 调用结果: {}", ajaxResult.toString());
    }

}
