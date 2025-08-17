package com.maxinhai.platform.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ClassUtil;
import com.maxinhai.platform.po.ComboBox;
import com.maxinhai.platform.po.RecordEntity;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 下拉框工具类
 */
public class ComboBoxUtils {

    private ComboBoxUtils() {
    }

    /**
     * JavaBean转ComboBox
     *
     * @param record JavaBean
     * @return
     */
    public static ComboBox convert(RecordEntity record) {
        String id = null;
        String name = null;
        if (ClassUtil.getDeclaredField(record.getClass(), "id") != null) {
            id = (String) BeanUtil.getFieldValue(record, "id");
        }
        if (ClassUtil.getDeclaredField(record.getClass(), "name") != null) {
            name = (String) BeanUtil.getFieldValue(record, "name");
        }
        return new ComboBox(id, name);
    }

    /**
     * JavaBean转ComboBox
     *
     * @param record    JavaBean
     * @param idField   id属性名称
     * @param nameField name属性名称
     * @return
     */
    public static ComboBox convert(RecordEntity record, String idField, String nameField) {
        String id = null;
        String name = null;
        // 在当前类中查找id属性
        if (ClassUtil.getDeclaredField(record.getClass(), idField) != null) {
            id = (String) BeanUtil.getFieldValue(record, idField);
        }
        // 到当前类的父类中查找id属性
        if (null == id) {
            Field field = findFieldIncludeParent(record.getClass(), idField);
            id = (String) BeanUtil.getFieldValue(record, field.getName());
        }
        // 在当前类中查找name属性
        if (ClassUtil.getDeclaredField(record.getClass(), nameField) != null) {
            name = (String) BeanUtil.getFieldValue(record, nameField);
        }
        // 到当前类的父类中查找name属性
        if (null == name) {
            Field field = findFieldIncludeParent(record.getClass(), nameField);
            name = (String) BeanUtil.getFieldValue(record, field.getName());
        }
        return new ComboBox(id, name);
    }

    /**
     * JavaBean转ComboBox（推荐）
     *
     * @param record     JavaBean
     * @param idGetter   id属性 getter 方法引用
     * @param nameGetter name属性 getter 方法引用
     * @return
     */
    public static <T, R> ComboBox convert(T record, Function<T, R> idGetter, Function<T, R> nameGetter) {
        if (record == null || idGetter == null || nameGetter == null) {
            return new ComboBox(); // 处理空对象或空引用的情况
        }
        // 调用方法引用获取属性值
        return new ComboBox(idGetter.apply(record).toString(), nameGetter.apply(record).toString());
    }

    /**
     * 批量JavaBean转ComboBox
     *
     * @param records JavaBean集合
     * @return
     */
    public static List<ComboBox> convert(List<RecordEntity> records) {
        return records.stream().map(record -> convert(record)).collect(Collectors.toList());
    }

    /**
     * 批量JavaBean转ComboBox
     *
     * @param records   JavaBean集合
     * @param idField   id属性名称
     * @param nameField name属性名称
     * @return
     */
    public static List<ComboBox> convert(List<RecordEntity> records, String idField, String nameField) {
        return records.stream().map(record -> convert(record, idField, nameField)).collect(Collectors.toList());
    }

    /**
     * 递归查找类及父类中的字段
     *
     * @param clazz     目标类
     * @param fieldName 字段名
     * @return 字段对象（不存在则返回 null）
     */
    private static Field findFieldIncludeParent(Class<?> clazz, String fieldName) {
        // 查找当前类的字段
        Field field = ClassUtil.getDeclaredField(clazz, fieldName);
        if (field != null) {
            return field;
        }

        // 递归查找父类（直到 Object 类）
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null && superClass != Object.class) {
            return findFieldIncludeParent(superClass, fieldName);
        }

        // 所有类中都未找到字段
        return null;
    }

}
