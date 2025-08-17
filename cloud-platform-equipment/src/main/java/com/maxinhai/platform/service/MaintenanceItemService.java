package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.MaintenanceItemAddDTO;
import com.maxinhai.platform.dto.MaintenanceItemEditDTO;
import com.maxinhai.platform.dto.MaintenanceItemQueryDTO;
import com.maxinhai.platform.po.MaintenanceItem;
import com.maxinhai.platform.vo.MaintenanceItemVO;

public interface MaintenanceItemService extends IService<MaintenanceItem> {

    Page<MaintenanceItemVO> searchByPage(MaintenanceItemQueryDTO param);

    MaintenanceItemVO getInfo(String id);

    void remove(String[] ids);

    void edit(MaintenanceItemEditDTO param);

    void add(MaintenanceItemAddDTO param);

}
