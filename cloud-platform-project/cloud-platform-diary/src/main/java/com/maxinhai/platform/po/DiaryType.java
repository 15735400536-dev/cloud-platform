package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_diary_type")
public class DiaryType extends RecordEntity {

    private String name;

}
