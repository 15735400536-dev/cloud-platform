package com.maxinhai.platform.service.habit;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.habit.HabitTypeAddDTO;
import com.maxinhai.platform.dto.habit.HabitTypeEditDTO;
import com.maxinhai.platform.dto.habit.HabitTypeQueryDTO;
import com.maxinhai.platform.po.habit.HabitType;
import com.maxinhai.platform.vo.habit.HabitTypeVO;

public interface HabitTypeService extends IService<HabitType> {

    Page<HabitTypeVO> searchByPage(HabitTypeQueryDTO param);

    HabitTypeVO getInfo(String id);

    void remove(String[] ids);

    void edit(HabitTypeEditDTO param);

    void add(HabitTypeAddDTO param);

}
