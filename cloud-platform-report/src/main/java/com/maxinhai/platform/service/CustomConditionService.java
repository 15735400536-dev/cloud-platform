package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.CustomConditionAddDTO;
import com.maxinhai.platform.dto.CustomConditionEditDTO;
import com.maxinhai.platform.dto.CustomConditionQueryDTO;
import com.maxinhai.platform.po.CustomCondition;
import com.maxinhai.platform.vo.CustomConditionVO;

public interface CustomConditionService extends IService<CustomCondition> {

    Page<CustomConditionVO> searchByPage(CustomConditionQueryDTO param);

    CustomConditionVO getInfo(String id);

    void remove(String[] ids);

    void edit(CustomConditionEditDTO param);

    void add(CustomConditionAddDTO param);

}
