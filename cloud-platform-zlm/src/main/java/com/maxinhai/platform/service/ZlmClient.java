package com.maxinhai.platform.service;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Map;

//@Slf4j
@Component
public class ZlmClient {

    @Resource
    private RestTemplate restTemplate;

    public Map<String, Object> getApiList(String secret) {
        return restTemplate.getForObject("http://localhost:8081/index/api/getApiList?secret=" + secret, Map.class);
    }

    public Map<String, Object> getVersion(String secret) {
        return restTemplate.getForObject("http://localhost:8081/index/api/version?secret=" + secret, Map.class);
    }


}
