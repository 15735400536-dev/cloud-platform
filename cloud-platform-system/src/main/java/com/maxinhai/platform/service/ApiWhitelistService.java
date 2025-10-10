package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.ApiWhitelistAddDTO;
import com.maxinhai.platform.dto.ApiWhitelistEditDTO;
import com.maxinhai.platform.dto.ApiWhitelistQueryDTO;
import com.maxinhai.platform.po.ApiWhitelist;
import com.maxinhai.platform.vo.ApiWhitelistVO;

public interface ApiWhitelistService extends IService<ApiWhitelist> {

    Page<ApiWhitelistVO> searchByPage(ApiWhitelistQueryDTO param);

    ApiWhitelistVO getInfo(String id);

    void remove(String[] ids);

    void edit(ApiWhitelistEditDTO param);

    void add(ApiWhitelistAddDTO param);

}
