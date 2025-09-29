package com.maxinhai.platform.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.UploadFileQueryDTO;
import com.maxinhai.platform.mapper.UploadFileMapper;
import com.maxinhai.platform.po.UploadFile;
import com.maxinhai.platform.service.UploadFileService;
import com.maxinhai.platform.vo.UploadFileVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName：FileServiceImpl
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 14:13
 * @Description: 上传文件业务层
 */
@Slf4j
@Service
public class UploadFileServiceImpl extends ServiceImpl<UploadFileMapper, UploadFile>
        implements UploadFileService {

    @Resource
    private UploadFileMapper uploadFileMapper;

    @Override
    public Page<UploadFileVO> searchByPage(UploadFileQueryDTO param) {
        return uploadFileMapper.selectJoinPage(param.getPage(), UploadFileVO.class,
                new MPJLambdaWrapper<UploadFile>()
                        .like(StrUtil.isNotBlank(param.getFileName()), UploadFile::getFileName, param.getFileName())
                        .like(StrUtil.isNotBlank(param.getFileType()), UploadFile::getFileType, param.getFileType())
                        .orderByDesc(UploadFile::getCreateTime));
    }

    @Override
    public void delFile(String id) {
        // 1. 查询文件记录
        UploadFile uploadFile = uploadFileMapper.selectOne(new LambdaQueryWrapper<UploadFile>()
                .select(UploadFile::getId, UploadFile::getFilePath)
                .eq(UploadFile::getId, id));
        // 2.删除文件
        FileUtil.del(uploadFile.getFilePath());
        // 3.删除记录
        uploadFileMapper.deleteById(id);
    }

}
