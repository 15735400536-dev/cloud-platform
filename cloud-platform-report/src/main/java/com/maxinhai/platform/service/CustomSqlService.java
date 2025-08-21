package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.CustomSqlAddDTO;
import com.maxinhai.platform.dto.CustomSqlEditDTO;
import com.maxinhai.platform.dto.CustomSqlQueryDTO;
import com.maxinhai.platform.po.CustomSql;
import com.maxinhai.platform.vo.CustomSqlVO;

public interface CustomSqlService extends IService<CustomSql> {

    Page<CustomSqlVO> searchByPage(CustomSqlQueryDTO param);

    CustomSqlVO getInfo(String id);

    void remove(String[] ids);

    void edit(CustomSqlEditDTO param);

    void add(CustomSqlAddDTO param);

}
