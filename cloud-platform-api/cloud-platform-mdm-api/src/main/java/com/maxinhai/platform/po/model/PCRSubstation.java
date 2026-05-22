package com.maxinhai.platform.po.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maxinhai.platform.po.RecordEntity;
import lombok.Data;

@Data
@TableName("mdm_pcr_substation")
public class PCRSubstation extends RecordEntity {

    private String substationCode;
    private String substationName;

}