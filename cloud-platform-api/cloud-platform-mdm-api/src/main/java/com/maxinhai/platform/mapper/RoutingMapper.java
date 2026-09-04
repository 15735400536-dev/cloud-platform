package com.maxinhai.platform.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import com.maxinhai.platform.po.technology.Routing;
import com.maxinhai.platform.vo.technology.OperationVO;
import com.maxinhai.platform.vo.technology.RoutingInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoutingMapper extends MPJBaseMapper<Routing> {

    @Select(value = "SELECT " +
            "  a.id, " +
            "  a.code, " +
            "  a.name, " +
            "  a.product_id, " +
            "  b.code product_code, " +
            "  b.name product_name, " +
            "  a.version, " +
            "  a.status, " +
            "  a.create_time, " +
            "  a.update_time " +
            "FROM " +
            "  mdm_routing a " +
            "  INNER JOIN mdm_product b ON a.product_id = b.id " +
            "WHERE " +
            "  b.code = #{productCode} " +
            "  AND a.VERSION = #{routingVersion}")
    RoutingInfoVO queryRoutingByProductCodeAndVersion(@Param("productCode") String productCode, @Param("routingVersion") String routingVersion);

    @Select(value = "SELECT " +
            "  c.id, " +
            "  c.code, " +
            "  c.name, " +
            "  c.description, " +
            "  c.work_time, " +
            "  c.status, " +
            "  c.create_time, " +
            "  c.update_time " +
            "FROM " +
            "  mdm_routing a " +
            "  INNER JOIN mdm_routing_operation_rel b ON a.id = b.routing_id " +
            "  INNER JOIN mdm_operation c ON c.id = b.operation_id " +
            "  INNER JOIN mdm_product d ON a.product_id = d.id " +
            "WHERE " +
            "  a.del_flag = 0 " +
            "  AND b.del_flag = 0 " +
            "  AND c.del_flag = 0 " +
            "  AND d.code = #{productCode} " +
            "  AND a.VERSION = #{routingVersion}")
    List<OperationVO> queryOperationByProductCodeAndVersion(@Param("productCode") String productCode, @Param("routingVersion") String routingVersion);

}
