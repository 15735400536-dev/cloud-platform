package com.maxinhai.platform.utils;

import cn.dev33.satoken.stp.StpUtil;
import org.apache.http.auth.BasicUserPrincipal;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName：LoginUserContext
 * @Author: XinHai.Ma
 * @Date: 2025/8/20 13:47
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
public class LoginUserContext {

    private static final Map<String, Map<String, String>> cache = new ConcurrentHashMap<>();

    /**
     * 根据key设置Map到缓存
     *
     * @param key
     * @param dataMap
     */
    private static void set(String key, Map<String, String> dataMap) {
        cache.put(key, dataMap);
    }

    /**
     * 根据key和itemKey设置数值到缓存
     *
     * @param key
     * @param itemKey
     * @param itemValue
     */
    private static void set(String key, String itemKey, String itemValue) {
        if (cache.containsKey(key)) {
            cache.get(key).put(itemKey, itemValue);
        } else {
            Map<String, String> item = new ConcurrentHashMap<>();
            item.put(itemKey, itemValue);
            cache.put(key, item);
        }
    }

    /**
     * 根据key获取全部数据
     *
     * @param key
     * @return
     */
    private static Map<String, String> getValue(String key) {
        return cache.getOrDefault(key, new ConcurrentHashMap<>());
    }

    /**
     * 根据key和itemKey获取指定数据
     *
     * @param key
     * @param itemKey
     * @return
     */
    private static String getValue(String key, String itemKey) {
        return cache.getOrDefault(key, new ConcurrentHashMap<>()).get(itemKey);
    }

    /**
     * 根据key和itemKey获取指定数据
     * @param key
     * @param itemKey
     * @param defaultValue
     * @return
     */
    private static String getValue(String key, String itemKey, String defaultValue) {
        return cache.getOrDefault(key, new ConcurrentHashMap<>()).getOrDefault(itemKey, defaultValue);
    }

    /**
     * 根据key删除全部缓存
     *
     * @param key
     */
    private static void remove(String key) {
        if (cache.containsKey(key)) {
            return;
        }
        cache.remove(key);
    }

    /**
     * 根据key和子项key删除全部缓存
     *
     * @param key
     * @param itemKey
     */
    private static void remove(String key, String itemKey) {
        if (!cache.containsKey(key)) {
            return;
        }
        if (!cache.get(key).containsKey(itemKey)) {
            return;
        }
        cache.get(key).remove(itemKey);
    }

    // ======================================== 分割线 ========================================//

    /**
     * 根据Sa-Token登录ID设置缓存
     *
     * @param dataMap
     */
    public static void set(Map<String, String> dataMap) {
        if (!StpUtil.isLogin()) {
            throw new RuntimeException("当前用户未登录!");
        }
        String key = StpUtil.getLoginIdAsString();
        set(key, dataMap);
    }

    /**
     * 根据Sa-Token登录ID和子项ID获取缓存
     *
     * @param itemKey
     * @param itemValue
     */
    public static void setItemKey(String itemKey, String itemValue) {
        if (!StpUtil.isLogin()) {
            throw new RuntimeException("当前用户未登录!");
        }
        String key = StpUtil.getLoginIdAsString();
        set(key, itemKey, itemValue);
    }

    /**
     * 根据Sa-Token登录ID获取全部缓存
     *
     * @return
     */
    public static Map<String, String> getValue() {
        if (!StpUtil.isLogin()) {
            throw new RuntimeException("当前用户未登录!");
        }
        String key = StpUtil.getLoginIdAsString();
        return getValue(key);
    }

    /**
     * 根据Sa-Token登录ID和子项ID获取指定缓存
     *
     * @param itemKey
     * @return
     */
    public static String getItemValue(String itemKey) {
        if (!StpUtil.isLogin()) {
            throw new RuntimeException("当前用户未登录!");
        }
        String key = StpUtil.getLoginIdAsString();
        return getValue(key, itemKey);
    }

    /**
     * 据Sa-Token登录ID和子项ID获取指定缓存
     *
     * @param itemKey      子项ID
     * @param defaultValue 默认数值
     * @return 默认数值
     */
    public static String getItemValue(String itemKey, String defaultValue) {
        if (!StpUtil.isLogin()) {
            return defaultValue;
        }
        String key = StpUtil.getLoginIdAsString();
        return getValue(key, itemKey, defaultValue);
    }

    /**
     * 根据Sa-Token登录ID清除全部缓存
     */
    public static void remove() {
        if (!StpUtil.isLogin()) {
            throw new RuntimeException("当前用户未登录!");
        }
        String key = StpUtil.getLoginIdAsString();
        remove(key);
    }

    /**
     * 根据Sa-Token登录ID和子项ID清除指定缓存
     *
     * @param itemKey
     */
    public static void removeItem(String itemKey) {
        if (!StpUtil.isLogin()) {
            throw new RuntimeException("当前用户未登录!");
        }
        String key = StpUtil.getLoginIdAsString();
        remove(key, itemKey);
    }

}
