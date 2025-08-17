package com.maxinhai.platform.service.stocktaking;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.stocktaking.StocktakingDetailAddDTO;
import com.maxinhai.platform.dto.stocktaking.StocktakingDetailEditDTO;
import com.maxinhai.platform.dto.stocktaking.StocktakingDetailQueryDTO;
import com.maxinhai.platform.po.stocktaking.StocktakingDetail;
import com.maxinhai.platform.vo.stocktaking.StocktakingDetailVO;

public interface StocktakingDetailService extends IService<StocktakingDetail> {

    Page<StocktakingDetailVO> searchByPage(StocktakingDetailQueryDTO param);

    StocktakingDetailVO getInfo(String id);

    void remove(String[] ids);

    void edit(StocktakingDetailEditDTO param);

    void add(StocktakingDetailAddDTO param);

}
