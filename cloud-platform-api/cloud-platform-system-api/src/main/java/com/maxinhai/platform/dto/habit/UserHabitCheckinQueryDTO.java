package com.maxinhai.platform.dto.habit;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.habit.UserHabitCheckinVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户打卡记录分页查询DTO")
public class UserHabitCheckinQueryDTO extends PageSearch<UserHabitCheckinVO> {

    @Schema(description = "用户ID")
    private String userId;
    @Schema(description = "习惯类型ID")
    private String habitTypeId;

}
