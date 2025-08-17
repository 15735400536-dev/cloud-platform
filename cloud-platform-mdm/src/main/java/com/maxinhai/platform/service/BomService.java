package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.technology.BomAddDTO;
import com.maxinhai.platform.dto.technology.BomEditDTO;
import com.maxinhai.platform.dto.technology.BomQueryDTO;
import com.maxinhai.platform.po.technology.Bom;
import com.maxinhai.platform.vo.technology.BomVO;

public interface BomService extends IService<Bom> {

    Page<BomVO> searchByPage(BomQueryDTO param);

    BomVO getInfo(String id);

    void remove(String[] ids);

    void edit(BomEditDTO param);

    void add(BomAddDTO param);

}
