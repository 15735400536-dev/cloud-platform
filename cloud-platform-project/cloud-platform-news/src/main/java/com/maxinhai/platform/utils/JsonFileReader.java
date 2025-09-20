package com.maxinhai.platform.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class JsonFileReader {

    @Autowired
    private ResourceLoader resourceLoader;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 读取resources目录下的JSON文件并转换为JsonNode对象
     * @param filePath 相对于resources的路径（如：news_tags.json）
     * @return JsonNode对象
     * @throws IOException 当文件不存在或读取失败时抛出
     */
    public JsonNode readJsonFile(String filePath) throws IOException {
        // 加载资源文件
        Resource resource = resourceLoader.getResource("classpath:" + filePath);

        // 读取文件内容并转换为JsonNode
        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readTree(inputStream);
        }
    }

}
