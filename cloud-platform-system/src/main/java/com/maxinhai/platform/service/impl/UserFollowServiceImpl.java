package com.maxinhai.platform.service.impl;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxinhai.platform.dto.follow.CancelDTO;
import com.maxinhai.platform.dto.follow.FollowDTO;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.mapper.UserFollowMapper;
import com.maxinhai.platform.mapper.UserMapper;
import com.maxinhai.platform.po.UserFollow;
import com.maxinhai.platform.po.User;
import com.maxinhai.platform.service.UserFollowService;
import com.maxinhai.platform.vo.UserFollowVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserFollowServiceImpl extends ServiceImpl<UserFollowMapper, UserFollow> implements UserFollowService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserFollowMapper followMapper;
    @Resource
    @Qualifier(value = "ioIntensiveExecutor")
    private Executor ioIntensiveExecutor;

    @PostConstruct
    public void initData() {
        List<User> userList = userMapper.selectList(new LambdaQueryWrapper<User>()
                .select(User::getId, User::getAccount, User::getUsername));

        for (User user : userList) {
            ioIntensiveExecutor.execute(() -> {
                Set<String> userFollowSet = getUserFollowSet(user.getId());

                List<UserFollow> followList = userList.stream()
                        .filter(item -> !user.getId().equals(item.getId()))
                        .filter(item -> !userFollowSet.contains(user.getId() + "-" + item.getId()))
                        .map(item -> {
                            UserFollow follow = new UserFollow();
                            follow.setUserId(user.getId());
                            follow.setFollowId(item.getId());
                            return follow;
                        })
                        .collect(Collectors.toList());
                this.saveBatch(followList);
                log.info("生成账号:{}关注信息", user.getAccount());
            });
        }
    }

    /**
     * 获取用户关注Set
     *
     * @param userId 用户ID
     * @return
     */
    public Set<String> getUserFollowSet(String userId) {
        List<UserFollow> followList = followMapper.selectList(new LambdaQueryWrapper<UserFollow>()
                .select(UserFollow::getUserId, UserFollow::getFollowId)
                .eq(UserFollow::getUserId, userId));
        return followList.stream()
                .map(follow -> follow.getUserId() + "-" + follow.getFollowId())
                .collect(Collectors.toSet());
    }

    @Override
    public List<UserFollowVO> getFollowListByUserId(String userId) {
        List<UserFollowVO> followVOList = followMapper.getFollowListByUserId(userId);
        return (!CollectionUtils.isEmpty(followVOList) && followVOList.size() > 500) ?
                followVOList.subList(0, 500) : followVOList;
    }

    @Override
    public boolean getFollowByUserIdAndFollowId(String userId, String followId) {
        UserFollow follow = followMapper.getFollowByUserIdAndFollowId(userId, followId);
        return Objects.nonNull(follow);
    }

    @Override
    public boolean follow(FollowDTO param) {
        UserFollow follow = followMapper.getFollowByUserIdAndFollowId(param.getUserId(), param.getFollowId());
        if (Objects.nonNull(follow)) {
            return true;
        }
        boolean exists = userMapper.exists(new LambdaQueryWrapper<User>().eq(User::getId, param.getUserId()));
        if (!exists) {
            throw new BusinessException("用户ID不存在或已删除！");
        }
        exists = userMapper.exists(new LambdaQueryWrapper<User>().eq(User::getId, param.getFollowId()));
        if (!exists) {
            throw new BusinessException("关注用户ID不存在或已删除！");
        }
        follow = new UserFollow();
        follow.setUserId(param.getUserId());
        follow.setFollowId(param.getFollowId());
        int row = followMapper.insert(follow);
        return row > 0;
    }

    @Override
    public boolean cancel(CancelDTO param) {
        UserFollow follow = followMapper.getFollowByUserIdAndFollowId(param.getUserId(), param.getFollowId());
        int row = followMapper.deleteById(follow.getId());
        return row > 0;
    }
}
