package com.maxinhai.platform.dto.follow;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "取消关注用户DTO")
public class CancelDTO {

    @Schema(description = "用户ID")
    private String userId;
    @Schema(description = "关注用户ID")
    private String followId;

}
