package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.bo.CustomConditionBO;
import com.maxinhai.platform.bo.CustomDataSourceBO;
import com.maxinhai.platform.bo.CustomSqlBO;
import com.maxinhai.platform.bo.CustomViewBO;
import com.maxinhai.platform.context.ReportContext;
import com.maxinhai.platform.dto.CustomReportAddDTO;
import com.maxinhai.platform.dto.CustomReportEditDTO;
import com.maxinhai.platform.dto.CustomReportQueryDTO;
import com.maxinhai.platform.mapper.*;
import com.maxinhai.platform.po.*;
import com.maxinhai.platform.service.CustomReportService;
import com.maxinhai.platform.vo.CustomReportPreviewVO;
import com.maxinhai.platform.vo.CustomReportVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName：CustomReportServiceImpl
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 17:59
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Slf4j
@Service
public class CustomReportServiceImpl extends ServiceImpl<CustomReportMapper, CustomReport>
        implements CustomReportService {

    @Resource
    private CustomReportMapper reportMapper;
    @Resource
    private CustomViewMapper viewMapper;
    @Resource
    private CustomReportViewRelMapper reportViewRelMapper;
    @Resource
    private CustomSqlMapper sqlMapper;
    @Resource
    private CustomConditionMapper conditionMapper;
    @Resource
    private CustomDataSourceMapper dataSourceMapper;

    @Override
    public Page<CustomReportVO> searchByPage(CustomReportQueryDTO param) {
        Page<CustomReportVO> pageResult = reportMapper.selectJoinPage(param.getPage(), CustomReportVO.class,
                new MPJLambdaWrapper<CustomReport>()
                        .like(StrUtil.isNotBlank(param.getKey()), CustomReport::getKey, param.getKey())
                        .like(StrUtil.isNotBlank(param.getTitle()), CustomReport::getTitle, param.getTitle())
                        .orderByDesc(CustomReport::getCreateTime));
        return pageResult;
    }

    @Override
    public CustomReportVO getInfo(String id) {
        return reportMapper.selectJoinOne(CustomReportVO.class, new MPJLambdaWrapper<CustomReport>()
                .eq(CustomReport::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        reportMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(CustomReportEditDTO param) {
        CustomReport report = BeanUtil.toBean(param, CustomReport.class);
        reportMapper.updateById(report);
    }

    @Override
    public void add(CustomReportAddDTO param) {
        CustomReport report = BeanUtil.toBean(param, CustomReport.class);
        reportMapper.insert(report);
    }

    private boolean flag = true;
    @Resource
    private ReportContext reportContext;

    @Override
    public CustomReportPreviewVO preview(String reportId) {
        flag = !flag;
        if (flag) {
            return BeanUtil.toBean(reportContext.getReport(reportId), CustomReportPreviewVO.class);
        } else {
            // 查新报表
            CustomReport report = reportMapper.selectOne(new LambdaQueryWrapper<CustomReport>()
                    .select(CustomReport::getId, CustomReport::getKey, CustomReport::getTitle)
                    .eq(CustomReport::getId, reportId));
            // 查询视图
            List<CustomView> viewList = viewMapper.selectList(new LambdaQueryWrapper<CustomView>()
                    // 查询字段
                    .select(CustomView::getId, CustomView::getKey, CustomView::getTitle, CustomView::getType, CustomView::getSqlId, CustomView::getReportId)
                    // 查询条件
                    .eq(CustomView::getReportId, report.getId())
                    // 排序
                    .orderByAsc(CustomView::getCreateTime));

            // 查询报表关联视图
//        List<CustomView> viewList = reportViewRelMapper.selectJoinList(CustomView.class, new MPJLambdaWrapper<CustomReportViewRel>()
//                .innerJoin(CustomReport.class, CustomReport::getId, CustomReportViewRel::getReportId)
//                .innerJoin(CustomView.class, CustomView::getId, CustomReportViewRel::getViewId)
//                // 查询条件
//                .eq(CustomReport::getId, reportId)
//                // 查询字段
//                .select(CustomView::getId, CustomView::getKey, CustomView::getTitle, CustomView::getType, CustomView::getSqlId)
//                // 排序
//                .orderByAsc(CustomReportViewRel::getSort));

            // 查询SQL语句
            List<String> sqlIds = viewList.stream().map(CustomView::getSqlId).distinct().collect(Collectors.toList());
            List<CustomSql> sqlList = sqlMapper.selectList(new LambdaQueryWrapper<CustomSql>()
                    .select(CustomSql::getId, CustomSql::getDataSourceId, CustomSql::getSql)
                    .in(CustomSql::getId, sqlIds));
            Map<String, CustomSql> sqlMap = sqlList.stream().collect(Collectors.toMap(CustomSql::getId, CustomSql -> CustomSql));
            // 查询筛选条件
            Map<String, List<CustomCondition>> conditionMap = conditionMapper.selectList(new LambdaQueryWrapper<CustomCondition>()
                            .in(CustomCondition::getSqlId, sqlIds)).stream()
                    .collect(Collectors.groupingBy(CustomCondition::getSqlId));
            // 查询数据源
            List<String> dataSourceIds = sqlList.stream().map(CustomSql::getDataSourceId).distinct().collect(Collectors.toList());
            Map<String, CustomDataSource> dataSourceMap = dataSourceMapper.selectList(new LambdaQueryWrapper<CustomDataSource>()
                            .select(CustomDataSource::getId, CustomDataSource::getKey, CustomDataSource::getType, CustomDataSource::getDatabase,
                                    CustomDataSource::getUrl, CustomDataSource::getDriverClassName, CustomDataSource::getUsername, CustomDataSource::getPassword)
                            .in(CustomDataSource::getId, dataSourceIds)).stream()
                    .collect(Collectors.toMap(CustomDataSource::getId, CustomDataSource -> CustomDataSource));
            // 组装返回结果
            CustomReportPreviewVO resultVO = BeanUtil.toBean(report, CustomReportPreviewVO.class);
            List<CustomViewBO> viewBOList = BeanUtil.copyToList(viewList, CustomViewBO.class);
            for (CustomViewBO viewBO : viewBOList) {
                CustomSqlBO sqlBO = BeanUtil.toBean(sqlMap.get(viewBO.getSqlId()), CustomSqlBO.class);
                sqlBO.setDataSource(BeanUtil.toBean(dataSourceMap.get(sqlBO.getDataSourceId()), CustomDataSourceBO.class));
                sqlBO.setConditionList(BeanUtil.copyToList(conditionMap.get(sqlBO.getId()), CustomConditionBO.class));
                viewBO.setCustomSql(sqlBO);
            }
            resultVO.setViewList(viewBOList);
            return resultVO;
        }
    }
}
