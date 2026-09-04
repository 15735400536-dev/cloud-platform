package com.maxinhai.platform.service.technology;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.technology.OperationAddDTO;
import com.maxinhai.platform.dto.technology.OperationEditDTO;
import com.maxinhai.platform.dto.technology.OperationQueryDTO;
import com.maxinhai.platform.po.technology.Operation;
import com.maxinhai.platform.vo.technology.OperationVO;
import org.springframework.web.multipart.MultipartFile;

public interface OperationService extends IService<Operation> {

    Page<OperationVO> searchByPage(OperationQueryDTO param);

    OperationVO getInfo(String id);

    void remove(String[] ids);

    void edit(OperationEditDTO param);

    void add(OperationAddDTO param);

    /**
     * 导入Excel数据
     * @param file 上传的Excel文件
     */
    void importExcel(MultipartFile file);

}
