package com.maxinhai.platform.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import com.maxinhai.platform.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

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
    List<User> queryUserListWithoutRole();

    /**
     * 查询账号重复的用户信息
     *
     * @return
     */
    @Select(value = "select account, count(account) as total " +
            "from sys_user " +
            "where del_flag = 0 " +
            "group by account having count(account) > 1")
    List<Map<String, Object>> queryUserListDuplicateAccount();

    @Select(value = "SELECT id, account, username " +
            "FROM sys_user WHERE account IN ( " +
            "  SELECT account FROM sys_user " +
            "  WHERE del_flag = 0  " +
            "  GROUP BY account  " +
            "  HAVING count(account) > 1  " +
            ") ORDER BY account")
    List<User> selectUserListDuplicateAccount();

    String password = "$2a$10$vSuwgCkk6nfxPI7S/QmaAeNRxEqHHZaNuKN4He5SiSXvnMrJnlhyq";

    @Update(value = "UPDATE sys_user SET password = '" + password + "' " +
            "WHERE del_flag = 0 " +
            "AND account = username " +
            "AND password <> '" + password + "'")
    int updateUserPassword();

}
