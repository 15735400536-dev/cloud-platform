package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("mdm_material_type")
public class MaterialType extends RecordEntity {

    private String code;
    private String name;
    private String description;
    private String parentId;

}
