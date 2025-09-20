package com.maxinhai.platform.utils;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.UUID;

/**
 * 自定义生成器
 */
public class CustomUUIDGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        // 生成UUID并去掉横线（例如：从"a1b2c3d4-5678-90ef-ghij-klmnopqrstuv"变为"a1b2c3d4567890efghijklmnopqrstuv"）
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
