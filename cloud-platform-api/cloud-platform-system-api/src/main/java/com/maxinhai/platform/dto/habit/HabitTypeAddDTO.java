package com.maxinhai.platform.dto.habit;

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
@Schema(description = "习惯类型-新增VO")
public class HabitTypeAddDTO {

    @ApiModelProperty(value = "习惯类型编码")
    private String typeCode;
    @ApiModelProperty(value = "习惯类型名称")
    private String typeName;

}
