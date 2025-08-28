package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.EquipmentAddDTO;
import com.maxinhai.platform.dto.EquipmentEditDTO;
import com.maxinhai.platform.dto.EquipmentQueryDTO;
import com.maxinhai.platform.po.Equipment;
import com.maxinhai.platform.vo.EquipmentVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface EquipmentService extends IService<Equipment> {

    Page<EquipmentVO> searchByPage(EquipmentQueryDTO param);

    EquipmentVO getInfo(String id);

    void remove(String[] ids);

    void edit(EquipmentEditDTO param);

    void add(EquipmentAddDTO param);

    /**
     * Excel导入设备数据
     * @param file
     */
    void importExcel(MultipartFile file);

    /**
     * 导出excel
     * @param request
     * @param response
     */
    void exportExcel(HttpServletRequest request, HttpServletResponse response);

}
