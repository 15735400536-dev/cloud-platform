package com.maxinhai.platform.repository;

import com.maxinhai.platform.mapper.RecordRepository;
import com.maxinhai.platform.po.NovelChapter;
import org.springframework.stereotype.Repository;

@Repository
public interface NovelChapterRepository extends RecordRepository<NovelChapter, Long> {
}
