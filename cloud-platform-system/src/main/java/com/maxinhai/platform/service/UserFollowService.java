package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.follow.CancelDTO;
import com.maxinhai.platform.dto.follow.FollowDTO;
import com.maxinhai.platform.po.UserFollow;
import com.maxinhai.platform.vo.UserFollowVO;
import com.maxinhai.platform.vo.UserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserFollowService extends IService<UserFollow> {

    /**
     * 根据用户ID查询关注列表
     *
     * @param userId 用户ID
     * @return 关注列表
     */
    List<UserFollowVO> getFollowListByUserId(@Param("userId") String userId);

    /**
     * 根据用户ID和关注用户ID查询关注信息
     *
     * @param userId   用户ID
     * @param followId 关注用户ID
     * @return 关注信息
     */
    boolean getFollowByUserIdAndFollowId(@Param("userId") String userId, @Param("followId") String followId);

    /**
     * 关注用户
     * @param param 关注参数
     * @return 操作结果：TRUE.成功 FALSE.失败
     */
    boolean follow(FollowDTO param);

    /**
     * 取消关注
     * @param param 取关参数
     * @return 操作结果：TRUE.成功 FALSE.失败
     */
    boolean cancel(CancelDTO param);

    /**
     * 查询 没关注过任何人 的用户列表
     * @return 用户列表
     */
    List<UserVO> getUnfollowedUserList();

    /**
     * 根据用户ID查询重复关注用户列表数据
     * @param userId 用户ID
     * @return 重复关注用户列表数据
     */
    List<UserVO> getDuplicateFollowedUserList(String userId);

}
