package com.maxinhai.platform.service.habit;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.habit.UserHabitCheckinQueryDTO;
import com.maxinhai.platform.dto.habit.UserHabitCheckinDTO;
import com.maxinhai.platform.po.habit.UserHabitCheckin;
import com.maxinhai.platform.vo.habit.UserHabitCheckinVO;

public interface UserHabitCheckinService extends IService<UserHabitCheckin> {

    Page<UserHabitCheckinVO> searchByPage(UserHabitCheckinQueryDTO param);

    UserHabitCheckinVO getInfo(String id);

    /**
     * 用户习惯打卡
     * @param param
     * @return
     */
    boolean checkin(UserHabitCheckinDTO param);

}
