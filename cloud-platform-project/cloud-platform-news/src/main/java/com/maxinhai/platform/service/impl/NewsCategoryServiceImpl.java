package com.maxinhai.platform.service.impl;

import cn.hutool.db.PageResult;
import com.maxinhai.platform.po.NewsCategory;
import com.maxinhai.platform.repository.NewsCategoryRepository;
import com.maxinhai.platform.service.NewsCategoryService;
import com.maxinhai.platform.utils.PageSearch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class NewsCategoryServiceImpl implements NewsCategoryService {

    @Resource
    private NewsCategoryRepository newsCategoryRepository;

    @Override
    public PageResult<NewsCategory> pageSearch(PageSearch params) {
        return null;
    }

    @Override
    public NewsCategory detail(Long id) {
        return newsCategoryRepository.getById(id);
    }

    @Override
    public void save(NewsCategory param) {
        newsCategoryRepository.save(param);
    }

    @Override
    public void update(NewsCategory param) {
        newsCategoryRepository.save(param);
    }

    @Override
    public void delete(Long id) {
        newsCategoryRepository.deleteById(id);
    }
}
