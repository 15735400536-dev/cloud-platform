package com.maxinhai.platform.service.order;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.order.IssueOrderDetailAddDTO;
import com.maxinhai.platform.dto.order.IssueOrderDetailEditDTO;
import com.maxinhai.platform.dto.order.IssueOrderDetailQueryDTO;
import com.maxinhai.platform.po.order.IssueOrderDetail;
import com.maxinhai.platform.vo.order.IssueOrderDetailVO;

public interface IssueOrderDetailService extends IService<IssueOrderDetail> {

    Page<IssueOrderDetailVO> searchByPage(IssueOrderDetailQueryDTO param);

    IssueOrderDetailVO getInfo(String id);

    void remove(String[] ids);

    void edit(IssueOrderDetailEditDTO param);

    void add(IssueOrderDetailAddDTO param);

    /**
     * 出库单出库
     * @param id 主键ID
     */
    void issue(String id);

}
