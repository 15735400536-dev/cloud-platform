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

}
