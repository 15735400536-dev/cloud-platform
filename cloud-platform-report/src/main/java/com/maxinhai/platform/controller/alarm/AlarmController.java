package com.maxinhai.platform.controller.alarm;

import com.google.common.collect.Lists;
import com.maxinhai.platform.dto.alarm.AlarmQueryDTO;
import com.maxinhai.platform.dto.alarm.RealTimeAlarmDTO;
import com.maxinhai.platform.service.alarm.AlarmService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.ImageBase64Utils;
import com.maxinhai.platform.utils.PageResult;
import com.maxinhai.platform.vo.alarm.AlarmVO;
import com.maxinhai.platform.vo.alarm.CountAlarmInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @ClassName：AlarmController
 * @Author: XinHai.Ma
 * @Date: 2025/12/2 21:00
 * @Description: 告警记录管理接口
 */
@RestController
@RequestMapping("/alarm")
@Api(tags = "告警记录管理接口")
public class AlarmController {

    @Resource
    private AlarmService alarmService;
    @Resource
    private RestTemplate restTemplate;

    @PostMapping("/searchByPage")
    @ApiOperation(value = "分页查询告警记录", notes = "分页查询告警记录")
    public AjaxResult<PageResult<AlarmVO>> searchByPage(@RequestBody AlarmQueryDTO param) {
        return AjaxResult.success(PageResult.convert(alarmService.searchByPage(param)));
    }

