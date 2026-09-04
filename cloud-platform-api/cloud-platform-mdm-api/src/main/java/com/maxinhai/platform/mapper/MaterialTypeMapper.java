package com.maxinhai.platform.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import com.maxinhai.platform.po.MaterialType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MaterialTypeMapper extends MPJBaseMapper<MaterialType> {

    /**
     * 批量新增物料分类
     *
     * @param itemList 物料类型集合
     * @return 受影响行数
     */
    @Insert("<script>" +
            "INSERT INTO mdm_material_type (id,code,name,description,parent_id,create_by,create_time,update_by,update_time,del_flag) VALUES " +
            "<foreach collection='itemList' item='item' separator=','>" +
            "(#{item.id},#{item.code},#{item.name},#{item.description},#{item.parentId},#{item.createBy},#{item.createTime},#{item.updateBy},#{item.updateTime},#{item.delFlag})" +
            "</foreach>" +
            "</script>")
    int batchInsert(@Param("itemList") List<MaterialType> itemList);

}
