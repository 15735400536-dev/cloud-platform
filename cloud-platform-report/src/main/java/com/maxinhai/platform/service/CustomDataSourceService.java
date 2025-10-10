package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.CustomDataSourceAddDTO;
import com.maxinhai.platform.dto.CustomDataSourceEditDTO;
import com.maxinhai.platform.dto.CustomDataSourceQueryDTO;
import com.maxinhai.platform.po.CustomDataSource;
import com.maxinhai.platform.vo.CustomDataSourceVO;

public interface CustomDataSourceService extends IService<CustomDataSource> {

    Page<CustomDataSourceVO> searchByPage(CustomDataSourceQueryDTO param);

    CustomDataSourceVO getInfo(String id);

    void remove(String[] ids);

    void edit(CustomDataSourceEditDTO param);

    void add(CustomDataSourceAddDTO param);

}
