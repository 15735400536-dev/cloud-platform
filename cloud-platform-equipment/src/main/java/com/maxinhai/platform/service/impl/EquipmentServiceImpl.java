package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.google.common.collect.Lists;
import com.maxinhai.platform.dto.EquipmentAddDTO;
import com.maxinhai.platform.dto.EquipmentEditDTO;
import com.maxinhai.platform.dto.EquipmentQueryDTO;
import com.maxinhai.platform.enums.EquipStatus;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.feign.SystemFeignClient;
import com.maxinhai.platform.mapper.EquipmentMapper;
import com.maxinhai.platform.mapper.InspectionConfigMapper;
import com.maxinhai.platform.mapper.MaintenanceConfigMapper;
import com.maxinhai.platform.po.*;
import com.maxinhai.platform.service.CommonCodeCheckService;
import com.maxinhai.platform.service.EquipmentService;
import com.maxinhai.platform.service.InspectionItemService;
import com.maxinhai.platform.service.MaintenanceItemService;
import com.maxinhai.platform.vo.EquipmentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EquipmentServiceImpl extends ServiceImpl<EquipmentMapper, Equipment> implements EquipmentService {

    @Resource
    private EquipmentMapper equipmentMapper;
    @Resource
    private SystemFeignClient systemFeignClient;
    @Resource
    private CommonCodeCheckService commonCodeCheckService;

    @Override
    public Page<EquipmentVO> searchByPage(EquipmentQueryDTO param) {
        Page<EquipmentVO> pageResult = equipmentMapper.selectJoinPage(param.getPage(), EquipmentVO.class,
                new MPJLambdaWrapper<Equipment>()
                        .like(StrUtil.isNotBlank(param.getEquipCode()), Equipment::getEquipCode, param.getEquipCode())
                        .like(StrUtil.isNotBlank(param.getEquipName()), Equipment::getEquipName, param.getEquipName())
                        .eq(StrUtil.isNotBlank(param.getEquipType()), Equipment::getEquipType, param.getEquipType())
                        .orderByDesc(Equipment::getCreateTime));
        return pageResult;
    }

    @Override
    public EquipmentVO getInfo(String id) {
        return equipmentMapper.selectJoinOne(EquipmentVO.class,
                new MPJLambdaWrapper<Equipment>()
                        .eq(StrUtil.isNotBlank(id), Equipment::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        equipmentMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(EquipmentEditDTO param) {
        Equipment equipment = BeanUtil.toBean(param, Equipment.class);
        equipmentMapper.updateById(equipment);
    }

    @Override
    public void add(EquipmentAddDTO param) {
        boolean unique = commonCodeCheckService.isCodeUnique(Equipment.class, Equipment::getEquipCode, param.getEquipCode());
        if (!unique) {
            throw new BusinessException("设备【" + param.getEquipCode() + "】已存在！");
        }
        Equipment equipment = BeanUtil.toBean(param, Equipment.class);
        equipmentMapper.insert(equipment);
    }

    @Override
    public void importExcel(MultipartFile file) {

    }

    @Override
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) {

    }

    @Resource
    private ResourceLoader resourceLoader;
    @Resource
    private MaintenanceConfigMapper maintenanceConfigMapper;
    @Resource
    private MaintenanceItemService maintenanceItemService;
    @Resource
    private InspectionConfigMapper inspectionConfigMapper;
    @Resource
    private InspectionItemService inspectionItemService;

    //    @PostConstruct
    public void initData() throws IOException {
        Map<String, List<String>> equipMap = new LinkedHashMap<>();
        equipMap.put("核心动力设备", Lists.newArrayList("空压机", "真空泵", "发电机"));
        equipMap.put("传动与输送设备", Lists.newArrayList("减速器", "输送带", "链条传动"));
        equipMap.put("泵类设备", Lists.newArrayList("离心泵", "齿轮泵", "隔膜泵"));
        equipMap.put("办公与小型通用设备", Lists.newArrayList("打印机", "空调", "风机"));

        // 统计所有List元素个数
        long totalCount = equipMap.values().stream().mapToInt(List::size).sum();
        List<String> equipCodeList = systemFeignClient.generateCode("equip_code", Integer.parseInt(String.valueOf(totalCount))).getData();

        int index = 0;
        for (Map.Entry<String, List<String>> entry : equipMap.entrySet()) {
            String equipType = entry.getKey();
            List<String> equipNameList = entry.getValue();
            for (String equipName : equipNameList) {
                Equipment equipment = new Equipment();
                equipment.setEquipCode(equipCodeList.get(index));
                equipment.setEquipName(equipName);
                equipment.setEquipType(equipType);
                equipment.setStatus(EquipStatus.RUN);
                equipmentMapper.insert(equipment);
                index++;
            }
        }
    }

    public void initConfigItem(Set<String> equipTypeSet) throws IOException {
        List<Equipment> equipmentList = equipmentMapper.selectList(new LambdaQueryWrapper<Equipment>()
                .in(Equipment::getEquipType, equipTypeSet));
        Map<String, List<Equipment>> equipMap = equipmentList.stream()
                .collect(Collectors.groupingBy(Equipment::getEquipType));

        // 加载resources目录下的check_item.json文件，读取文件内容为字符串
        byte[] content = FileCopyUtils.copyToByteArray(resourceLoader
                .getResource("classpath:check_item.json")
                .getInputStream());
        String jsonString = new String(content, StandardCharsets.UTF_8);
        JSONObject jsonObject = JSON.parseObject(jsonString);

        initMainConfigItem(equipMap, jsonObject);
        initInspectConfigItem(equipMap, jsonObject);
    }

    public void initMainConfigItem(Map<String, List<Equipment>> equipMap, JSONObject jsonObject) {
        for (String equipType : equipMap.keySet()) {
            List<Equipment> equipList = equipMap.get(equipType);
            for (Equipment equip : equipList) {
                // 生成保养配置编码
                List<String> maintenanceConfigCodeList = systemFeignClient.generateCode("maintenance_config_code", 1).getData();

                MaintenanceConfig maintenanceConfig = new MaintenanceConfig();
                maintenanceConfig.setConfigCode(maintenanceConfigCodeList.get(0));
                maintenanceConfig.setConfigName(equip.getEquipCode());
                maintenanceConfig.setEquipId(equip.getId());
                maintenanceConfig.setMaintenanceType("定期保养");
                maintenanceConfig.setMaintenanceLevel(1);
                maintenanceConfig.setCycleType(1);
                maintenanceConfig.setCycleInterval(30);
                maintenanceConfig.setStatus(1);
                maintenanceConfigMapper.insert(maintenanceConfig);

                List<MaintenanceItem> itemList = new ArrayList<>();
                JSONArray jsonArray = jsonObject.getJSONArray(equipType);
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject item = (JSONObject) jsonArray.get(i);
                    String itemName = item.getString("保养项目");
                    String 每日 = item.getString("每日");
                    JSONArray standards = item.getJSONArray("要求");

                    // 生成保养配置编码
                    List<String> maintenanceItemCodeList = systemFeignClient.generateCode("maintenance_item_code", 1).getData();
                    MaintenanceItem maintenanceItem = new MaintenanceItem();
                    maintenanceItem.setConfigId(maintenanceConfig.getId());
                    maintenanceItem.setItemCode(maintenanceItemCodeList.get(0));
                    maintenanceItem.setItemName(itemName);
                    maintenanceItem.setStandard(build(standards));
                    maintenanceItem.setSort(i + 1);
                    itemList.add(maintenanceItem);
                }
                maintenanceItemService.saveBatch(itemList);
            }
        }
    }

    public void initInspectConfigItem(Map<String, List<Equipment>> equipMap, JSONObject jsonObject) {
        for (String equipType : equipMap.keySet()) {
            List<Equipment> equipList = equipMap.get(equipType);
            for (Equipment equip : equipList) {
                // 生成点检配置编码
                List<String> maintenanceConfigCodeList = systemFeignClient.generateCode("inspection_config_code", 1).getData();

                InspectionConfig inspectionConfig = new InspectionConfig();
                inspectionConfig.setConfigCode(maintenanceConfigCodeList.get(0));
                inspectionConfig.setConfigName(equip.getEquipCode());
                inspectionConfig.setEquipId(equip.getId());
                inspectionConfig.setCycleType(1);
                inspectionConfig.setCycleInterval(30);
                inspectionConfig.setStatus(1);
                inspectionConfigMapper.insert(inspectionConfig);

                List<InspectionItem> itemList = new ArrayList<>();
                JSONArray jsonArray = jsonObject.getJSONArray(equipType);
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject item = (JSONObject) jsonArray.get(i);
                    String itemName = item.getString("保养项目");
                    String 每日 = item.getString("每日");
                    JSONArray standards = item.getJSONArray("要求");

                    // 生成保养配置编码
                    List<String> inspectionItemCodeList = systemFeignClient.generateCode("inspection_item_code", 1).getData();
                    InspectionItem inspectionItem = new InspectionItem();
                    inspectionItem.setConfigId(inspectionConfig.getId());
                    inspectionItem.setItemCode(inspectionItemCodeList.get(0));
                    inspectionItem.setItemName(itemName);
                    inspectionItem.setStandard(build(standards));
                    inspectionItem.setSort(i + 1);
                    itemList.add(inspectionItem);
                }
                inspectionItemService.saveBatch(itemList);
            }
        }
    }

    public String build(JSONArray jsonArray) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < jsonArray.size(); i++) {
            String item = jsonArray.get(i).toString();
            buffer.append(item);
        }
        return buffer.toString();
    }
}
