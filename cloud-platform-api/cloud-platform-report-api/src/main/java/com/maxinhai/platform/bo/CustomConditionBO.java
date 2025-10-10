package com.maxinhai.platform.bo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.maxinhai.platform.enums.ConditionEnum;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.handler.StrListArrayTypeHandler;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @ClassName：CustomConditionBO
 * @Author: XinHai.Ma
 * @Date: 2025/8/22 15:17
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
@ApiModel(description = "BO")
public class CustomConditionBO {

    /**
     * 查询字段
     */
    private String field;
    /**
     * 条件：大于、大于等于、小于、小于等于、等于、不等于、在范围、不在范围
     */
    private ConditionEnum condition;
    /**
     * 下限
     */
    private String minVal;
    /**
     * 上限
     */
    private String maxVal;
    /**
     * 标准数值
     */
    private String standardVal;
    /**
     * 范围
     */
    // 指定字段类型为ARRAY，使用自定义类型处理器
    @TableField(jdbcType = JdbcType.ARRAY, typeHandler = StrListArrayTypeHandler.class)
    private List<String> range;
    /**
     * 查询SQL
     */
    private String sqlId;

    /**
     * 构建查询条件
     *
     * @return
     */
    public String build() {
        StringBuffer buffer = new StringBuffer(" ");
        buffer.append(field);
        buffer.append(" ");
        switch (condition) {
            case eq:
                buffer.append(condition.getKey()).append(" ").append(standardVal);
                break;
            case ne:
                buffer.append(condition.getKey()).append(" ").append(standardVal);
                break;
            case gt:
                buffer.append(condition.getKey()).append(" ").append(standardVal);
                break;
            case ge:
                buffer.append(condition.getKey()).append(" ").append(standardVal);
                break;
            case lt:
                buffer.append(condition.getKey()).append(" ").append(standardVal);
                break;
            case le:
                buffer.append(condition.getKey()).append(" ").append(standardVal);
                break;
            case in:
                buffer.append(condition.getKey()).append(" (").append(StringUtils.collectionToDelimitedString(range, ",")).append(") ");
                break;
            case not_in:
                buffer.append(condition.getKey()).append(" (").append(StringUtils.collectionToDelimitedString(range, ",")).append(") ");
                break;
            case between:
                buffer.append(condition.getKey()).append(" ").append(minVal).append(" AND ").append(maxVal);
                break;
            case not_between:
                buffer.append(condition.getKey()).append(" ").append(minVal).append(" AND ").append(maxVal);
                break;
            case like:
                buffer.append(condition.getKey()).append(" '%").append(standardVal).append("%'");
                break;
            case like_left:
                buffer.append(condition.getKey()).append(" '%").append(standardVal).append("'");
                break;
            case like_right:
                buffer.append(condition.getKey()).append(" '").append(standardVal).append("%'");
                break;
            case ilike:
                buffer.append(condition.getKey()).append(" '%").append(standardVal).append("%'");
                break;
            case ilike_left:
                buffer.append(condition.getKey()).append(" '%").append(standardVal).append("'");
                break;
            case ilike_right:
                buffer.append(condition.getKey()).append(" '").append(standardVal).append("%'");
                break;
            default:
                throw new BusinessException("未知查询条件!");
        }
        return buffer.append(" ").toString();
    }

}
