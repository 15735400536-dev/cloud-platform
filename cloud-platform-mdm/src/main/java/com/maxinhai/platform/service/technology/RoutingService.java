package com.maxinhai.platform.service.technology;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.bo.RoutingExcelBO;
import com.maxinhai.platform.dto.technology.RoutingAddDTO;
import com.maxinhai.platform.dto.technology.RoutingEditDTO;
import com.maxinhai.platform.dto.technology.RoutingQueryDTO;
import com.maxinhai.platform.po.technology.Routing;
import com.maxinhai.platform.vo.technology.RoutingVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface RoutingService extends IService<Routing> {

    Page<RoutingVO> searchByPage(RoutingQueryDTO param);

    RoutingVO getInfo(String id);

    void remove(String[] ids);

    void edit(RoutingEditDTO param);

    void add(RoutingAddDTO param);

    /**
     * 工艺路线绑定工序
     * @param routingId 工艺路线ID
     * @param operationIds 工序ID集合
     */
    void binding(String routingId, List<String> operationIds);

    /**
     * 导入Excel数据
     * @param file 上传的Excel文件
     */
    void importExcel(MultipartFile file);

    /**
     * 保存excel数据
     * @param dataList excel数据
     */
    void saveExcelData(List<RoutingExcelBO> dataList);

    CompletableFuture<RoutingVO> getRoutingByProductCode(String productCode);

}
