package com.maxinhai.platform.controller;

import cn.hutool.core.io.FileUtil;
import com.maxinhai.platform.handler.*;
import com.maxinhai.platform.lock.RedisLock;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.DocxUtils;
import com.maxinhai.platform.utils.HttpClientUtils;
import com.maxinhai.platform.utils.OkHttpClientUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("/nacos")
@Api(tags = "Nacos配置管理接口")
public class NacosConfigController {

    @Value("${service.name:unknown}")
    private String nacosConfig;
    @Resource
    private StringHandler stringHandler;
    @Resource
    private ListHandler listHandler;
    @Resource
    private SetHandler setHandler;
    @Resource
    private ZSetHandler zSetHandler;
    @Resource
    private HashHandler hashHandler;
    @Resource
    private RedisLock redisLock;
    @Resource
    private CommonHandler commonHandler;
    @Resource()
    @Qualifier("cpuIntensiveExecutor")
    private ThreadPoolTaskExecutor executor;
    @Resource
    private DocxUtils docxUtils;
    @Resource
    private HttpClientUtils httpClientUtils;
    @Resource
    private OkHttpClientUtils okHttpClientUtils;

    @GetMapping("/getConfig")
    @ApiOperation(value = "获取nacos配置文件配置", notes = "获取nacos配置文件配置")
    public AjaxResult<String> getConfig() {
        return AjaxResult.success(nacosConfig);
    }

    @GetMapping("/redisTest")
    @ApiOperation(value = "Redis测试", notes = "Redis测试")
    public AjaxResult<String> redisTest() {
        String flag = redisLock.lock("lock", 1, 1);
        if (Objects.nonNull(flag)) {
            stringHandler.set("name", "马鑫海");
            stringHandler.set("sex", "男");
            stringHandler.set("age", 28);

            Object name = stringHandler.get("name");
            Object sex = stringHandler.get("sex");
            Object age = stringHandler.get("age");
            System.out.println("name => " + name);
            System.out.println("sex => " + sex);
            System.out.println("age => " + age);

            hashHandler.set("person", "name", name);
            hashHandler.set("person", "sex", sex);
            hashHandler.set("person", "age", age);

            Map<Object, Object> person = hashHandler.getAll("person");
            System.out.println("person => " + person);

            List<String> array = person.keySet().stream().map(String::valueOf).collect(Collectors.toList());
            for (String str : array) {
                listHandler.leftPush("list", str);
            }
            List<Object> values = listHandler.range("list", 0, array.size());
            System.out.println("list => " + values);
            Long size = listHandler.size("list");
            System.out.println("list size => " + size);

            for (double score = 1; score <= 10; score++) {
                zSetHandler.add("zset", String.valueOf(score), score);
            }
            Set<Object> zset = zSetHandler.rangeByScore("zset", 3, 5);
            System.out.println("zset => " + zset);

            for (Object o : person.keySet()) {
                setHandler.add("set", o);
            }
            Set<Object> set = setHandler.members("set");
            System.out.println("set => " + set);

            List<String> keys = commonHandler.scan("*", 100);
            System.out.println("keys => " + keys);
            for (String key : keys) {
                commonHandler.expire(key, 0, TimeUnit.SECONDS);
            }

            redisLock.unlock("lock", flag);
        }
        return AjaxResult.success("success");
    }

    @GetMapping("/call")
    @ApiOperation(value = "多线程测试", notes = "多线程测试")
    public AjaxResult<Integer> call() throws Exception {
        // 初始化数值
        stringHandler.set("qty", 0);

        // 初始化门闩（计数器为任务数量）
        int tastQty = 100;
        CountDownLatch latch = new CountDownLatch(tastQty);
        for (int i = 0; i < tastQty; i++) {
            executor.execute(() -> {
                try {
                    String flag = redisLock.lock("lock", 3, 3);
                    if (flag != null) {
                        Integer qty = Integer.valueOf(stringHandler.get("qty").toString());
                        qty++;
                        stringHandler.set("qty", qty);
                        redisLock.unlock("lock", flag);
                        log.info("线程:{}, 数值:{}", Thread.currentThread().getName(), stringHandler.get("qty"));
                    } else {
                        log.info("线程:{}, 未获取到锁", Thread.currentThread().getName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        // 等待所有任务完成（可设置超时时间）
        latch.await(); // 也可使用 latch.await(5, TimeUnit.SECONDS) 带超时
        return AjaxResult.success(Integer.valueOf(stringHandler.get("qty").toString()));
    }

    @GetMapping("/generateDocx")
    @ApiOperation(value = "生成docx", notes = "根据docx模板生成docx")
    public ResponseEntity<byte[]> generateDocx() {
        try {
            // 准备动态数据，使用Map<String, String>
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("title", "质检单");
            for (int i = 1; i <= 4; i++) {
                dataMap.put("title_" + i, "标题" + i);
                dataMap.put("content_" + i, "内容" + i);
            }
            dataMap.put("author", "软件工程师");
            dataMap.put("company", "鑫海才华有限公司");
            dataMap.put("filing_time", "2025-08-15 23:59:59");

            // 生成docx文件字节数组
            byte[] docxBytes = docxUtils.generateDocx("template.docx", dataMap);

            // 设置响应头，指定文件名和类型
            HttpHeaders headers = new HttpHeaders();
            String fileName = URLEncoder.encode("个人信息表.docx", StandardCharsets.UTF_8.name());
            headers.add("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");

            return new ResponseEntity<>(docxBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/interfaceCall")
    @ApiOperation(value = "使用httpclient调用接口", notes = "使用httpclient调用接口")
    public AjaxResult<Void> interfaceCall() {
        String pageResult = okHttpClientUtils.doGet("https://www.baidu.com/");
        FileUtil.writeUtf8String(pageResult, System.getProperty("user.dir") + File.separator + "response" + File.separator + "baidu.html");
        return AjaxResult.success();
    }

}
