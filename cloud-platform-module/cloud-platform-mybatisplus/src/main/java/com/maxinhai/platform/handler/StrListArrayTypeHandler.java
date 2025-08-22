package com.maxinhai.platform.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StrListArrayTypeHandler extends BaseTypeHandler<List<String>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
        // 将 List<String>转换为 String 数组，再设置到 PreparedStatement
        String [] array = parameter.toArray (new String [0]);
        ps.setArray (i, ps.getConnection ().createArrayOf ("text", array));
    }

    @Override
    public List<String> getNullableResult (ResultSet rs, String columnName) throws SQLException {
        // 从结果集中获取数组并转换为 List<String>
        Array array = rs.getArray(columnName);
        return array != null ? Arrays.asList((String[]) array.getArray()) : new ArrayList<>();
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Array array = rs.getArray(columnIndex);
        return array != null ? Arrays.asList((String[]) array.getArray()) : new ArrayList<>();
    }

    @Override
    public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Array array = cs.getArray(columnIndex);
        return array != null ? Arrays.asList((String[]) array.getArray()) : new ArrayList<>();
    }

}
