package com.maxinhai.platform.service.impl;

import cn.hutool.db.PageResult;
import com.maxinhai.platform.po.NewsTag;
import com.maxinhai.platform.repository.NewsTagRepository;
import com.maxinhai.platform.service.NewsTagService;
import com.maxinhai.platform.utils.PageSearch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class NewsTagServiceImpl implements NewsTagService {

    @Resource
    private NewsTagRepository newsTagRepository;

    @Override
    public PageResult<NewsTag> pageSearch(PageSearch params) {
        return null;
    }

    @Override
    public NewsTag detail(Long id) {
        return newsTagRepository.getById(id);
    }

    @Override
    public void save(NewsTag param) {
        newsTagRepository.save(param);
    }

    @Override
    public void update(NewsTag param) {
        newsTagRepository.save(param);
    }

    @Override
    public void delete(Long id) {
        newsTagRepository.deleteById(id);
    }
}