    @GetMapping("/getInfo/{id}")
    @ApiOperation(value = "获取告警记录", notes = "根据告警记录ID获取详细信息")
    public AjaxResult<AlarmVO> getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(alarmService.getInfo(id));
    }

    @PostMapping("/removeAlarm")
    @ApiOperation(value = "删除告警记录", notes = "根据告警记录ID数组删除告警记录")
    public AjaxResult<Void> removeAlarm(@RequestBody String[] ids) {
        alarmService.remove(ids);
        return AjaxResult.success();
    }

    @PostMapping("/realTimeAlarm")
    @ApiOperation(value = "实时告警", notes = "实时告警")
    public AjaxResult<Void> realTimeAlarm(@RequestBody RealTimeAlarmDTO param) throws IOException {
        alarmService.realTimeAlarm(param);
        return AjaxResult.success();
    }

    @PostMapping("/countAlarmInfo")
    @ApiOperation(value = "统计告警情况", notes = "统计告警情况")
    public AjaxResult<CountAlarmInfoVO> countAlarmInfo() {
        return AjaxResult.success(alarmService.countAlarmInfo());
    }

    @PostMapping("/mergeSearchByPage")
    @ApiOperation(value = "改造后的分页查询", notes = "合并Redis实时告警 + 数据库历史告警")
    public AjaxResult<PageResult<AlarmVO>> mergeSearchByPage(@RequestBody AlarmQueryDTO param) {
        return AjaxResult.success(PageResult.convert(alarmService.mergeSearchByPage(param)));
    }

    private static Boolean alarmStatus = Boolean.TRUE;

    @PostMapping("/callApi")
    @ApiOperation(value = "调用实时告警接口", notes = "测试实时告警接口")
    public AjaxResult<List<AjaxResult<Void>>> callApi() throws ExecutionException, InterruptedException, IOException {
        String TARGET_URL = "http://localhost:10080/alarm/realTimeAlarm";
        String TEST_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjbG91ZC1wbGF0Zm9ybSIsImV4cCI6MTc2NDc2MzUyNSwidXNlcklkIjoiMTk3MDgzMTY4MjI5NDcwNjE3NyIsImlhdCI6MTc2NDY3NzEyNSwiYWNjb3VudCI6Indhbmd4aWZlbmciLCJ1c2VybmFtZSI6IueOi-eGmeWHpCJ9.j5ZKDZRpX_W4N-lmFTjspU82iArJWtHSJ_1-g3jz9hh08DdV94osLGcoYHZ7tnlTBmzZvqZko5bYJS7ZjAxbaA";
        List<String> imgs = Lists.newArrayList(ImageBase64Utils.imageToBase64("C:\\Users\\MaXinHai\\Pictures\\v2-d2e6cbd40426e70d82fb2b090f92923d_1440w.jpg"),
                ImageBase64Utils.imageToBase64("C:\\Users\\MaXinHai\\Pictures\\v2-77bafa0f24ae7643638c605dbacf7a62_1440w.jpg"),
                ImageBase64Utils.imageToBase64("C:\\Users\\MaXinHai\\Pictures\\v2-66de663a460fdbd022169017a7ee0756_1440w.jpg"));

        // 1. 构建多个请求参数（模拟多用户/多场景请求）
        Map<String, Object> param1 = new HashMap<>();
        param1.put("algorithmKey", "person");
        param1.put("alarmStatus", alarmStatus);
        param1.put("alarmTime", new Date());
        param1.put("imgs", imgs);

        Map<String, Object> param2 = new HashMap<>();
        param2.put("algorithmKey", "hat");
        param2.put("alarmStatus", alarmStatus);
        param2.put("alarmTime", new Date());
        param2.put("imgs", imgs);

        Map<String, Object> param3 = new HashMap<>();
        param3.put("algorithmKey", "post");
        param3.put("alarmStatus", alarmStatus);
        param3.put("alarmTime", new Date());
        param3.put("imgs", imgs);

        Map<String, Object> param4 = new HashMap<>();
        param4.put("algorithmKey", "fire");
        param4.put("alarmStatus", alarmStatus);
        param4.put("alarmTime", new Date());
        param4.put("imgs", imgs);

        // 2. 异步调用（3个并发请求）
        CompletableFuture<AjaxResult> future1 = this.asyncPostJson(TARGET_URL, TEST_TOKEN, param1, AjaxResult.class);
        CompletableFuture<AjaxResult> future2 = this.asyncPostJson(TARGET_URL, TEST_TOKEN, param2, AjaxResult.class);
        CompletableFuture<AjaxResult> future3 = this.asyncPostJson(TARGET_URL, TEST_TOKEN, param3, AjaxResult.class);
        CompletableFuture<AjaxResult> future4 = this.asyncPostJson(TARGET_URL, TEST_TOKEN, param4, AjaxResult.class);

        // 3. 等待所有请求完成（阻塞当前线程，直到所有异步任务结束）
        CompletableFuture.allOf(future1, future2, future3).join();

        // 4. 获取每个请求的结果
        AjaxResult<Void> result1 = future1.get();
        AjaxResult<Void> result2 = future2.get();
        AjaxResult<Void> result3 = future3.get();
        AjaxResult<Void> result4 = future4.get();

        alarmStatus = !alarmStatus;

        return AjaxResult.success("调用成功", Lists.newArrayList(result1, result2, result3, result4));
    }

    // ------------------------------ 通用方法：构建带token的请求头 ------------------------------

    /**
     * 构建请求头（Content-Type + Authorization token）
     *
     * @param token       认证token（如Bearer token）
     * @param contentType 请求体格式（APPLICATION_JSON 或 APPLICATION_FORM_URLENCODED）
     * @return HttpHeaders
     */
    private HttpHeaders buildHeaders(String token, MediaType contentType) {
        HttpHeaders headers = new HttpHeaders();
        // 添加token认证（默认Bearer格式，若接口要求其他格式可修改，如直接传token）
        // headers.setBearerAuth(token);
        headers.set("Authorization", token);
        // 设置请求体格式
        headers.setContentType(contentType);
        return headers;
    }

    // ------------------------------ 功能1：多线程调用POST接口（JSON格式参数） ------------------------------

    /**
     * 异步调用POST接口（JSON参数）
     *
     * @param url          接口地址（如：http://localhost:8080/api/test）
     * @param token        认证token
     * @param param        请求参数（Java对象或Map，会自动转为JSON）
     * @param responseType 返回结果类型（如：String.class、User.class）
     * @return CompletableFuture<T> 异步结果对象
     */
    @Async("asyncTaskExecutor") // 指定使用自定义线程池
    public <T> CompletableFuture<T> asyncPostJson(String url, String token, Object param, Class<T> responseType) {
        try {
            // 1. 构建请求头（JSON格式）
            HttpHeaders headers = buildHeaders(token, MediaType.APPLICATION_JSON);
            // 2. 构建请求实体（头 + 参数）
            HttpEntity<Object> requestEntity = new HttpEntity<>(param, headers);
            // 3. 调用POST接口（RestTemplate自动序列化JSON）
            ResponseEntity<T> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    responseType
            );
            // 4. 返回异步结果（CompletableFuture.completedFuture包装结果）
            return CompletableFuture.completedFuture(response.getBody());
        } catch (Exception e) {
            // 异常处理：将异常封装到CompletableFuture中，避免线程中断
            CompletableFuture<T> future = new CompletableFuture<>();
            future.completeExceptionally(new RuntimeException(
                    String.format("JSON格式POST请求失败：url=%s, error=%s", url, e.getMessage()), e
            ));
            return future;
        }
    }

}
