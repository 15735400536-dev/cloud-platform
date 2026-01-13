package com.maxinhai.platform.service.impl;

import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.maxinhai.platform.enums.AlarmConditionEnum;
import com.maxinhai.platform.po.*;
import com.maxinhai.platform.service.AlarmService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @ClassName：AlarmServiceImpl
 * @Author: XinHai.Ma
 * @Date: 2026/1/13 23:07
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
public class AlarmServiceImpl implements AlarmService {

    private static final List<Algorithm> ALGORITHMS = Lists.newArrayList(
            new Algorithm(UUID.randomUUID().toString().replaceAll("-", ""), "指针仪表", "1"),
            new Algorithm(UUID.randomUUID().toString().replaceAll("-", ""), "数字仪表", "2"),
            new Algorithm(UUID.randomUUID().toString().replaceAll("-", ""), "状态检测", "3")
    );
    private static final List<InspectPoint> INSPECT_POINTS = Lists.newArrayList(
            new InspectPoint(UUID.randomUUID().toString().replaceAll("-", ""), "巡检点1", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO),
            new InspectPoint(UUID.randomUUID().toString().replaceAll("-", ""), "巡检点2", BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE),
            new InspectPoint(UUID.randomUUID().toString().replaceAll("-", ""), "巡检点3", BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN)
    );
    private static final List<PointerMeterConfig> POINTER_METER_CONFIGS = Lists.newArrayList(
            new PointerMeterConfig(UUID.randomUUID().toString().replaceAll("-", ""), "1", "母线电压", "0", "1000", 5, "V"),
            new PointerMeterConfig(UUID.randomUUID().toString().replaceAll("-", ""), "1", "电机电压", "0", "1000", 5, "V"),
            new PointerMeterConfig(UUID.randomUUID().toString().replaceAll("-", ""), "2", "母线频率", "45", "50", 5, "Hz"),
            new PointerMeterConfig(UUID.randomUUID().toString().replaceAll("-", ""), "2", "电机频率", "45", "50", 5, "Hz")
    );
    private static final List<DigitMeterConfig> DIGIT_METER_CONFIGS = Lists.newArrayList(
            new DigitMeterConfig(UUID.randomUUID().toString().replaceAll("-", ""), "1", "0", "1000", 5, "V"),
            new DigitMeterConfig(UUID.randomUUID().toString().replaceAll("-", ""), "2", "45", "50", 5, "Hz")
    );

    public static void main(String[] args) {
        List<InspectPointConfig> inspectPointConfigs = new ArrayList<>();
        for (int i = 0; i < INSPECT_POINTS.size(); i++) {
            InspectPoint point = INSPECT_POINTS.get(i);
            Algorithm algorithm = ALGORITHMS.get(i);

            switch (algorithm.getName()) {
                case "指针仪表":
                    List<PointerMeterConfig> pointerMeterConfigList = POINTER_METER_CONFIGS.stream().filter(config -> "1".equals(config.getMeterType())).collect(Collectors.toList());
                    for (PointerMeterConfig pointerMeterConfig : pointerMeterConfigList) {
                        InspectPointConfig pointConfig = new InspectPointConfig();
                        pointConfig.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                        pointConfig.setPointId(point.getId());
                        pointConfig.setAlgorithmId(algorithm.getId());
                        pointConfig.setConfigId(pointerMeterConfig.getId());
                        pointConfig.setCondition(AlarmConditionEnum.IN_RANGE);
                        pointConfig.setMinValue(BigDecimal.ZERO);
                        pointConfig.setMaxValue(BigDecimal.TEN);
                        pointConfig.setStandardValue(BigDecimal.ZERO);
                        inspectPointConfigs.add(pointConfig);
                    }
                    break;
                case "数字仪表":
                    List<DigitMeterConfig> digitMeterConfigList = DIGIT_METER_CONFIGS.stream().filter(config -> "1".equals(config.getMeterType())).collect(Collectors.toList());
                    for (DigitMeterConfig digitMeterConfig : digitMeterConfigList) {
                        InspectPointConfig pointConfig = new InspectPointConfig();
                        pointConfig.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                        pointConfig.setPointId(point.getId());
                        pointConfig.setAlgorithmId(algorithm.getId());
                        pointConfig.setConfigId(digitMeterConfig.getId());
                        pointConfig.setCondition(AlarmConditionEnum.IN_RANGE);
                        pointConfig.setMinValue(BigDecimal.ZERO);
                        pointConfig.setMaxValue(BigDecimal.TEN);
                        pointConfig.setStandardValue(BigDecimal.ZERO);
                        inspectPointConfigs.add(pointConfig);
                    }
                    break;
                case "状态检测":
                    for (int j = 0; j < 10; j++) {
                        InspectPointConfig pointConfig = new InspectPointConfig();
                        pointConfig.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                        pointConfig.setPointId(point.getId());
                        pointConfig.setAlgorithmId(algorithm.getId());
                        pointConfig.setConfigId(null);
                        pointConfig.setCondition(AlarmConditionEnum.EQ);
                        pointConfig.setMinValue(BigDecimal.ZERO);
                        pointConfig.setMaxValue(BigDecimal.TEN);
                        pointConfig.setStandardValue(BigDecimal.ZERO);
                        inspectPointConfigs.add(pointConfig);
                    }
                    break;
            }
        }
        System.out.println("共" + inspectPointConfigs.size() + "条数据");
        for (InspectPointConfig inspectPointConfig : inspectPointConfigs) {
            System.out.println(inspectPointConfig);
        }
    }

}
