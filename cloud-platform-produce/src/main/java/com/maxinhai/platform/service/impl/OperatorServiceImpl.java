package com.maxinhai.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.maxinhai.platform.handler.HashHandler;
import com.maxinhai.platform.mapper.UserMapper;
import com.maxinhai.platform.po.User;
import com.maxinhai.platform.service.OperatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OperatorServiceImpl implements OperatorService {

    private final HashHandler hashHandler;
    private final UserMapper userMapper;
    public static final String cacheKey = "operator";

    @PostConstruct
    public void loadOperator() {
        if (!hashHandler.hasKey(cacheKey)) {
            List<User> userList = userMapper.selectList(new LambdaQueryWrapper<User>()
                    .select(User::getId, User::getUsername));
            if (CollectionUtils.isEmpty(userList)) {
                return;
            }
            Map<Object, Object> userMap = new HashMap<>();
            for (User user : userList) {
                userMap.put(user.getId(), user.getUsername());
            }
            hashHandler.setAll(cacheKey, userMap);
            log.info("操作人缓存加载完毕！");
        }
    }

    @Override
    public Map<String, String> getOperator() {
        Map<Object, Object> userMap = hashHandler.getAll(cacheKey);
        return userMap.entrySet().stream()
                .filter(entry -> entry.getKey() != null) // 可选：过滤 null key
                .collect(HashMap::new,
                        (map, entry) -> {
                            map.put(entry.getKey().toString(),
                                    entry.getValue() == null ? "anonymous" : entry.getValue().toString());
                        },
                        HashMap::putAll);
    }

    @Override
    public String getOperator(String userId) {
        return getOperator().getOrDefault(userId, "anonymous");
    }
}
