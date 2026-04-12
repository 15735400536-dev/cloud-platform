package com.maxinhai.platform.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import com.maxinhai.platform.po.WorkOrder;
import com.maxinhai.platform.vo.WorkOrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface WorkOrderMapper extends MPJBaseMapper<WorkOrder> {

    /**
     * 根据ID集合批量查询详情（多表关联，超快）
     */
    @Select({
            "<script>",
            "SELECT  " +
            "  t.*, " +
            "  t1.order_code, " +
            "  t3.id as product_id, " +
            "  t3.code as product_code, " +
            "  t3.name as product_name, " +
            "  t4.id as bom_id, " +
            "  t4.code as bom_code, " +
            "  t4.name as bom_name, " +
            "  t5.id as routing_id, " +
            "  t5.code as routing_code, " +
            "  t5.name as routing_name " +
            "FROM prod_work_order t  " +
            "INNER JOIN prod_order t1 on (t1.id = t.order_id)  " +
            "INNER JOIN mdm_product t3 ON (t3.id = t.product_id) " +
            "INNER JOIN mdm_bom t4 ON (t4.id = t.bom_id) " +
            "INNER JOIN mdm_routing t5 ON (t5.id = t.routing_id) " +
            "WHERE t.del_flag = 0 " +
            "AND t1.del_flag = 0 " +
            "AND t3.del_flag = 0 " +
            "AND t4.del_flag = 0 " +
            "AND t5.del_flag = 0 ",
            "AND t.id IN ",
            "    <foreach collection='ids' item='id' open='(' separator=',' close=')'>",
            "        #{id}",
            "    </foreach>",
            "</script>"
    })
    List<WorkOrderVO> findWorkOrderDetailByIds(@Param("ids") List<String> ids);

}
