package com.maxinhai.platform.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 自动填充
 */
@Component
public class MyBatisPlusMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
//        this.strictInsertFill(metaObject, "createBy", String.class, LoginUserContext.getItemValue("account", "anonymous"));
        this.strictInsertFill(metaObject, "createBy", String.class, "anonymous");
        this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
//        this.strictInsertFill(metaObject, "updateBy", String.class, LoginUserContext.getItemValue("account", "anonymous"));
        this.strictInsertFill(metaObject, "updateBy", String.class, "anonymous");
        this.strictInsertFill(metaObject, "delFlag", Integer.class, 0);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
//        this.strictInsertFill(metaObject, "updateBy", String.class, LoginUserContext.getItemValue("account", "anonymous"));
        this.strictInsertFill(metaObject, "updateBy", String.class, "anonymous");
    }

}
