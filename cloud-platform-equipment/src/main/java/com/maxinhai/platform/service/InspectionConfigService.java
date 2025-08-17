package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.InspectionConfigAddDTO;
import com.maxinhai.platform.dto.InspectionConfigEditDTO;
import com.maxinhai.platform.dto.InspectionConfigQueryDTO;
import com.maxinhai.platform.po.InspectionConfig;
import com.maxinhai.platform.vo.InspectionConfigVO;

public interface InspectionConfigService extends IService<InspectionConfig> {

    Page<InspectionConfigVO> searchByPage(InspectionConfigQueryDTO param);

    InspectionConfigVO getInfo(String id);

    void remove(String[] ids);

    void edit(InspectionConfigEditDTO param);

    void add(InspectionConfigAddDTO param);

}
