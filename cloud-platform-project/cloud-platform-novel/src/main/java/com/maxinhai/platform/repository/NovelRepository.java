package com.maxinhai.platform.repository;

import com.maxinhai.platform.mapper.RecordRepository;
import com.maxinhai.platform.po.Novel;
import org.springframework.stereotype.Repository;

@Repository
public interface NovelRepository extends RecordRepository<Novel, Long> {
}
