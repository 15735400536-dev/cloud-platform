package com.maxinhai.platform.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.maxinhai.platform.dto.worktime.UserWorkTimeQueryDTO;
import com.maxinhai.platform.mapper.OperateRecordMapper;
import com.maxinhai.platform.mapper.TaskOrderMapper;
import com.maxinhai.platform.mapper.UserMapper;
import com.maxinhai.platform.po.OperateRecord;
import com.maxinhai.platform.po.User;
import com.maxinhai.platform.service.WorkTimeService;
import com.maxinhai.platform.utils.WorkTimeUtils;
import com.maxinhai.platform.vo.worktime.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkTimeServiceImpl implements WorkTimeService {

    private final OperateRecordMapper operateRecordMapper;
    private final TaskOrderMapper taskOrderMapper;
    private final UserMapper userMapper;

    @Override
    public Page<UserWorkTimeVO> searchPage(UserWorkTimeQueryDTO param) {
        long begin = System.currentTimeMillis();
        Page<User> userPage = userMapper.selectPage(new Page<>(param.getCurrent(), param.getSize()),
                new LambdaQueryWrapper<User>()
                        .select(User::getId, User::getAccount, User::getUsername)
                        .like(StrUtil.isNotBlank(param.getAccount()), User::getAccount, param.getAccount())
                        .like(StrUtil.isNotBlank(param.getUsername()), User::getUsername, param.getUsername())
                        .orderByAsc(User::getUsername));
        long end = System.currentTimeMillis();
        log.info("用户数据查询耗时：{}", end - begin);
        List<User> records = userPage.getRecords();

        Page<UserWorkTimeVO> pageResult = new Page<>();
        pageResult.setRecords(Lists.newArrayList());
        pageResult.setTotal(userPage.getTotal());
        pageResult.setSize(userPage.getSize());
        pageResult.setCurrent(userPage.getCurrent());
        pageResult.setSearchCount(false);
        pageResult.setOptimizeCountSql(userPage.optimizeJoinOfCountSql());
        pageResult.setOrders(userPage.getOrders());
        pageResult.setOptimizeJoinOfCountSql(userPage.optimizeJoinOfCountSql());
        pageResult.setMaxLimit(userPage.getMaxLimit());
        pageResult.setCountId(userPage.getCountId());

        if (!CollectionUtils.isEmpty(records)) {
            List<String> userIds = records.stream().map(User::getId).collect(Collectors.toList());
            begin = System.currentTimeMillis();
            List<UserWorkTimeVO> userWorkTimeVOList = operateRecordMapper.selectUserTaskOrderWorkTimeByUserIds(userIds);
            end = System.currentTimeMillis();

            log.info("用户工时数据条数:{}, 查询耗时：{}", userWorkTimeVOList.size(), end - begin);
            Map<String, UserWorkTimeVO> userWorkTimeMap = userWorkTimeVOList.stream()
                    .collect(Collectors.toMap(UserWorkTimeVO::getUserId, UserWorkTimeVO -> UserWorkTimeVO));

            List<UserWorkTimeVO> voList = records.stream().map(user -> {
                // 一行代码搞定：不存在则创建，存在则获取
                UserWorkTimeVO userWorkTimeVO = userWorkTimeMap.computeIfAbsent(user.getId(), id -> {
                    UserWorkTimeVO vo = new UserWorkTimeVO();
                    vo.setTaskOrderFinishQty(0L);
                    vo.setTotalWorkTime(0L);
                    return vo;
                });

                // 统一设置用户信息（不管是不是新创建的，都要覆盖）
                userWorkTimeVO.setUserId(user.getId());
                userWorkTimeVO.setAccount(user.getAccount());
                userWorkTimeVO.setUsername(user.getUsername());
                return userWorkTimeVO;
            }).collect(Collectors.toList());
            pageResult.setRecords(voList);
        }
        return pageResult;
    }

    @Override
    public EmployeeWorkTimeVO getEmployeeWorkTime() {
        return null;
    }

    @Override
    public TeamWorkTimeVO getTeamWorkTime() {
        return null;
    }

    @Override
    public WorkShopWorkTimeVO getWorkShopWorkTime() {
        return null;
    }

    @Override
    public ProductionLineWorkTimeVO getProductionLineWorkTime() {
        return null;
    }

    @Override
    public EquipWorkTimeVO getEquipWorkTime() {
        return null;
    }

    @Override
    public WorkOrderWorkTimeVO getWorkOrderWorkTime(String workOrderId) {
        List<OperateRecord> recordList = operateRecordMapper.getWorkOrderWorkTime(workOrderId);
        BigDecimal workHour = WorkTimeUtils.calculateWorkOrderWorkHour(recordList);
        return new WorkOrderWorkTimeVO(workOrderId, "", workHour.longValue());
    }

    @Override
    public OrderWorkTimeVO getOrderWorkTime(String orderId) {
        List<OperateRecord> recordList = operateRecordMapper.getOrderWorkTime(orderId);
        BigDecimal workHour = WorkTimeUtils.calculateOrderWorkHour(recordList);
        return new OrderWorkTimeVO(orderId, "", workHour.longValue());
    }

    @Override
    public List<CountTaskFinishQtyVO> countTaskFinishQtyByWorkOrderId() {
        List<CountTaskFinishQtyVO> taskFinishQtyVOList = taskOrderMapper.countTaskFinishQtyByWorkOrderId();
        return taskFinishQtyVOList.size() > 100 ? taskFinishQtyVOList.subList(0, 100) : taskFinishQtyVOList;
    }

    @Override
    public List<CountTaskFinishQtyVO> countTaskFinishQtyByOrderId() {
        List<CountTaskFinishQtyVO> taskFinishQtyVOList = taskOrderMapper.countTaskFinishQtyByOrderId();
        return taskFinishQtyVOList.size() > 100 ? taskFinishQtyVOList.subList(0, 100) : taskFinishQtyVOList;
    }
}
