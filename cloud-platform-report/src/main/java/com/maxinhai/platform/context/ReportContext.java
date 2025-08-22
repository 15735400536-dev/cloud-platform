package com.maxinhai.platform.context;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.bo.*;
import com.maxinhai.platform.mapper.*;
import com.maxinhai.platform.po.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @ClassName：ReportContext
 * @Author: XinHai.Ma
 * @Date: 2025/8/22 15:37
 * @Description: 报表上下文
 */
@Component
public class ReportContext implements CommandLineRunner {

    @Resource
    private CustomReportMapper reportMapper;
    @Resource
    private CustomViewMapper viewMapper;
    @Resource
    private CustomSqlMapper sqlMapper;
    @Resource
    private CustomConditionMapper conditionMapper;
    @Resource
    private CustomDataSourceMapper dataSourceMapper;

    private static final Map<String, CustomReportBO> context = new ConcurrentHashMap<>();

    /**
     * 根据报表ID更新上下文
     *
     * @param reportId
     */
    public void update(String reportId) {
        // 查新报表
        CustomReport report = reportMapper.selectOne(new LambdaQueryWrapper<CustomReport>()
                .select(CustomReport::getId, CustomReport::getKey, CustomReport::getTitle)
                .eq(CustomReport::getId, reportId));
        if (Objects.isNull(report)) {
            return;
        }
        // 查询报表关联视图
        List<CustomView> viewList = viewMapper.selectJoinList(CustomView.class, new MPJLambdaWrapper<CustomView>()
                .innerJoin(CustomReportViewRel.class, CustomReportViewRel::getViewId, CustomView::getId)
                .innerJoin(CustomReport.class, CustomReport::getId, CustomReportViewRel::getReportId)
                // 查询条件
                .eq(CustomReport::getId, reportId)
                // 查询字段
                .select(CustomView::getId, CustomView::getKey, CustomView::getTitle, CustomView::getType, CustomView::getSqlId)
                // 排序
                .orderByAsc(CustomReportViewRel::getSort));
        // 查询SQL语句
        String sqlIds = viewList.stream().map(CustomView::getSqlId).distinct().collect(Collectors.joining());
        List<CustomSql> sqlList = sqlMapper.selectList(new LambdaQueryWrapper<CustomSql>()
                .select(CustomSql::getId, CustomSql::getDataSourceId, CustomSql::getSql)
                .in(CustomSql::getId, sqlIds));
        Map<String, CustomSql> sqlMap = sqlList.stream().collect(Collectors.toMap(CustomSql::getId, CustomSql -> CustomSql));
        // 查询筛选条件
        Map<String, List<CustomCondition>> conditionMap = conditionMapper.selectList(new LambdaQueryWrapper<CustomCondition>()
                        .in(CustomCondition::getId, sqlIds)).stream()
                .collect(Collectors.groupingBy(CustomCondition::getSqlId));
        // 查询数据源
        List<String> dataSourceIds = sqlList.stream().map(CustomSql::getDataSourceId).distinct().collect(Collectors.toList());
        Map<String, CustomDataSource> dataSourceMap = dataSourceMapper.selectList(new LambdaQueryWrapper<CustomDataSource>()
                        .select(CustomDataSource::getId, CustomDataSource::getKey, CustomDataSource::getType,
                                CustomDataSource::getUrl, CustomDataSource::getDriverClassName, CustomDataSource::getUsername, CustomDataSource::getPassword)
                        .in(CustomDataSource::getId, dataSourceIds)).stream()
                .collect(Collectors.toMap(CustomDataSource::getId, CustomDataSource -> CustomDataSource));
        // 组装返回结果
        CustomReportBO reportBO = BeanUtil.toBean(report, CustomReportBO.class);
        List<CustomViewBO> viewBOList = BeanUtil.copyToList(viewList, CustomViewBO.class);
        for (CustomViewBO viewBO : viewBOList) {
            CustomSqlBO sqlBO = BeanUtil.toBean(sqlMap.get(viewBO.getSqlId()), CustomSqlBO.class);
            sqlBO.setDataSource(BeanUtil.toBean(dataSourceMap.get(sqlBO.getDataSourceId()), CustomDataSourceBO.class));
            sqlBO.setConditionList(BeanUtil.copyToList(conditionMap.get(sqlBO.getId()), CustomConditionBO.class));
            viewBO.setCustomSql(sqlBO);
        }
        reportBO.setViewList(viewBOList);
        context.put(reportId, reportBO);
    }

    /**
     * 根据报表ID获取报表数据
     *
     * @param reportId
     * @return
     */
    private CustomReportBO getReport(String reportId) {
        return context.get(reportId);
    }

    /**
     * 根据报表ID和查询语句ID获取数据源
     *
     * @param reportId 报表ID
     * @param sqlId    查询语句ID
     * @return
     */
    public CustomDataSourceBO getDataSource(String reportId, String sqlId) {
        CustomReportBO reportBO = context.get(reportId);
        for (CustomViewBO viewBO : reportBO.getViewList()) {
            CustomSqlBO sqlBO = viewBO.getCustomSql();
            if (sqlId.equals(sqlBO.getId())) {
                return sqlBO.getDataSource();
            }
        }
        return null;
    }

