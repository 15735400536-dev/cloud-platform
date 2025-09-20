package com.maxinhai.platform.listener;

import cn.hutool.core.date.DateUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.maxinhai.platform.po.NewsCategory;
import com.maxinhai.platform.po.NewsInfo;
import com.maxinhai.platform.po.NewsTag;
import com.maxinhai.platform.repository.NewsCategoryRepository;
import com.maxinhai.platform.repository.NewsInfoRepository;
import com.maxinhai.platform.repository.NewsTagRepository;
import com.maxinhai.platform.utils.JsonFileReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
public class InitDataListsner implements CommandLineRunner {

    @Resource
    private NewsTagRepository newsTagRepository;
    @Resource
    private NewsCategoryRepository newsCategoryRepository;
    @Resource
    private JsonFileReader jsonFileReader;
    @Resource
    private NewsInfoRepository newsInfoRepository;

    @Override
    public void run(String... args) throws Exception {
        initNewsTag();
        initNewsCategory();
        initNewsInfo();
    }

    /**
     * 读取news_tags.json
     *
     * @return
     */
    public JsonNode getNewsTags() {
        try {
            // 读取resources目录下的news_tags.json文件
            return jsonFileReader.readJsonFile("news_tags.json");
        } catch (IOException e) {
            // 处理异常（如文件不存在、格式错误等）
            throw new RuntimeException("读取标签配置文件失败", e);
        }
    }

    /**
     * 初始化新闻标签数据
     */
    public void initNewsTag() {
        long count = newsTagRepository.count();
        if (count == 0) {
            JsonNode newsTags = getNewsTags();
            if (newsTags.isArray()) {
                ArrayNode arrayNode = (ArrayNode) newsTags;
                for (JsonNode jsonNode : arrayNode) {
                    // 第一层
                    NewsTag firstTag = new NewsTag();
                    firstTag.setTag(jsonNode.get("category").asText());
                    newsTagRepository.save(firstTag);

                    // 第二层
                    ArrayNode tags = (ArrayNode) jsonNode.get("tags");
                    for (JsonNode node : tags) {
                        NewsTag secondTag = new NewsTag();
                        String name = node.get("name").asText();
                        secondTag.setTag(name);
                        newsTagRepository.save(secondTag);

                        // 第三层
                        ArrayNode examples = (ArrayNode) node.get("examples");
                        for (JsonNode example : examples) {
                            NewsTag thirdTag = new NewsTag();
                            thirdTag.setTag(example.asText());
                            newsTagRepository.save(thirdTag);
                        }
                    }
                }
            }

//            String[] tags = {"时政新闻", "财经金融", "社会民生", "文体娱乐", "科技数码", "军事国防", "健康养生", "环境气候", "国际热点",
//                    "时效类", "内容性质类", "状态类", "推荐类", "专题聚合类", "互动类", "地域类"};
//            for (String tag : tags) {
//                NewsTag newsTag = new NewsTag();
//                newsTag.setTag(tag);
//                newsTagRepository.save(newsTag);
//            }
        }
    }

    /**
     * 初始化新闻分类数据
     */
    public void initNewsCategory() {
        long count = newsCategoryRepository.count();
        if (count == 0) {
            JsonNode newsTags = getNewsTags();
            if (newsTags.isArray()) {
                int first = 1;
                ArrayNode arrayNode = (ArrayNode) newsTags;
                for (JsonNode jsonNode : arrayNode) {
                    // 第一层
                    NewsCategory firstCategory = new NewsCategory();
                    firstCategory.setCategoryName(jsonNode.get("category").asText());
                    firstCategory.setParentId(0L);
                    firstCategory.setSort(first);
                    newsCategoryRepository.save(firstCategory);
                    first++;

                    // 第二层
                    int second = 1;
                    ArrayNode tags = (ArrayNode) jsonNode.get("tags");
                    for (JsonNode node : tags) {
                        NewsCategory secondCategory = new NewsCategory();
                        String name = node.get("name").asText();
                        secondCategory.setCategoryName(name);
                        secondCategory.setParentId(firstCategory.getId());
                        secondCategory.setSort(second);
                        newsCategoryRepository.save(secondCategory);
                        second++;

                        // 第三层
                        int third = 1;
                        ArrayNode examples = (ArrayNode) node.get("examples");
                        for (JsonNode example : examples) {
                            NewsCategory thirdCategory = new NewsCategory();
                            thirdCategory.setCategoryName(example.asText());
                            thirdCategory.setParentId(secondCategory.getId());
                            thirdCategory.setSort(third);
                            newsCategoryRepository.save(thirdCategory);
                            third++;
                        }
                    }
                }
            }

//            String[] categories = {"时政新闻", "财经金融", "社会民生", "文体娱乐", "科技数码", "军事国防", "健康养生", "环境气候", "国际热点",
//                    "时效类", "内容性质类", "状态类", "推荐类", "专题聚合类", "互动类", "地域类"};
//            for (String category : categories) {
//                NewsCategory newsCategory = new NewsCategory();
//                newsCategory.setCategoryName(category);
//                newsCategory.setParentId(0L);
//                newsCategoryRepository.save(newsCategory);
//            }
        }
    }

    /**
     * 初始化新闻信息
     */
    public void initNewsInfo() {
        String format = DateUtil.format(LocalDateTime.now(), "yyyy-MM-dd HH:mm:ss");
        NewsInfo newsInfo = new NewsInfo();
        newsInfo.setTitle("标题【" + format + "】");
        newsInfo.setSummary("新闻摘要【" + format + "】");
        newsInfo.setContent("新闻正文");
        newsInfo.setCoverImg("封面图URL【" + format + "】");
        newsInfo.setCategoryId(162L);
        newsInfo.setSource("原创");
        newsInfo.setAuthorId("maxinhai");
        newsInfo.setStatus(0);
        newsInfo.setViewCount(0L);
        newsInfo.setLikeCount(0L);
        newsInfo.setCommentCount(0L);
        newsInfoRepository.save(newsInfo);
    }

}
