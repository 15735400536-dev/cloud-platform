package com.maxinhai.platform.controller;

import com.maxinhai.platform.dto.UserVisitLogQueryDTO;
import com.maxinhai.platform.dto.WebVisitRecordDTO;
import com.maxinhai.platform.service.WebVisitRecordService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.vo.UserVisitDurationStatVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/visit")
public class WebVisitRecordController {

    @Resource
    private WebVisitRecordService webVisitRecordService;

    /**
     * 接收网页访问埋点数据
     * POST /api/visit/save
     * 请求体就是你原始json
     */
    @PostMapping("/save")
    public AjaxResult<Boolean> saveVisit(@RequestBody WebVisitRecordDTO dto) {
        return AjaxResult.success(webVisitRecordService.saveVisit(dto));
    }

    /**
     * 接收网页访问埋点数据
     * POST /api/visit/saveList
     * 请求体就是你原始json
     */
    @PostMapping("/saveList")
    public AjaxResult<Boolean> saveVisitList(@RequestBody List<WebVisitRecordDTO> dtoList) {
        return AjaxResult.success(webVisitRecordService.saveVisitList(dtoList));
    }

    @PostMapping("/selectUserDailyHostDurationStat")
    public AjaxResult<List<UserVisitDurationStatVO>> selectUserDailyHostDurationStat(@RequestBody UserVisitLogQueryDTO query) {
        return AjaxResult.success(webVisitRecordService.selectUserDailyHostDurationStat(query));
    }

}
