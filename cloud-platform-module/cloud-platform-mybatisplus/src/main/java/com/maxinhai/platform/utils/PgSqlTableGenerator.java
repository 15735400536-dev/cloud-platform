package com.maxinhai.platform.utils;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName：PgSqlTableGenerator
 * @Author: XinHai.Ma
 * @Date: 2025/9/24 14:09
 * @Description: PostgreSQL建表语句生成工具类，支持指定包路径，根据实体类生成建表语句
 */
public class PgSqlTableGenerator {

    /**
     * Java类型到PostgreSQL类型的映射（含默认长度）
     */
    private static final Map<Class<?>, String> TYPE_MAPPING = new HashMap<Class<?>, String>() {{
        put(String.class, "varchar(255)");
        put(int.class, "int4");
        put(Integer.class, "int4");
        put(long.class, "int8");
        put(Long.class, "int8");
        put(float.class, "float4");
        put(Float.class, "float4");
        put(double.class, "float8");
        put(Double.class, "float8");
        put(boolean.class, "bool");
        put(Boolean.class, "bool");
        put(Date.class, "timestamp");
        put(LocalDate.class, "date");
        put(LocalDateTime.class, "timestamp");
        put(BigDecimal.class, "numeric(19,2)");
        put(BigInteger.class, "numeric");
        put(Enum.class, "varchar(32)");
        // 可扩展其他类型（如byte[] -> bytea）
    }};

    /**
     * 默认值不为空字段
     */
    private static final Set<String> NOT_NULL_COLUMNS = new HashSet<>();

    static {
        NOT_NULL_COLUMNS.add("id");
        NOT_NULL_COLUMNS.add("del_flag");
        NOT_NULL_COLUMNS.add("create_by");
        NOT_NULL_COLUMNS.add("create_time");
    }


    /**
     * 生成指定包路径下所有实体类的建表语句
     *
     * @param basePackage 包路径（如"com.maxinhai.platform.po"）
     * @return 建表语句列表（每个实体类对应一条）
     */
    public List<String> generateTableSql(String basePackage) {
        // 1. 扫描包下所有带@TableName注解的实体类
        List<Class<?>> entityClasses = scanEntityClasses(basePackage);
        if (entityClasses.isEmpty()) {
            System.out.println("未找到实体类，请检查包路径：" + basePackage);
            return Collections.emptyList();
        }

        // 2. 为每个实体类生成建表语句
        return entityClasses.stream()
                .map(this::generateSingleTableSql)
                .collect(Collectors.toList());
    }

