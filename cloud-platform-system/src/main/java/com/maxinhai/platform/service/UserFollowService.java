package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.follow.CancelDTO;
import com.maxinhai.platform.dto.follow.FollowDTO;
import com.maxinhai.platform.po.UserFollow;
import com.maxinhai.platform.vo.UserFollowVO;
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

    boolean follow(FollowDTO param);

    boolean cancel(CancelDTO param);

}
