package com.maxinhai.platform.repository;

import com.maxinhai.platform.mapper.RecordRepository;
import com.maxinhai.platform.po.NovelChapterContent;
import org.springframework.stereotype.Repository;

@Repository
public interface NovelChapterContentRepository extends RecordRepository<NovelChapterContent, Long> {
}