    /**
     * 扫描指定包下带@TableName注解的类
     */
    private List<Class<?>> scanEntityClasses(String basePackage) {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        // 只扫描带@TableName注解的类
        scanner.addIncludeFilter(new AnnotationTypeFilter(TableName.class));

        return scanner.findCandidateComponents(basePackage).stream()
                .map(beanDefinition -> {
                    try {
                        return Class.forName(beanDefinition.getBeanClassName());
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException("加载实体类失败：" + beanDefinition.getBeanClassName(), e);
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * 获取当前类及所有父类的非静态、非transient字段
     */
    private List<Field> getAllFields(Class<?> entityClass) {
        List<Field> allFields = new ArrayList<>();
        Class<?> currentClass = entityClass;

        // 遍历继承链：从当前类向上直到Object类（不包含Object）
        while (currentClass != null && currentClass != Object.class) {
            // 获取当前类的所有声明字段，过滤静态和transient
            List<Field> currentFields = Arrays.stream(currentClass.getDeclaredFields())
                    .filter(field -> !(Modifier.isStatic(field.getModifiers())
                            || Modifier.isTransient(field.getModifiers())))
                    .collect(Collectors.toList());
            allFields.addAll(currentFields);

            // 移动到父类
            currentClass = currentClass.getSuperclass();
        }
        return allFields;
    }

    /**
     * 为单个实体类生成建表语句
     */
    private String generateSingleTableSql(Class<?> entityClass) {
        // 1. 获取表名（优先使用@Table注解，否则类名转下划线）
        String tableName = getTableName(entityClass);

        // 2. 解析所有字段（排除静态字段和transient字段）
        List<Field> fields = getAllFields(entityClass);

        if (fields.isEmpty()) {
            throw new RuntimeException("实体类" + entityClass.getName() + "无有效字段");
        }

        // 3. 生成字段定义（含类型、约束）
        List<String> columnDefinitions = new ArrayList<>();
        String primaryKey = null;

        for (Field field : fields) {
            // 3.1 解析字段名（优先@Column.name，否则字段名转下划线）
            String columnName = getColumnName(field);

            // 3.2 解析字段类型（含长度）
            String columnType = getColumnType(field);

            // 3.3 解析约束（主键、非空）
            StringBuilder columnDef = new StringBuilder();
            columnDef.append("\"").append(columnName).append("\" ").append(columnType);

            // 处理主键（@TableId注解）
            if (field.isAnnotationPresent(TableId.class)) {
                primaryKey = columnName;
                columnDef.append(" NOT NULL"); // 主键默认非空
            } else {
                // 非主键字段：根据NOT_NULL_COLUMNS判断是否允许为空（默认允许）
                if (NOT_NULL_COLUMNS.contains(columnName)) {
                    columnDef.append(" NOT NULL");
                }
            }

            columnDefinitions.add(columnDef.toString());
        }

        // 4. 拼接主键约束
        if (primaryKey == null) {
            throw new RuntimeException("实体类" + entityClass.getName() + "未找到@TableId主键字段");
        }
        columnDefinitions.add("PRIMARY KEY (\"" + primaryKey + "\")");

        // 5. 生成完整建表语句
        return String.format("CREATE TABLE IF NOT EXISTS \"%s\" (\n  %s\n);",
                tableName,
                String.join(",\n  ", columnDefinitions)
        );
    }

    /**
     * 获取表名（处理@TableName注解）
     */
    private String getTableName(Class<?> entityClass) {
        TableName tableAnn = entityClass.getAnnotation(TableName.class);
        if (tableAnn != null && !tableAnn.value().isEmpty()) {
            return tableAnn.value();
        }
        // 类名驼峰转下划线（如UserInfo -> user_info）
        return camelToUnderline(entityClass.getSimpleName());
    }

    /**
     * 获取字段名（处理@Column注解）
     */
    private String getColumnName(Field field) {
        // 字段名驼峰转下划线（如userName -> user_name）
        return camelToUnderline(field.getName());
    }

    /**
     * 获取字段类型（处理@TableField，默认使用类型映射）
     */
    private String getColumnType(Field field) {
        Class<?> fieldType = field.getType();
        // 检查是否有自定义类型映射
//        if (!TYPE_MAPPING.containsKey(fieldType)) {
//            throw new RuntimeException("不支持的字段类型：" + fieldType.getName() + "（字段：" + field.getName() + "）");
//        }
        String defaultType = TYPE_MAPPING.getOrDefault(fieldType, "varchar(32)");

        // 处理String类型的长度（@TableField）
        if (String.class.equals(fieldType)) {
            int length = 255;
            return "varchar(" + length + ")";
        }

        // 处理Integer类型的长度（@TableField）
        if (Integer.class.equals(fieldType)) {
            return defaultType;
        }

        // 处理Long类型的长度（@TableField）
        if (Long.class.equals(fieldType)) {
            return defaultType;
        }

        // 处理Double类型的长度（@TableField）
        if (Double.class.equals(fieldType)) {
            return defaultType;
        }

        // 处理Float类型的长度（@TableField）
        if (Float.class.equals(fieldType)) {
            return defaultType;
        }

        // 处理Boolean类型的长度（@TableField）
        if (Boolean.class.equals(fieldType)) {
            return defaultType;
        }

        // 处理Date类型的长度（@TableField）
        if (Date.class.equals(fieldType)) {
            return defaultType;
        }

        // 处理LocalDateTime类型的长度（@TableField）
        if (LocalDateTime.class.equals(fieldType)) {
            return defaultType;
        }

        // 处理BigInteger类型的长度（@TableField）
        if (BigInteger.class.equals(fieldType)) {
            int precision = 11; // 总位数
            return "numeric(" + precision + ",0)";
        }

        // 处理BigDecimal的精度（@TableField）
        if (BigDecimal.class.equals(fieldType)) {
            int precision = 10; // 总位数
            int scale = 2; // 小数位数
            return "numeric(" + precision + "," + scale + ")";
        }

        return defaultType;
    }

    /**
     * 驼峰命名转下划线命名
     */
    private String camelToUnderline(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(Character.toLowerCase(str.charAt(0)));
        for (int i = 1; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append("_").append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
