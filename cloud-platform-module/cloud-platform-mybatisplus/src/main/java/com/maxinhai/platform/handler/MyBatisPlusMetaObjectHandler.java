package com.maxinhai.platform.handler;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.maxinhai.platform.utils.JwtUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 自动填充
 */
@Component
public class MyBatisPlusMetaObjectHandler implements MetaObjectHandler {

    @Resource
    private JwtUtils jwtUtils;

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "createBy", String.class, getUserId(getToken()));
        this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "updateBy", String.class, getUserId(getToken()));
        this.strictInsertFill(metaObject, "delFlag", Integer.class, 0);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "updateBy", String.class, getUserId(getToken()));
    }

    /**
     * 从token获取userId
     *
     * @param token
     * @return
     */
    public String getUserId(String token) {
        if (StrUtil.isEmpty(token)) {
            return "anonymous";
        }
        if ("anonymous".equals(token)) {
            return token;
        }
        return jwtUtils.getUserIdFromToken(getToken());
    }

    /**
     * 获取当前请求头token（TODO 异步线程下，或获取不到请求上下文，需要改进）
     *
     * @return
     */
    public String getToken() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            return "anonymous";
        }
        HttpServletRequest request = attrs.getRequest();
        String token = request.getHeader("Authorization");
        return token;
    }

}
