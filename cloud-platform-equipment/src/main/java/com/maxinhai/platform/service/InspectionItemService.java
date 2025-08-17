package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.InspectionItemAddDTO;
import com.maxinhai.platform.dto.InspectionItemEditDTO;
import com.maxinhai.platform.dto.InspectionItemQueryDTO;
import com.maxinhai.platform.po.InspectionItem;
import com.maxinhai.platform.vo.InspectionItemVO;

public interface InspectionItemService extends IService<InspectionItem> {

    Page<InspectionItemVO> searchByPage(InspectionItemQueryDTO param);

    InspectionItemVO getInfo(String id);

    void remove(String[] ids);

    void edit(InspectionItemEditDTO param);

    void add(InspectionItemAddDTO param);

}
