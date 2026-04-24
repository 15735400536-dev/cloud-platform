package com.maxinhai.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.maxinhai.platform.po.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
