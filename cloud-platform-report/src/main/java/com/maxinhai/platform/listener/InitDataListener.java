package com.maxinhai.platform.listener;

import cn.hutool.core.lang.UUID;
import com.maxinhai.platform.enums.ChartType;
import com.maxinhai.platform.enums.ConditionEnum;
import com.maxinhai.platform.enums.DbType;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.mapper.*;
import com.maxinhai.platform.po.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@Order(Ordered.LOWEST_PRECEDENCE) // 最低优先级，最后执行
public class InitDataListener implements CommandLineRunner {

    @Resource
    private CustomDataSourceMapper dataSourceMapper;
    @Resource
    private CustomConditionMapper conditionMapper;
    @Resource
    private CustomSqlMapper sqlMapper;
    @Resource
    private CustomViewMapper viewMapper;
    @Resource
    private CustomReportMapper reportMapper;

    @Override
    public void run(String... args) throws Exception {
        initData();
    }

    //@Scheduled(initialDelay = 10000, fixedDelay = 60000)
    public void initData() {
        CustomReport report = new CustomReport();
        report.setKey(UUID.fastUUID().toString());
        report.setTitle("自定义报表");
        reportMapper.insert(report);

        CustomDataSource postgres = new CustomDataSource();
        postgres.setKey("pgsql");
        postgres.setType(DbType.PgSQL);
        postgres.setDriverClassName("org.postgresql.Driver");
        postgres.setUrl("jdbc:postgresql://localhost:5432/cloud-platform");
        postgres.setUsername("postgres");
        postgres.setPassword("MaXinHai!970923");
        postgres.setDatabase("cloud-platform");
        dataSourceMapper.insert(postgres);

        CustomSql sql = new CustomSql();
        sql.setSql("select * from prod_order");
        sql.setDataSourceId(postgres.getId());
        sqlMapper.insert(sql);

        String[] array = {"id", "del_flag", "create_time", "update_time", "create_by", "update_by"};
        for (int i = 0; i < array.length; i++) {
            CustomCondition condition = new CustomCondition();
            condition.setField(array[i]);
            condition.setSqlId(sql.getId());
            switch (i) {
                case 0:
                case 1:
                    condition.setCondition(ConditionEnum.eq);
                    condition.setStandardVal("1");
                    break;
                case 2:
                case 3:
                    condition.setCondition(ConditionEnum.between);
                    condition.setMinVal("2025-01-01 00:00:00");
                    condition.setMaxVal("2025-12-31 23:59:59");
                    break;
                case 4:
                case 5:
                    condition.setCondition(ConditionEnum.like);
                    condition.setStandardVal("anonymous");
                    break;
                default:
            }
            conditionMapper.insert(condition);
        }

        for (int i = 0; i < 4; i++) {
            CustomView view = new CustomView();
            switch (i) {
                case 0:
                    view.setKey(ChartType.TABLE.getKey());
                    view.setTitle(ChartType.TABLE.getValue());
                    view.setType(ChartType.TABLE);
                    break;
                case 1:
                    view.setKey(ChartType.PIE_CHART.getKey());
                    view.setTitle(ChartType.PIE_CHART.getValue());
                    view.setType(ChartType.PIE_CHART);
                    break;
                case 2:
                    view.setKey(ChartType.BAR_CHART.getKey());
                    view.setTitle(ChartType.BAR_CHART.getValue());
                    view.setType(ChartType.BAR_CHART);
                    break;
                case 3:
                    view.setKey(ChartType.LINE_CHART.getKey());
                    view.setTitle(ChartType.LINE_CHART.getValue());
                    view.setType(ChartType.LINE_CHART);
                    break;
                default:
                    throw new BusinessException("未知图表类型!");
            }
            view.setSqlId(sql.getId());
            view.setReportId(report.getId());
            viewMapper.insert(view);
        }
    }

}
