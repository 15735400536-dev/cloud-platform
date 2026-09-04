package com.maxinhai.platform.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxinhai.platform.dto.UserVisitLogQueryDTO;
import com.maxinhai.platform.dto.WebVisitRecordDTO;
import com.maxinhai.platform.mapper.WebVisitRecordMapper;
import com.maxinhai.platform.po.WebVisitRecord;
import com.maxinhai.platform.service.WebVisitRecordService;
import com.maxinhai.platform.vo.UserVisitDurationStatVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebVisitRecordServiceImpl  extends ServiceImpl<WebVisitRecordMapper, WebVisitRecord> implements WebVisitRecordService {

    @Override
    public boolean saveVisit(WebVisitRecordDTO dto) {
        // 参数简单校验
        if(dto == null || dto.getUrl() == null){
            return false;
        }
        WebVisitRecord entity = new WebVisitRecord();
        // DTO拷贝到实体；delFlag/createBy等公共字段由MetaObjectHandler自动填充
        BeanUtils.copyProperties(dto, entity);
        // mybatis‑plus insert，自动触发填充器
        return this.save(entity);
    }

    @Override
    public boolean saveVisitList(List<WebVisitRecordDTO> dtoList) {
        if(dtoList == null || dtoList.isEmpty()){
            return false;
        }
        List<WebVisitRecord> dataList = dtoList.stream().map(this::cover).collect(Collectors.toList());
        int row = baseMapper.insertBatch(dataList);
        return row >= dtoList.size();
    }

    @Override
    public List<UserVisitDurationStatVO> selectUserDailyHostDurationStat(UserVisitLogQueryDTO query) {
        return baseMapper.selectUserDailyHostDurationStat(query);
    }

    private WebVisitRecord cover(WebVisitRecordDTO dto) {
        WebVisitRecord record = new WebVisitRecord();
        // 👉手动生成雪花字符串ID！！手写xml批量不会自动生成
        if (record.getId() == null) {
            record.setId(IdUtil.getSnowflakeNextIdStr());
        }
        record.setClientId(dto.getClientId());
        record.setBrowserName(dto.getBrowserName());
        record.setBrowserVersion(dto.getBrowserVersion());
        record.setDay(dto.getDay());
        record.setDurationMs(dto.getDurationMs());
        record.setPublicIp(dto.getPublicIp());
        record.setStartTime(dto.getStartTime());
        record.setTimeStr(dto.getTimeStr());
        record.setTitle(dto.getTitle());
        record.setUrl(dto.getUrl());
        return record;
    }
}
