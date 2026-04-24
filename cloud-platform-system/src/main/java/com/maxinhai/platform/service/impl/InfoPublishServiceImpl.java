package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.InfoPublishDTO;
import com.maxinhai.platform.dto.InfoPublishQueryDTO;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.mapper.info.InfoPublishMapper;
import com.maxinhai.platform.mapper.info.InfoPublishRecordMapper;
import com.maxinhai.platform.po.FileStorage;
import com.maxinhai.platform.po.info.InfoPublish;
import com.maxinhai.platform.po.info.InfoPublishRecord;
import com.maxinhai.platform.service.FileStorageService;
import com.maxinhai.platform.service.InfoPublishService;
import com.maxinhai.platform.utils.JwtUtils;
import com.maxinhai.platform.vo.InfoPublishVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InfoPublishServiceImpl extends ServiceImpl<InfoPublishMapper, InfoPublish> implements InfoPublishService {

    private final InfoPublishMapper infoPublishMapper;
    private final InfoPublishRecordMapper infoPublishRecordMapper;
    private final JwtUtils jwtUtils;
    private final FileStorageService fileStorageService;
    private static final int PUBLISHED = 1;
    private static final int UN_PUBLISHED = 0;

    @Override
    public Page<InfoPublishVO> searchPage(InfoPublishQueryDTO param) {
        return infoPublishMapper.selectJoinPage(param.getPage(), InfoPublishVO.class,
                new MPJLambdaWrapper<InfoPublish>()
                        .select(InfoPublish::getId, InfoPublish::getCreateBy, InfoPublish::getCreateTime, InfoPublish::getUpdateBy, InfoPublish::getUpdateTime,
                                InfoPublish::getTitle, InfoPublish::getAuthor, InfoPublish::getPublishStatus,  InfoPublish::getPublishTime, InfoPublish::getPublisher)
                        .like(StrUtil.isNotBlank(param.getTitle()), InfoPublish::getTitle, param.getTitle())
                        .orderByDesc(InfoPublish::getCreateTime));
    }

    @Override
    public InfoPublishVO getInfo(String id) {
        return infoPublishMapper.selectJoinOne(InfoPublishVO.class, new MPJLambdaWrapper<InfoPublish>().eq(InfoPublish::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        Long count = infoPublishMapper.selectCount(new MPJLambdaWrapper<InfoPublish>()
                .eq(InfoPublish::getPublishStatus, PUBLISHED)
                .in(InfoPublish::getId, Arrays.stream(ids).collect(Collectors.toList())));
        if (count > 0) {
            throw new BusinessException("删除失败，无法删除已发布信息!");
        }
        infoPublishMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(InfoPublishDTO param) {
        InfoPublish exist = infoPublishMapper.selectOne(new MPJLambdaWrapper<InfoPublish>()
                .select(InfoPublish::getId, InfoPublish::getPublishStatus)
                .eq(InfoPublish::getId, param.getId()));
        if (exist.getPublishStatus() == PUBLISHED) {
            throw new BusinessException("编辑失败，信息已发布!");
        }
        InfoPublish infoPublish = BeanUtil.toBean(param, InfoPublish.class);
        infoPublishMapper.updateById(infoPublish);
    }

    @Override
    public void add(InfoPublishDTO param) {
        InfoPublish infoPublish = BeanUtil.toBean(param, InfoPublish.class);
        infoPublish.setPublishStatus(UN_PUBLISHED);
        infoPublishMapper.insert(infoPublish);
    }

    @Override
    public List<Map<String, Object>> uploadFile(List<MultipartFile> fileList) {
        if (CollectionUtils.isEmpty(fileList)) {
            throw new BusinessException("请选择至少一个文件上传!");
        }
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (MultipartFile file : fileList) {
            FileStorage fileStorage = fileStorageService.uploadFile(file);
            Map<String, Object> resultMap = new LinkedHashMap<>();
            resultMap.put("name", fileStorage.getOriginalName());
            resultMap.put("type", fileStorage.getSuffix());
            resultMap.put("url", fileStorage.getUrl());
            resultMap.put("size", fileStorage.getFileSize());
            resultList.add(resultMap);
        }
        return resultList;
    }

    @Override
    public void publish(String id) {
        InfoPublish infoPublish = infoPublishMapper.selectOne(new MPJLambdaWrapper<InfoPublish>()
                .select(InfoPublish::getId, InfoPublish::getPublishStatus)
                .eq(InfoPublish::getId, id));
        if (infoPublish.getPublishStatus() == PUBLISHED) {
            throw new BusinessException("信息已发布!");
        }
        infoPublish.setPublishStatus(PUBLISHED);
        infoPublish.setPublishTime(new Date());
        infoPublish.setPublisher(jwtUtils.getUserIdFromToken(jwtUtils.getToken()));
        infoPublishMapper.updateById(infoPublish);
        createRecord(infoPublish);
    }

    @Override
    public void cancel(String id) {
        InfoPublish infoPublish = infoPublishMapper.selectOne(new MPJLambdaWrapper<InfoPublish>()
                .select(InfoPublish::getId, InfoPublish::getPublishStatus)
                .eq(InfoPublish::getId, id));
        if (infoPublish.getPublishStatus() == UN_PUBLISHED) {
            throw new BusinessException("信息未发布!");
        }
        infoPublish.setPublishStatus(UN_PUBLISHED);
        infoPublish.setPublishTime(null);
        infoPublish.setPublisher(null);
        infoPublishMapper.updateById(infoPublish);
        createRecord(infoPublish);
    }

    /**
     * 生成发布/撤销记录
     *
     * @param infoPublish 发布信息
     */
    private void createRecord(InfoPublish infoPublish) {
        InfoPublishRecord record = new InfoPublishRecord();
        record.setInfoId(infoPublish.getId());
        record.setStatus(infoPublish.getPublishStatus());
        record.setOperator(infoPublish.getPublisher());
        record.setOperateTime(infoPublish.getPublishTime());
        infoPublishRecordMapper.insert(record);
    }
}
