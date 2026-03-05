package com.maxinhai.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.maxinhai.platform.po.UserFollow;
import com.maxinhai.platform.vo.UserFollowVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserFollowMapper extends BaseMapper<UserFollow> {

    /**
     * 通过用户ID查询关注列表
     *
     * @param userId 用户ID
     * @return 关注列表
     */
    @Select(value = "SELECT f.user_id, f.follow_id, u.account, u.username " +
            "FROM mdm_user_follow AS f " +
            "INNER JOIN sys_user AS u ON f.follow_id = u.id " +
            "WHERE f.del_flag = 0 AND u.del_flag = 0 AND f.user_id = #{userId} " +
            "ORDER BY f.create_time ASC")
    List<UserFollowVO> getFollowListByUserId(@Param("userId") String userId);

    /**
     * 通过用户ID和关注用户ID查询关注信息
     *
     * @param userId   用户ID
     * @param followId 关注用户ID
     * @return 关注信息
     */
    @Select(value = "SELECT f.id, f.user_id, f.follow_id, u.account, u.username " +
            "FROM mdm_user_follow AS f " +
            "INNER JOIN sys_user AS u ON f.follow_id = u.id " +
            "WHERE  f.del_flag = 0 AND u.del_flag = 0 " +
            "AND f.user_id = #{userId} AND f.follow_id = #{followId}")
    UserFollow getFollowByUserIdAndFollowId(@Param("userId") String userId, @Param("followId") String followId);

}
