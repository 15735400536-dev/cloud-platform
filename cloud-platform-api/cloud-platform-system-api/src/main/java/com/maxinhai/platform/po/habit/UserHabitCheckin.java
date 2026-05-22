package com.maxinhai.platform.po.habit;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maxinhai.platform.po.RecordEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 习惯打卡主表
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("user_habit_checkin")
public class UserHabitCheckin extends RecordEntity {

    private String userId;
    private String habitTypeId;
    private String remark;

}
