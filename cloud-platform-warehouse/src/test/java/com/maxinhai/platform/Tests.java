package com.maxinhai.platform;

import com.maxinhai.platform.utils.PgSqlTableGenerator;

import java.util.List;

/**
 * @ClassName：Tests
 * @Author: XinHai.Ma
 * @Date: 2025/9/24 14:51
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
public class Tests {

    // 测试示例
    public static void main(String[] args) {
        // 指定实体类包路径
        String packagePath = "com.maxinhai.platform.po";
        PgSqlTableGenerator generator = new PgSqlTableGenerator();
        List<String> sqlList = generator.generateTableSql(packagePath);

        // 打印生成的建表语句
        for (String sql : sqlList) {
            System.out.println(sql + "\n");
        }
    }

}
