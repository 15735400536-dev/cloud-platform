package com.maxinhai.platform.utils;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.List;

/**
 * @ClassName：LiquibaseTableGenerator
 * @Author: XinHai.Ma
 * @Date: 2025/9/2 11:43
 * @Description: Liquibase<createTable>标签生成器
 */
@Slf4j
public class LiquibaseTableGenerator {

    // 数据库连接信息
    private static final String URL = "jdbc:mysql://localhost:3306/your_db";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static void main(String[] args) throws SQLException {
        // 要生成的表名列表
        List<String> tableNames = List.of("user", "order", "product");

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            DatabaseMetaData metaData = conn.getMetaData();

            for (String tableName : tableNames) {
                generateCreateTableXml(metaData, tableName);
            }
        }
    }

    /**
     * 生成单个表的createTable标签
     */
    private static void generateCreateTableXml(DatabaseMetaData metaData, String tableName) throws SQLException {
        System.out.println("<!-- 创建" + tableName + "表 -->");
        System.out.println("<createTable tableName=\"" + tableName + "\">");

        // 获取表字段信息
        ResultSet columns = metaData.getColumns(null, null, tableName, null);
        while (columns.next()) {
            String columnName = columns.getString("COLUMN_NAME");
            String typeName = columns.getString("TYPE_NAME"); // 数据库类型（如VARCHAR）
            int columnSize = columns.getInt("COLUMN_SIZE"); // 长度（如50）
            boolean isNullable = columns.getInt("NULLABLE") == DatabaseMetaData.columnNullable;
            boolean isAutoIncrement = "YES".equals(columns.getString("IS_AUTOINCREMENT"));

            // 构建column标签
            StringBuilder columnSb = new StringBuilder();
            columnSb.append("    <column name=\"").append(columnName).append("\" ");
            columnSb.append("type=\"").append(typeName).append("(").append(columnSize).append(")\" ");
            if (isAutoIncrement) {
                columnSb.append("autoIncrement=\"true\" ");
            }
            columnSb.append(">");
            System.out.println(columnSb);

            // 构建constraints标签
            StringBuilder constraintsSb = new StringBuilder();
            constraintsSb.append("        <constraints ");
            // 判断是否为主键
            ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, tableName);
            boolean isPrimaryKey = false;
            while (primaryKeys.next()) {
                if (columnName.equals(primaryKeys.getString("COLUMN_NAME"))) {
                    isPrimaryKey = true;
                    break;
                }
            }
            if (isPrimaryKey) {
                constraintsSb.append("primaryKey=\"true\" ");
            }
            constraintsSb.append("nullable=\"").append(isNullable).append("\" ");
            // 判断是否为唯一键（简化版，实际可能需要查询唯一索引）
            constraintsSb.append("/>");
            System.out.println(constraintsSb);

            System.out.println("    </column>");
        }

        System.out.println("</createTable>");
        System.out.println();
    }

}
