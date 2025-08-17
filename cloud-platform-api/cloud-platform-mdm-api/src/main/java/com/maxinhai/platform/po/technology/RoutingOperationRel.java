package com.maxinhai.platform.po.technology;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maxinhai.platform.po.RecordEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("mdm_routing_operation_rel")
public class RoutingOperationRel extends RecordEntity {

    /**
     * 工艺路线ID
     */
    private String routingId;
    /**
     * 工序ID
     */
    private String operationId;
    /**
     * 工序顺序
     */
    private Integer sort;

}
