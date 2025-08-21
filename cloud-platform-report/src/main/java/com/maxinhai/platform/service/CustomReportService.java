package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.CustomReportAddDTO;
import com.maxinhai.platform.dto.CustomReportEditDTO;
import com.maxinhai.platform.dto.CustomReportQueryDTO;
import com.maxinhai.platform.po.CustomReport;
import com.maxinhai.platform.vo.CustomReportVO;

public interface CustomReportService extends IService<CustomReport> {

    Page<CustomReportVO> searchByPage(CustomReportQueryDTO param);

    CustomReportVO getInfo(String id);

    void remove(String[] ids);

    void edit(CustomReportEditDTO param);

    void add(CustomReportAddDTO param);

}
