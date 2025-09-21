package com.maxinhai.platform.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import com.maxinhai.platform.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends MPJBaseMapper<User> {

    @Select(value = "SELECT DISTINCT account, username FROM sys_user WHERE del_flag = 0")
    List<User> getUserList();

    /**
     * 查询未绑定任何角色的用户列表
     *
     * @return
     */
    @Select(value = "SELECT u.id, u.account, u.username " +
            "FROM sys_user u " +
            "LEFT JOIN sys_user_role_rel rel ON rel.del_flag = 0 AND u.id = rel.user_id " +
            "WHERE u.del_flag = 0 " +
            "  AND rel.user_id IS NULL")
    List<User> findUserListWithoutRole();

    /**
     * 查询未绑定任何角色的用户列表
     *
     * @return
     */
    @Select(value = "SELECT u.id, u.account, u.username " +
            "FROM sys_user u " +
            "WHERE u.del_flag = 0 " +
            "  AND NOT EXISTS(" +
            "    SELECT 1 FROM sys_user_role_rel rel WHERE rel.del_flag = 0 AND rel.user_id = u.id" +
            "  );")
    List<User> queryUserListWithRole();

}
