package com.maxinhai.platform.mapper;

import com.maxinhai.platform.po.RecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface RecordRepository <T extends RecordEntity, ID> extends JpaRepository<T, ID> {
}
