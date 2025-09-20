package com.maxinhai.platform.service;

import cn.hutool.db.PageResult;
import com.maxinhai.platform.po.NewsTag;
import com.maxinhai.platform.utils.PageSearch;

public interface NewsTagService {

    PageResult<NewsTag> pageSearch(PageSearch params);

    NewsTag detail(Long id);

    void save(NewsTag param);

    void update(NewsTag param);

    void delete(Long id);

}
