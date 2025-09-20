package com.maxinhai.platform.listener;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.maxinhai.platform.handler.ListHandler;
import com.maxinhai.platform.po.NewsInfo;
import com.maxinhai.platform.repository.NewsInfoRepository;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class RedisDataListener {

    @Resource
    private NewsInfoRepository newsInfoRepository;
    @Resource
    private ListHandler listHandler;

    @XxlJob("publishNews")
    public void publishNews() {
        // TODO Redis 6.2.0版本之后支持
//        List<Object> msgList = listHandler.batchLeftPop("list:news", 10);
//        if(CollectionUtils.isEmpty(msgList)) {
//            return;
//        }
//        for (Object o : msgList) {
//            String format = DateUtil.format(LocalDateTime.now(), "yyyy-MM-dd HH:mm:ss");
//            NewsInfo newsInfo = new NewsInfo();
//            newsInfo.setTitle("标题【" + o.toString() + "】");
//            newsInfo.setSummary("新闻摘要【" + o.toString() + "】");
//            newsInfo.setContent("新闻正文【" + o.toString() + "】");
//            newsInfo.setCoverImg("封面图URL【" + format + "】");
//            newsInfo.setCategoryId(162L);
//            newsInfo.setSource("原创");
//            newsInfo.setAuthorId("maxinhai");
//            newsInfo.setStatus(0);
//            newsInfo.setViewCount(0L);
//            newsInfo.setLikeCount(0L);
//            newsInfo.setCommentCount(0L);
//            newsInfoRepository.save(newsInfo);
//        }

        // TODO Redis 6.2.0版本只能一个一个元素取值
        List<Object> objectList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Object object = listHandler.leftPop("list:news");
            objectList.add(object);
        }
        if (!CollectionUtils.isEmpty(objectList)) {
            for (Object object : objectList) {
                JSONObject parseObj = JSONUtil.parseObj(object.toString());
                String title = String.format("欢迎账号【%s】用户名【%s】使用系统", parseObj.getStr("userId"), parseObj.getStr("nickname"));

                NewsInfo newsInfo = new NewsInfo();
                newsInfo.setTitle(title);
                newsInfo.setSummary("新闻摘要【" + title + "】");
                newsInfo.setContent("新闻正文【" + title + "】");
                newsInfo.setCoverImg("封面图URL");
                newsInfo.setCategoryId(162L);
                newsInfo.setSource("原创");
                newsInfo.setAuthorId("system");
                newsInfo.setStatus(0);
                newsInfo.setViewCount(0L);
                newsInfo.setLikeCount(0L);
                newsInfo.setCommentCount(0L);
                newsInfoRepository.save(newsInfo);
            }
        }
        XxlJobHelper.log("发布新闻完成");
    }

}
