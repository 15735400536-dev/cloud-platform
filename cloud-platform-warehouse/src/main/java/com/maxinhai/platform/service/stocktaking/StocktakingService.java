package com.maxinhai.platform.service.stocktaking;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.stocktaking.StocktakingAddDTO;
import com.maxinhai.platform.dto.stocktaking.StocktakingEditDTO;
import com.maxinhai.platform.dto.stocktaking.StocktakingQueryDTO;
import com.maxinhai.platform.po.stocktaking.Stocktaking;
import com.maxinhai.platform.vo.stocktaking.StocktakingVO;

public interface StocktakingService extends IService<Stocktaking> {

    Page<StocktakingVO> searchByPage(StocktakingQueryDTO param);

    StocktakingVO getInfo(String id);

    void remove(String[] ids);

    void edit(StocktakingEditDTO param);

    void add(StocktakingAddDTO param);

}
