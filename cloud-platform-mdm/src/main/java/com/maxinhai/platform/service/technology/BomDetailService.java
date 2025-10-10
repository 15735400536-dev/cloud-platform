package com.maxinhai.platform.service.technology;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.technology.BomDetailAddDTO;
import com.maxinhai.platform.dto.technology.BomDetailEditDTO;
import com.maxinhai.platform.dto.technology.BomDetailQueryDTO;
import com.maxinhai.platform.po.technology.BomDetail;
import com.maxinhai.platform.vo.technology.BomDetailVO;

public interface BomDetailService extends IService<BomDetail> {

    Page<BomDetailVO> searchByPage(BomDetailQueryDTO param);

    BomDetailVO getInfo(String id);

    void remove(String[] ids);

    void edit(BomDetailEditDTO param);

    void add(BomDetailAddDTO param);

}
