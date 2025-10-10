package com.maxinhai.platform.listener;

import com.maxinhai.platform.po.*;
import com.maxinhai.platform.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class InitDataListener implements CommandLineRunner {

    @Resource
    private AuthorInfoRepository authorInfoRepository;
    @Resource
    private NovelCategoryRepository categoryRepository;
    @Resource
    private NovelRepository novelRepository;
    @Resource
    private NovelTagRepository tagRepository;
    @Resource
    private NovelChapterRepository chapterRepository;
    @Resource
    private NovelTagRelRepository novelTagRelRepository;
    @Resource
    private NovelChapterContentRepository chapterContentRepository;

    @Override
    public void run(String... args) throws Exception {
        // 创建作者
        AuthorInfo authorInfo = new AuthorInfo();
        authorInfo.setUserId("maxinhai");
        authorInfo.setNickname("马新宇");
        authorInfo.setInfo("2025年新生代杰出白金作家");
        authorInfoRepository.save(authorInfo);

        // 创建分类
        NovelCategory category = new NovelCategory();
        category.setName("剧情");
        category.setSort(1);
        category.setStatus(1);
        category.setDescription("剧情");
        categoryRepository.save(category);

        // 创建小说
        Novel novel = new Novel();
        novel.setTitle("我的28岁");
        novel.setAuthorId(authorInfo.getId());
        novel.setCategoryId(category.getId());
        novel.setCoverUrl("https://www.baidu.com");
        novel.setStatus(1);
        novelRepository.save(novel);

        // 创建标签
        String[] tags = {"东方玄幻", "修仙", "洪荒", "历史", "争霸"};
        for (int i = 0; i < tags.length; i++) {
            NovelTag novelTag = new NovelTag();
            novelTag.setName(tags[i]);
            novelTag.setSort(i + 1);
            tagRepository.save(novelTag);
        }

        // 小说关联标签
        List<NovelTag> tagList = tagRepository.findAll();
        for (NovelTag tag : tagList) {
            NovelTagRel rel = new NovelTagRel();
            rel.setNovelId(novel.getId());
            rel.setTagId(tag.getId());
            novelTagRelRepository.save(rel);
        }

        // 创建章节
        for (int i = 1; i <= 100; i++) {
            NovelChapter chapter = new NovelChapter();
            chapter.setNovelId(novel.getId());
            chapter.setTitle(String.format("第%d章节", i));
            chapter.setSort(i);
            chapter.setIsVip(Boolean.FALSE);
            chapter.setWordCount(2000);
            chapter.setStatus(1);
            chapter.setPublishTime(LocalDateTime.now());
            chapterRepository.save(chapter);

            NovelChapterContent chapterContent = new NovelChapterContent();
            chapterContent.setChapterId(chapter.getId());
            chapterContent.setContent(chapter.getTitle() + "2025年10月1日-10月7日，梁甜甜结婚了！！！");
            chapterContentRepository.save(chapterContent);
        }
    }

}
