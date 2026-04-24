package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.InfoPublishDTO;
import com.maxinhai.platform.dto.InfoPublishQueryDTO;
import com.maxinhai.platform.po.info.InfoPublish;
import com.maxinhai.platform.vo.InfoPublishVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface InfoPublishService extends IService<InfoPublish> {

    /**
     * 分页查询发布信息
     * @param param 参数
     * @return 发布信息
     */
    Page<InfoPublishVO> searchPage(InfoPublishQueryDTO param);

    /**
     * 获取发布信息详情
     * @param id 信息ID
     * @return 发布信息
     */
    InfoPublishVO getInfo(String id);

    /**
     * 删除发布信息
     * @param ids 信息ID集合
     */
    void remove(String[] ids);

    /**
     * 编辑发布信息
     * @param param 参数
     */
    void edit(InfoPublishDTO param);

    /**
     * 新增发布信息
     * @param param 参数
     */
    void add(InfoPublishDTO param);

    /**
     * 上传文件
     * @param fileList 文件集合
     * @return 上传文件信息
     */
    List<Map<String, Object>> uploadFile(List<MultipartFile> fileList);

    /**
     * 发布信息
     * @param id 信息ID
     */
    void publish(String id);

    /**
     * 撤销信息
     * @param id 信息ID
     */
    void cancel(String id);

}
