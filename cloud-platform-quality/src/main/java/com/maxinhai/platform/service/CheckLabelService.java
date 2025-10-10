package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.CheckLabelQueryDTO;
import com.maxinhai.platform.enums.CheckType;
import com.maxinhai.platform.po.CheckLabel;
import com.maxinhai.platform.po.CheckOrder;
import com.maxinhai.platform.vo.CheckLabelVO;

import java.util.List;
import java.util.Map;

public interface CheckLabelService extends IService<CheckLabel> {

    Page<CheckLabelVO> searchByPage(CheckLabelQueryDTO param);

    CheckLabelVO getInfo(String id);

    /**
     * 根据产品ID和检测类型生成电子履历标签
     * @param productId
     * @param checkType
     */
    void generateLabel(String productId, CheckType checkType);

    /**
     * 根据质检单ID获取电子履历标签以及对应数值
     * @param checkOrderId
     * @return
     */
    Map<String, String> getLabelValueMap(String checkOrderId);

    /**
     * 根据产品ID和检测类型查询电子履历标签
     * @param productId
     * @param checkType
     * @return
     */
    List<CheckLabel> getCheckLabelList(String productId, CheckType checkType);

    /**
     * 根据工单ID生成电子履历
     * @param workOrderId
     */
    byte[] generate(String workOrderId);

}
