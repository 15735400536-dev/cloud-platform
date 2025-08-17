package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.technology.RoutingAddDTO;
import com.maxinhai.platform.dto.technology.RoutingEditDTO;
import com.maxinhai.platform.dto.technology.RoutingQueryDTO;
import com.maxinhai.platform.po.technology.Routing;
import com.maxinhai.platform.vo.technology.RoutingVO;

import java.util.List;

public interface RoutingService extends IService<Routing> {

    Page<RoutingVO> searchByPage(RoutingQueryDTO param);

    RoutingVO getInfo(String id);

    void remove(String[] ids);

    void edit(RoutingEditDTO param);

    void add(RoutingAddDTO param);

    /**
     * 工艺路线绑定工序
     * @param routingId
     * @param operationIds
     */
    void binding(String routingId, List<String> operationIds);

}
