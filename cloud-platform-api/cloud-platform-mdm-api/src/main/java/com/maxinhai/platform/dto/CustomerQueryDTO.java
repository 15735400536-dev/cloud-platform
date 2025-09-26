package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.CustomerVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @ClassName：CustomerQueryDTO
 * @Author: XinHai.Ma
 * @Date: 2025/9/26 15:58
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
@ApiModel(description = "客户分页查询DTO")
public class CustomerQueryDTO extends PageSearch<CustomerVO> {

    /**
     * 客户名称（个人 / 企业名称，如 “张三”“XX 科技有限公司”）
     */
    private String name;
    /**
     * 客户类型：0 - 个人客户，1 - 企业客户
     */
    private Integer type;

}
