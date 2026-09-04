package com.maxinhai.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.maxinhai.platform.dto.UserVisitLogQueryDTO;
import com.maxinhai.platform.dto.WebVisitRecordDTO;
import com.maxinhai.platform.po.WebVisitRecord;
import com.maxinhai.platform.vo.UserVisitDurationStatVO;

import java.util.List;

public interface WebVisitRecordService extends IService<WebVisitRecord> {

    /**
     * 保存访问记录
     * @param dto 埋点入参
     * @return true/false
     */
    boolean saveVisit(WebVisitRecordDTO dto);

    boolean saveVisitList(List<WebVisitRecordDTO> records);

    List<UserVisitDurationStatVO> selectUserDailyHostDurationStat(UserVisitLogQueryDTO query);

}
