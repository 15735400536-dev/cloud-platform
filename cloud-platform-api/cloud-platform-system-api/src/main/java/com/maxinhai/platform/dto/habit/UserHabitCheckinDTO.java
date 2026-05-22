package com.maxinhai.platform.dto.habit;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户打卡DTO")
public class UserHabitCheckinDTO {

    @Schema(description = "习惯类型ID")
    private String habitTypeId;
    @Schema(description = "备注")
    private String remark;

}
