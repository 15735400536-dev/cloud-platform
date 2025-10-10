package com.maxinhai.platform;

import com.maxinhai.platform.utils.RestTemplateUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @ClassName：ZlmApplicationTests
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 13:42
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Slf4j
@SpringBootTest
public class ZlmApplicationTests {

    @Resource
    private RestTemplateUtils restTemplateUtils;

    @Test
    public void test() {
        String result = restTemplateUtils.get("https://www.baidu.com", null, String.class);
        log.info(result);
    }

}
