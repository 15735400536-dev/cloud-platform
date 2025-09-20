package com.maxinhai.platform.service.impl;

import com.maxinhai.platform.mapper.VideoMapper;
import com.maxinhai.platform.service.ParseVideoService;
import com.maxinhai.platform.utils.HttpClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Slf4j
@Service
public class ParseVideoServiceImpl implements ParseVideoService {

    @Resource
    private VideoMapper videoMapper;
    @Resource
    private RestTemplate restTemplate;

    @Override
//    @PostConstruct
    public void parseVideo() {
        String url = "https://missav.live/dm34/cn/madou?page=";
        for (int i = 1; i <= 573; i++) {
            String html = getHtml(url + i);
            System.out.println(html);
        }
    }

    @Resource
    private HttpClientUtils httpClientUtils;

    public String getHtml(String url, int page) {
        return httpClientUtils.doGet(url);
    }

    public String getHtml(String url) {
        // 设置请求头，模拟浏览器
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36");
        headers.set("Accept", "*/*");
        headers.set("Accept-Language", "gzip, deflate, br");
        headers.set("Connection", "keep-alive");
        headers.set("Host", "missav.live");
        headers.set("Cache-Control", "no-cache");
        headers.set("cookie", "user_uuid=2ce7fd6d-f406-4adc-a22f-4765223de058; _ga=GA1.1.3122660.1754881547; search_history=[%22%25E5%25B0%2591%25E5%25B9%25B4%25E9%2598%25BF%25E8%25B3%2593%22%2C%22%25E5%25B0%2591%25E5%25A6%2587%25E7%2599%25BD%25E6%25B4%2581%22%2C%22%25E8%2593%259D%25E5%25A4%25A9%25E8%2588%25AA%25E7%25A9%25BA%25E5%2585%25AC%25E5%258F%25B8%25E7%259A%2584%25E7%25A9%25BA%25E5%25A7%2590%22%2C%22%25E5%25A4%259C%25E5%258B%25A4%25E7%2597%2585%25E6%25A0%258B%22]; cf_clearance=.3Y83wcttv0mTbrUHH3uAhgfLW3Drn22Olvy2eeII60-1757233702-1.2.1.1-Fgu3ox8b0nRgj4QclThjVcLSRKG6S6qgYdL2GiuCqcuH8Yk7l_4Wa8wxw.5tPGy7WOOVW0SgYcZVPj8S_9yOkQp8zTL4uHHQxB4cvBd2HBdhCGhk2dms_TRC0MCUljpX3ZwEe2pEApcWO7dziwkbNXVp_fPSypZN6wURZPibpRNdyFkOWiFe0SBNbsUfuVkXpM1kvqmN8DM_Baajuf8sEAyX1_HJ6OgJ.ikHMXYSxw0; _ga_JV54L39Q8H=GS2.1.s1757233702$o14$g1$t1757233707$j55$l0$h0");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );
            // 处理响应
        } catch (ResourceAccessException e) {
            // 连接相关异常处理
            log.error("网络连接错误: {}", e.getMessage());
        } catch (Exception e) {
            // 其他异常处理
            log.error("发生错误: {}", e.getMessage());
        }
        return response.getBody();
    }

}
