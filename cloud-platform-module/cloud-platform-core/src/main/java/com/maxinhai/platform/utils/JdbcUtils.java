package com.maxinhai.platform.utils;

import java.sql.*;
import java.util.*;

public class JdbcUtils {

    private static String inner_driver;
    private static String inner_url;
    private static String inner_username;
    private static String inner_password;

    // 静态初始化块，加载数据库配置
//    static {
//        try {
//            // 加载配置（实际项目中可从配置文件读取）
//            Properties props = new Properties();
//            // 这里使用硬编码示例，实际项目中应从外部配置读取
//            driver = "org.postgresql.Driver";
//            url = "jdbc:postgresql://localhost:5432/cloud-platform";
//            username = "postgres";
//            password = "MaXinHai!970923";
//
//            // 加载数据库驱动
//            Class.forName(driver);
//        } catch (Exception e) {
//            throw new RuntimeException("初始化数据库配置失败", e);
//        }
//    }

    /**
     * 设置数据源
     *
     * @param url
     * @param driver
     * @param username
     * @param password
     */
    public static void setDatasource(String url, String driver, String username, String password) {
        inner_driver = driver;
        inner_url = url;
        inner_username = username;
        inner_password = password;
        try {
            // 加载数据库驱动
            Class.forName(inner_driver);
        } catch (Exception e) {
            throw new RuntimeException("初始化数据库配置失败", e);
        }
    }

    /**
     * 获取数据库连接
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(inner_url, inner_username, inner_password);
    }

    /**
     * 关闭数据库连接
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<Map<String, Object>> selectList(String sql) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Map<String, Object>> resultList = new ArrayList<>();

        try {
            conn = JdbcUtils.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            // 获取结果集元数据（字段信息）
            ResultSetMetaData metaData = rs.getMetaData();
            // 获取字段总数
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Map<String, Object> result = new HashMap<>(columnCount);
                for (int i = 1; i <= columnCount; i++) {
                    // 获取字段名（或别名）作为Map的键，优先使用别名，无别名则用字段名
                    String columnName = metaData.getColumnLabel(i);
                    // 获取字段值作为Map的值
                    Object columnValue = rs.getObject(i);
                    // 获取Java类型
                    String javaType = metaData.getColumnClassName(i);
                    result.put(columnName, columnValue);
                }
                resultList.add(result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            JdbcUtils.closeConnection(conn);
        }

        return resultList;
    }

}
