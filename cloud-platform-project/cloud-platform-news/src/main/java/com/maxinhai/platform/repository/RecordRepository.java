package com.maxinhai.platform.repository;

import com.maxinhai.platform.po.RecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 通用Repository基类
 * 提供逻辑删除等通用操作
 */
@NoRepositoryBean // 标识为非Repository Bean，不会被Spring实例化
public interface RecordRepository<T extends RecordEntity> extends JpaRepository<T, Long> {
}