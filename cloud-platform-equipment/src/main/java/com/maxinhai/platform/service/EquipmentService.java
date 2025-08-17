package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.EquipmentAddDTO;
import com.maxinhai.platform.dto.EquipmentEditDTO;
import com.maxinhai.platform.dto.EquipmentQueryDTO;
import com.maxinhai.platform.po.Equipment;
import com.maxinhai.platform.vo.EquipmentVO;

public interface EquipmentService extends IService<Equipment> {

    Page<EquipmentVO> searchByPage(EquipmentQueryDTO param);

    EquipmentVO getInfo(String id);

    void remove(String[] ids);

    void edit(EquipmentEditDTO param);

    void add(EquipmentAddDTO param);

}
