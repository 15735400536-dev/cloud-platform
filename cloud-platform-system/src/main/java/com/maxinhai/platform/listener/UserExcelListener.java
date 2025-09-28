package com.maxinhai.platform.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.maxinhai.platform.excel.UserExcel;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.mapper.RoleMapper;
import com.maxinhai.platform.mapper.UserMapper;
import com.maxinhai.platform.po.Role;
import com.maxinhai.platform.po.User;
import com.maxinhai.platform.po.UserRoleRel;
import com.maxinhai.platform.service.UserRoleRelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Excel导入监听器，用于处理读取到的Excel数据
 */
@Slf4j
@Component
public class UserExcelListener implements ReadListener<UserExcel> {

    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private UserRoleRelService userRoleRelService;
    @Resource
    private PasswordEncoder passwordEncoder;

    // 批量处理阈值，达到该数量就进行一次处理
    private static final int BATCH_COUNT = 100;

    // 存储读取到的数据
    private List<UserExcel> dataList = new ArrayList<>(BATCH_COUNT);

    /**
     * 每读取一行数据就会调用该方法
     */
    @Override
    public void invoke(UserExcel data, AnalysisContext context) {
        log.info("读取到数据: {}", data);
        dataList.add(data);

        // 达到BATCH_COUNT，需要去存储一次数据库，防止数据量太大，内存溢出
        if (dataList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            dataList.clear();
        }
    }

    /**
     * 所有数据读取完成后调用该方法
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也被存储
        saveData();
        // 存储完成清理 list
        dataList.clear();
        log.info("所有数据解析完成！");
    }

    /**
     * 保存数据到数据库
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveData() {
        log.info("开始保存 {} 条数据到数据库", dataList.size());
        // 没有内容不执行后面操作
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        // 保存数据
        Set<String> accountSet = dataList.stream().map(UserExcel::getAccount).collect(Collectors.toSet());
        List<User> userList = userMapper.selectList(new LambdaQueryWrapper<User>()
                .in(User::getAccount, accountSet));
        if (!userList.isEmpty()) {
            Set<String> repeatAccountSet = userList.stream().map(User::getAccount).collect(Collectors.toSet());
            String msg = StringUtils.collectionToDelimitedString(repeatAccountSet, ",");
            throw new BusinessException("账号【" + msg + "】已存在！");
        }

        Role role = roleMapper.selectOne(new LambdaQueryWrapper<Role>()
                .select(Role::getId, Role::getRoleKey, Role::getRoleName)
                .eq(Role::getRoleKey, "USER"));
        if (Objects.isNull(role)) {
            throw new BusinessException("未找到【普通用户】角色!");
        }

        List<UserRoleRel> relList = new ArrayList<>(dataList.size());
        for (UserExcel userExcel : dataList) {
            // 保存用户
            User user = UserExcel.build(userExcel);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userMapper.insert(user);

            // 关联用户角色
            UserRoleRel userRoleRel = new UserRoleRel(user.getId(), role.getId());
            relList.add(userRoleRel);
        }
        userRoleRelService.saveBatch(relList);
        log.info("数据保存完成！");
    }

}
