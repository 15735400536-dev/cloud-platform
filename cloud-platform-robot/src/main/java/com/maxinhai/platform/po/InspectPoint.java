package com.maxinhai.platform.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @ClassName：InspectPoint
 * @Author: XinHai.Ma
 * @Date: 2026/1/13 22:36
 * @Description: 巡检点位
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InspectPoint {

    /**
     * 主键ID
     */
    private String id = UUID.randomUUID().toString().replaceAll("-", "");
    /**
     * 点位名称
     */
    private String name;
    /**
     * 机器人位置
     */
    private BigDecimal robotPos;
    /**
     * 升降杆位置
     */
    private BigDecimal liftPos;
    /**
     * 云台位置
     */
    private BigDecimal ptzPos;


}
