package com.maxinhai.platform.vo.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel(description = "用户增长趋势VO")
public class UserGrowthTrendVO {

    @JsonProperty("xData")
    private List<String> xData = new ArrayList<>();  // X轴：日期/月份/年份
    @JsonProperty("yData")
    private List<Integer> yData = new ArrayList<>(); // Y轴：用户增长数量

    /**
     * 设置日期数据
     *
     * @param dataList
     */
    public void setDayData(List<UserGrowthTrendOfDayVO> dataList) {
        for (UserGrowthTrendOfDayVO data : dataList) {
            xData.add(data.getStatDate());
            yData.add(data.getUserCount());
        }
    }

    /**
     * 设置月份数据
     *
     * @param dataList
     */
    public void setMonthData(List<UserGrowthTrendOfMonthVO> dataList) {
        for (UserGrowthTrendOfMonthVO data : dataList) {
            xData.add(data.getStatMonth());
            yData.add(data.getUserCount());
        }
    }

    /**
     * 设置年份数据
     *
     * @param dataList
     */
    public void setYearData(List<UserGrowthTrendOfYearVO> dataList) {
        for (UserGrowthTrendOfYearVO data : dataList) {
            xData.add(data.getStatYear());
            yData.add(data.getUserCount());
        }
    }

}