    /**
     * 根据报表ID和查询语句ID获取查询条件
     *
     * @param reportId 报表ID
     * @param sqlId    查询语句ID
     * @return
     */
    public List<CustomConditionBO> getConditionList(String reportId, String sqlId) {
        CustomReportBO reportBO = context.get(reportId);
        for (CustomViewBO viewBO : reportBO.getViewList()) {
            CustomSqlBO sqlBO = viewBO.getCustomSql();
            if (sqlId.equals(sqlBO.getId())) {
                return sqlBO.getConditionList();
            }
        }
        return null;
    }

    /**
     * 服务启动，加载上下文内容
     */
    public void load() {
        List<CustomReport> reportList = loadReport();
        List<CustomView> viewList = loadView();
        List<CustomSql> sqlList = loadSql();
        List<CustomDataSource> dataSourceList = loadDataSource();
        List<CustomCondition> conditionList = loadCondition();
        List<CustomReportBO> assembly = assembly(reportList, viewList, sqlList, conditionList, dataSourceList);
        for (CustomReportBO reportBO : assembly) {
            context.put(reportBO.getId(), reportBO);
        }
    }

    @Override
    public void run(String... args) throws Exception {
        load();
    }

    /**
     * 加载报表
     *
     * @return
     */
    private List<CustomReport> loadReport() {
        return reportMapper.selectList(new LambdaQueryWrapper<CustomReport>()
                .select(CustomReport::getId, CustomReport::getKey, CustomReport::getTitle));
    }

    /**
     * 加载视图
     *
     * @return
     */
    private List<CustomView> loadView() {
        return viewMapper.selectJoinList(CustomView.class, new MPJLambdaWrapper<CustomView>()
                .innerJoin(CustomReportViewRel.class, CustomReportViewRel::getViewId, CustomView::getId)
                .innerJoin(CustomReport.class, CustomReport::getId, CustomReportViewRel::getReportId)
                // 查询字段
                .select(CustomView::getId, CustomView::getKey, CustomView::getTitle, CustomView::getType, CustomView::getSqlId)
                // 排序
                .orderByAsc(CustomReportViewRel::getReportId, CustomReportViewRel::getSort));
    }

    /**
     * 加载自定义SQL
     *
     * @return
     */
    private List<CustomSql> loadSql() {
        return sqlMapper.selectList(new LambdaQueryWrapper<CustomSql>()
                .select(CustomSql::getId, CustomSql::getDataSourceId, CustomSql::getDataSourceId));
    }

    /**
     * 加载数据源
     *
     * @return
     */
    private List<CustomDataSource> loadDataSource() {
        return dataSourceMapper.selectList(new LambdaQueryWrapper<CustomDataSource>()
                .select(CustomDataSource::getId, CustomDataSource::getKey, CustomDataSource::getType,
                        CustomDataSource::getUrl, CustomDataSource::getDriverClassName, CustomDataSource::getUsername, CustomDataSource::getPassword));
    }

    /**
     * 加载筛选条件
     *
     * @return
     */
    private List<CustomCondition> loadCondition() {
        return conditionMapper.selectList(new LambdaQueryWrapper<CustomCondition>()
                .select(CustomCondition::getId, CustomCondition::getField, CustomCondition::getCondition, CustomCondition::getSqlId,
                        CustomCondition::getMinVal, CustomCondition::getMaxVal, CustomCondition::getStandardVal, CustomCondition::getRange));
    }

    /**
     * 组装报表上下文数据
     *
     * @param reportList
     * @param viewList
     * @param sqlList
     * @param conditionList
     * @param dataSourceList
     * @return
     */
    private List<CustomReportBO> assembly(List<CustomReport> reportList, List<CustomView> viewList, List<CustomSql> sqlList,
                                          List<CustomCondition> conditionList, List<CustomDataSource> dataSourceList) {
        if (CollectionUtils.isEmpty(reportList)) {
            return new ArrayList<>();
        }
        // 视图根据报表ID分组
        Map<String, List<CustomView>> viewMap = viewList.stream().collect(Collectors.groupingBy(CustomView::getReportId));
        // 查询SQL根据SQL ID分组
        Map<String, CustomSql> sqlMap = sqlList.stream().collect(Collectors.toMap(CustomSql::getId, CustomSql -> CustomSql));
        // 筛选条件根据SQL ID分组
        Map<String, List<CustomCondition>> conditionMap = conditionList.stream().collect(Collectors.groupingBy(CustomCondition::getSqlId));
        // 数据源根据数据源ID分组
        Map<String, CustomDataSource> dataSourceMap = dataSourceList.stream().collect(Collectors.toMap(CustomDataSource::getId, CustomDataSource -> CustomDataSource));
        // 组装数据
        List<CustomReportBO> reportBOList = BeanUtil.copyToList(reportList, CustomReportBO.class);
        for (CustomReportBO reportBO : reportBOList) {
            List<CustomViewBO> viewBOList = BeanUtil.copyToList(viewMap.get(reportBO.getId()), CustomViewBO.class);
            for (CustomViewBO viewBO : viewBOList) {
                CustomSqlBO sqlBO = BeanUtil.toBean(sqlMap.get(viewBO.getSqlId()), CustomSqlBO.class);
                sqlBO.setDataSource(BeanUtil.toBean(dataSourceMap.get(sqlBO.getDataSourceId()), CustomDataSourceBO.class));
                sqlBO.setConditionList(BeanUtil.copyToList(conditionMap.get(sqlBO.getId()), CustomConditionBO.class));
                viewBO.setCustomSql(sqlBO);
            }
            reportBO.setViewList(viewBOList);
        }
        return reportBOList;
    }

}
