package com.maxinhai.platform.service.order;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.order.IssueOrderAddDTO;
import com.maxinhai.platform.dto.order.IssueOrderEditDTO;
import com.maxinhai.platform.dto.order.IssueOrderQueryDTO;
import com.maxinhai.platform.po.order.IssueOrder;
import com.maxinhai.platform.vo.order.IssueOrderVO;

public interface IssueOrderService extends IService<IssueOrder> {

    Page<IssueOrderVO> searchByPage(IssueOrderQueryDTO param);

    IssueOrderVO getInfo(String id);

    void remove(String[] ids);

    void edit(IssueOrderEditDTO param);

    void add(IssueOrderAddDTO param);

    /**
     * 根据出库单ID入库
     * @param id 主键ID
     */
    void issue(String id);

}
