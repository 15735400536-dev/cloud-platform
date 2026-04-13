package com.maxinhai.platform.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import com.maxinhai.platform.po.User;
import com.maxinhai.platform.vo.report.UserGrowthTrendOfDayVO;
import com.maxinhai.platform.vo.report.UserGrowthTrendOfMonthVO;
import com.maxinhai.platform.vo.report.UserGrowthTrendOfYearVO;
import com.maxinhai.platform.vo.report.UserStatisticsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper extends MPJBaseMapper<User> {

    @Select(value = "SELECT DISTINCT account, username FROM sys_user WHERE del_flag = 0")
    List<User> getUserList();

    @Select(value = "select id, account, username from sys_user where del_flag = 0 order by create_time asc limit #{limit} offset #{offset}")
    List<User> selectUserList(@Param("limit") int limit, @Param("offset") int offset);

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

    @Select(value = "SELECT (SELECT COUNT(id) FROM sys_user WHERE del_flag = 0) AS totalUserCount, " +
            "  (SELECT COUNT(id) FROM sys_user WHERE del_flag = 0 AND create_time >= CURRENT_DATE AND create_time < CURRENT_DATE + INTERVAL '1 day') AS newUserCount, " +
            "  (SELECT COUNT(id) FROM sys_user WHERE del_flag = 0 AND sex = '男') AS menUserCount, " +
            "  (SELECT COUNT(id) FROM sys_user WHERE del_flag = 0 AND sex = '女') AS womenUserCount, " +
            "  (SELECT COUNT(id) FROM sys_user WHERE del_flag = 0 AND sex = '未知') AS unknownUserCount, " +
            "  (SELECT COUNT(id) FROM sys_login_log WHERE del_flag = 0 AND create_time >= CURRENT_DATE AND create_time < CURRENT_DATE + INTERVAL '1 day') AS todayLoginUserCount, " +
            "  (SELECT COUNT(id) FROM sys_login_log WHERE del_flag = 0 AND create_time >= date_trunc('month', now()) AND create_time < date_trunc('month', now()) + INTERVAL '1 month') AS monthLoginUserCount, " +
            "  (SELECT COUNT(id) FROM sys_login_log WHERE del_flag = 0 AND create_time >= date_trunc('year', now()) AND create_time < date_trunc('year', now()) + INTERVAL '1 year') AS yearLoginUserCount ")
    UserStatisticsVO getUserStatistics();

    /**
     * 按天统计新增用户
     * @return
     */
    @Select(value = "SELECT " +
            "    DATE(create_time) AS stat_date, " +
            "    COUNT(id) AS user_count " +
            "FROM sys_user " +
            "WHERE del_flag = 0 " +
            "GROUP BY DATE(create_time) " +
            "ORDER BY stat_date ASC")
    List<UserGrowthTrendOfDayVO> getUserGrowthTrendOfDay();

    /**
     * 按月统计新增用户
     * @return
     */
    @Select(value = "SELECT " +
            "    TO_CHAR(create_time, 'YYYY-MM') AS stat_month, " +
            "    COUNT(id) AS user_count " +
            "FROM sys_user " +
            "WHERE del_flag = 0 " +
            "GROUP BY TO_CHAR(create_time, 'YYYY-MM') " +
            "ORDER BY stat_month ASC")
    List<UserGrowthTrendOfMonthVO> getUserGrowthTrendOfMonth();

    /**
     * 按年统计新增用户
     * @return
     */
    @Select(value = "SELECT " +
            "    TO_CHAR(create_time, 'YYYY') AS stat_year, " +
            "    COUNT(id) AS user_count " +
            "FROM sys_user " +
            "WHERE del_flag = 0 " +
            "GROUP BY TO_CHAR(create_time, 'YYYY') " +
            "ORDER BY stat_year ASC")
    List<UserGrowthTrendOfYearVO> getUserGrowthTrendOfYear();

}
