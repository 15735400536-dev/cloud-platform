package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.bo.BomExcelBO;
import com.maxinhai.platform.dto.technology.BomAddDTO;
import com.maxinhai.platform.dto.technology.BomEditDTO;
import com.maxinhai.platform.dto.technology.BomQueryDTO;
import com.maxinhai.platform.po.technology.Bom;
import com.maxinhai.platform.vo.technology.BomVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BomService extends IService<Bom> {

    Page<BomVO> searchByPage(BomQueryDTO param);

    BomVO getInfo(String id);

    void remove(String[] ids);

    void edit(BomEditDTO param);

    void add(BomAddDTO param);

    /**
     * 导入Excel数据
     * @param file 上传的Excel文件
     */
    void importExcel(MultipartFile file);

    /**
     * 保存excel数据
     * @param dataList excel数据
     */
    void saveExcelData(List<BomExcelBO> dataList);

}
