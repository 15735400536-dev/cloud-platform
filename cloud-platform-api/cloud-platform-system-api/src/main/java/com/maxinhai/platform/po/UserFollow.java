package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("mdm_user_follow")
public class UserFollow extends RecordEntity {

    /**
     * 用户ID
     */
    private String userId;
    /**
     * 关注用户ID
     */
    private String followId;

}
