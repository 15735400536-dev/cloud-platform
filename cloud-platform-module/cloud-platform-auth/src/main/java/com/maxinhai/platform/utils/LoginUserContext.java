package com.maxinhai.platform.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户上下文工具类，存储当前登录用户信息
 */
public class LoginUserContext {

    private static final Map<String, Map<String, String>> cache = new ConcurrentHashMap<>();

    /**
     * 根据key设置Map到缓存
     *
     * @param key
     * @param dataMap
     */
    public static void set(String key, Map<String, String> dataMap) {
        cache.put(key, dataMap);
    }

    /**
     * 根据key和itemKey设置数值到缓存
     *
     * @param key
     * @param itemKey
     * @param itemValue
     */
    public static void set(String key, String itemKey, String itemValue) {
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
    public static Map<String, String> getValue(String key) {
        return cache.getOrDefault(key, new ConcurrentHashMap<>());
    }

    /**
     * 根据key和itemKey获取指定数据
     *
     * @param key
     * @param itemKey
     * @return
     */
    public static String getValue(String key, String itemKey) {
        return cache.getOrDefault(key, new ConcurrentHashMap<>()).get(itemKey);
    }

    /**
     * 根据key和itemKey获取指定数据
     * @param key
     * @param itemKey
     * @param defaultValue
     * @return
     */
    public static String getValue(String key, String itemKey, String defaultValue) {
        return cache.getOrDefault(key, new ConcurrentHashMap<>()).getOrDefault(itemKey, defaultValue);
    }

    /**
     * 根据key删除全部缓存
     *
     * @param key
     */
    public static void remove(String key) {
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
    public static void remove(String key, String itemKey) {
        if (!cache.containsKey(key)) {
            return;
        }
        if (!cache.get(key).containsKey(itemKey)) {
            return;
        }
        cache.get(key).remove(itemKey);
    }

}
