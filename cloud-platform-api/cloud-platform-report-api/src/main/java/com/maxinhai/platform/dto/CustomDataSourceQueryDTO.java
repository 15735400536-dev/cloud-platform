package com.maxinhai.platform.dto;

import com.baomidou.mybatisplus.annotation.DbType;
import com.maxinhai.platform.vo.CustomDataSourceVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @ClassName：CustomDateSourceQueryDTO
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 18:08
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
@ApiModel("DTO")
public class CustomDataSourceQueryDTO extends PageSearch<CustomDataSourceVO> {

    /**
     * 数据源标识
     */
    private String key;
    /**
     * 数据库类型（MySQL、PgSQL、Oracle、SqlServer）
     */
    private DbType type;

}
