package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.MaterialAddDTO;
import com.maxinhai.platform.dto.MaterialEditDTO;
import com.maxinhai.platform.dto.MaterialQueryDTO;
import com.maxinhai.platform.po.Material;
import com.maxinhai.platform.vo.MaterialVO;
import org.springframework.web.multipart.MultipartFile;

public interface MaterialService extends IService<Material> {

    Page<MaterialVO> searchByPage(MaterialQueryDTO param);

    MaterialVO getInfo(String id);

    void remove(String[] ids);

    void edit(MaterialEditDTO param);

    void add(MaterialAddDTO param);

    /**
     * 导入Excel数据
     * @param file 上传的Excel文件
     * @return 导入结果
     */
    void importExcel(MultipartFile file);

}
