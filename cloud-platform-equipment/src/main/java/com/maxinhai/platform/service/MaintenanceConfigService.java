package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.MaintenanceConfigAddDTO;
import com.maxinhai.platform.dto.MaintenanceConfigEditDTO;
import com.maxinhai.platform.dto.MaintenanceConfigQueryDTO;
import com.maxinhai.platform.po.MaintenanceConfig;
import com.maxinhai.platform.vo.MaintenanceConfigVO;

public interface MaintenanceConfigService extends IService<MaintenanceConfig> {

    Page<MaintenanceConfigVO> searchByPage(MaintenanceConfigQueryDTO param);

    MaintenanceConfigVO getInfo(String id);

    void remove(String[] ids);

    void edit(MaintenanceConfigEditDTO param);

    void add(MaintenanceConfigAddDTO param);

}
