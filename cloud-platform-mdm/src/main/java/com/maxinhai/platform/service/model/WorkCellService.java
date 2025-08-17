package com.maxinhai.platform.service.model;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.model.WorkCellAddDTO;
import com.maxinhai.platform.dto.model.WorkCellEditDTO;
import com.maxinhai.platform.dto.model.WorkCellQueryDTO;
import com.maxinhai.platform.po.model.WorkCell;
import com.maxinhai.platform.vo.model.WorkCellVO;

public interface WorkCellService extends IService<WorkCell> {

    Page<WorkCellVO> searchByPage(WorkCellQueryDTO param);

    WorkCellVO getInfo(String id);

    void remove(String[] ids);

    void edit(WorkCellEditDTO param);

    void add(WorkCellAddDTO param);

}
