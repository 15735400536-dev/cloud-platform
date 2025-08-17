package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("mdm_material")
public class Material extends RecordEntity {

    private String code;
    private String name;
    private String materialTypeId;
    private String unit;
    private String description;
    private String model;
    private String specs;
    /**
     * 图号
     */
    private String drawingNo;
    /**
     * 材质
     */
    private String material;
    private String remark;

}
