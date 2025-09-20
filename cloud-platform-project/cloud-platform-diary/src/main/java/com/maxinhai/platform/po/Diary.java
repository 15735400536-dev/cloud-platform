package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_diary")
public class Diary extends RecordEntity {

    private String title;
    private String content;
    private String diaryTypeId;

}
