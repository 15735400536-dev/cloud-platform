package com.maxinhai.platform.service.technology;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.technology.BomDetailAddDTO;
import com.maxinhai.platform.dto.technology.BomDetailEditDTO;
import com.maxinhai.platform.dto.technology.BomDetailQueryDTO;
import com.maxinhai.platform.po.technology.BomDetail;
import com.maxinhai.platform.vo.technology.BomDetailVO;

import java.util.List;

public interface BomDetailService extends IService<BomDetail> {

    Page<BomDetailVO> searchByPage(BomDetailQueryDTO param);

    BomDetailVO getInfo(String id);

    void remove(String[] ids);

    void edit(BomDetailEditDTO param);

    void add(BomDetailAddDTO param);

    /**
     * 根据产品编码和版本号查询BOM明细
     * @param productCode 产品编码
     * @param bomVersion 版本号
     * @return
     */
    List<BomDetailVO> queryBomDetail(String productCode, String bomVersion);

}
