package com.maxinhai.platform.dto.habit;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.habit.HabitTypeVO;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "习惯类型-分页查询VO")
public class HabitTypeQueryDTO extends PageSearch<HabitTypeVO> {

    @ApiModelProperty(value = "习惯类型编码")
    private String typeCode;
    @ApiModelProperty(value = "习惯类型名称")
    private String typeName;

}
