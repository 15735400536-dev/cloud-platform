package com.maxinhai.platform.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import com.maxinhai.platform.bo.ProductBomAndRoutingBO;
import com.maxinhai.platform.po.technology.Bom;
import com.maxinhai.platform.vo.technology.BomDetailVO;
import com.maxinhai.platform.vo.technology.BomInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BomMapper extends MPJBaseMapper<Bom> {

    /**
     * 查询产品配套BOM和工艺路线
     * @return
     */
    @Select(value = "SELECT " +
            "  a.id AS product_id, " +
            "  a.code AS product_code, " +
            "  a.name AS product_name, " +
            "  b.id AS bom_id, " +
            "  b.code AS bom_code, " +
            "  b.name AS bom_name, " +
            "  b.version AS bom_version, " +
            "  c.id AS routing_id, " +
            "  c.code AS routing_code, " +
            "  c.name AS routing_name, " +
            "  c.version AS routing_version " +
            "FROM " +
            "  mdm_product a " +
            "  INNER JOIN mdm_bom b ON a.id = b.product_id " +
            "  INNER JOIN mdm_routing c ON a.id = c.product_id " +
            "WHERE " +
            "  a.del_flag = 0 " +
            "  AND b.del_flag = 0 " +
            "  AND c.del_flag = 0 " +
            "  AND a.code IN ('01-02-01-001', '01-02-02-001', '01-01-01-001', '03-02-01-001', '03-01-01-001', '03-01-02-001', '01-02-02-002', '01-01-02-001') " +
            "  AND b.version = 'v4.0' " +
            "  AND c.version = 'v4.0'")
    List<ProductBomAndRoutingBO> queryProductBomAndRouting();

    @Select(value = "SELECT " +
            "  a.id, " +
            "  a.code, " +
            "  a.name, " +
            "  a.version, " +
            "  a.description, " +
            "  a.product_id, " +
            "  b.code product_code, " +
            "  b.name product_name, " +
            "  a.create_time, " +
            "  a.update_time " +
            "FROM " +
            "  mdm_bom a " +
            "  INNER JOIN mdm_product b ON b.id = a.product_id " +
            "WHERE " +
            "  a.del_flag = 0 " +
            "  AND b.del_flag = 0 " +
            "  AND b.code = #{productCode} " +
            "  AND a.VERSION = #{version}")
    BomInfoVO queryBomInfo(@Param("productCode") String productCode, @Param("version") String version);

    @Select(value = "SELECT " +
            "  a.id bom_id, " +
            "  c.material_id, " +
            "  d.code AS material_code, " +
            "  d.name AS material_name, " +
            "  c.qty, " +
            "  c.parent_id, " +
            "  c.create_time, " +
            "  c.update_time " +
            "FROM " +
            "  mdm_bom a " +
            "  INNER JOIN mdm_product b ON b.id = a.product_id " +
            "  INNER JOIN mdm_bom_detail c ON c.bom_id = a.id " +
            "  INNER JOIN mdm_material d ON d.id = c.material_id " +
            "WHERE " +
            "  a.del_flag = 0 " +
            "  AND b.del_flag = 0 " +
            "  AND c.del_flag = 0 " +
            "  AND d.del_flag = 0 " +
            "  AND b.code = #{productCode} " +
            "  AND a.VERSION = #{version}")
    List<BomDetailVO> queryBomDetail(@Param("productCode") String productCode, @Param("version") String version);

}
