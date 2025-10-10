package com.maxinhai.platform.repository;

import com.maxinhai.platform.mapper.RecordRepository;
import com.maxinhai.platform.po.NovelCategory;
import org.springframework.stereotype.Repository;

@Repository
public interface NovelCategoryRepository extends RecordRepository<NovelCategory, Long> {
}
