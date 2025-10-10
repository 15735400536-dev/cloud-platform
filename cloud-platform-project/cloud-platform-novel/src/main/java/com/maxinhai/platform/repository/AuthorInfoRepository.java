package com.maxinhai.platform.repository;

import com.maxinhai.platform.mapper.RecordRepository;
import com.maxinhai.platform.po.AuthorInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorInfoRepository extends RecordRepository<AuthorInfo, Long> {
}
