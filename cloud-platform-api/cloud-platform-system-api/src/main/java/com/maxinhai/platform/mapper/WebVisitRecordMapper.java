package com.maxinhai.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.maxinhai.platform.dto.UserVisitLogQueryDTO;
import com.maxinhai.platform.po.WebVisitRecord;
import com.maxinhai.platform.vo.UserVisitDurationStatVO;
import com.maxinhai.platform.vo.WebVisitRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WebVisitRecordMapper extends BaseMapper<WebVisitRecord> {

    /**
     * 批量插入网页访问记录
     * @param list 数据集合
     * @return 影响行数
     */
    int insertBatch(@Param("list") List<WebVisitRecord> list);

    /**
     * 根据条件查询记录
     * @return 记录
     */
    List<WebVisitRecordVO> selectRecordList(@Param("query") UserVisitLogQueryDTO query);

    /**
     * 根据条件，按用户id、访问日期、域名分组，统计用户各网站当日浏览总时长
     * @param query 查询条件（可传clientId、visitDate等筛选）
     * @return 统计结果VO
     */
    List<UserVisitDurationStatVO> selectUserDailyHostDurationStat(@Param("query") UserVisitLogQueryDTO query);

    /**
     * 创建自定义函数
     *
     * CREATE OR REPLACE FUNCTION get_url_host(p_url text)
     * RETURNS text AS $$
     * BEGIN
     *   IF p_url IS NULL OR trim(p_url) = '' THEN
     *     RETURN NULL;
     *   END IF;
     *   -- 移除 http:// https:// 前缀，再取第一个/前面内容
     *   RETURN split_part(regexp_replace(p_url, '^https?://', '', 'i'), '/', 1);
     * END;
     * $$ LANGUAGE plpgsql IMMUTABLE;
     *
     * CREATE OR REPLACE FUNCTION get_url_host_v1(p_url text)
     * RETURNS text AS $$
     * DECLARE
     *   v_temp text;
     * BEGIN
     *   IF p_url IS NULL OR trim(p_url) = '' THEN
     *     RETURN NULL;
     *   END IF;
     *   -- 1. 去掉 http/https 协议头
     *   v_temp := regexp_replace(p_url, '^https?://', '', 'i');
     *   -- 2. 截取第一个 / 之前部分 host:port
     *   v_temp := split_part(v_temp, '/', 1);
     *   -- 3. 去掉端口：截取冒号前面真正域名
     *   RETURN split_part(v_temp, ':', 1);
     * END;
     * $$ LANGUAGE plpgsql IMMUTABLE;
     */

}
