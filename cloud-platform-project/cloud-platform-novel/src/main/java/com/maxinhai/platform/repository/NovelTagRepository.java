package com.maxinhai.platform.repository;

import com.maxinhai.platform.mapper.RecordRepository;
import com.maxinhai.platform.po.NovelTag;
import org.springframework.stereotype.Repository;

@Repository
public interface NovelTagRepository extends RecordRepository<NovelTag, Long> {
}
