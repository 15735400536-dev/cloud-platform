package com.maxinhai.platform.service;

import cn.hutool.db.PageResult;
import com.maxinhai.platform.po.NewsCategory;
import com.maxinhai.platform.utils.PageSearch;

public interface NewsCategoryService {

    PageResult<NewsCategory> pageSearch(PageSearch params);

    NewsCategory detail(Long id);

    void save(NewsCategory param);

    void update(NewsCategory param);

    void delete(Long id);

}
