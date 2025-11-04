package com.maxinhai.platform.utils;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * 用Java集合框架模拟Redis五种数据类型的核心操作
 */
public class LocalCacheUtils {

    // 底层存储：key -> 对应类型的value（线程安全）
    private static final ConcurrentHashMap<Object, Object> storage = new ConcurrentHashMap<>();
    // 过期时间存储：key -> 过期时间戳(毫秒)，null表示不过期
    private static final ConcurrentHashMap<Object, Long> expireMap = new ConcurrentHashMap<>();

    // ========================== 过期键处理核心方法 ==========================

    /**
     * 检查并清理单个过期键（惰性删除）
     *
     * @return 是否清理成功
     */
    private static boolean cleanExpiredKey(Object key) {
        Long expireTime = expireMap.get(key);
        if (expireTime != null && System.currentTimeMillis() > expireTime) {
            // 已过期：删除存储和过期记录
            storage.remove(key);
            expireMap.remove(key);
            return true;
        }
        return false;
    }

    /**
     * 定期清理过期键（定期删除策略）
     * 模拟Redis的近似LRU策略：每次扫描部分键，过期则删除，超过比例则继续扫描
     */
    private static void cleanExpiredKeys() {
        if (expireMap.isEmpty()) return;

        int scanned = 0;
        int expiredCount = 0;
        int maxScan = 100; // 每次最多扫描100个键，避免阻塞
        double expireRatio = 0.25; // 过期比例超过25%则继续扫描

        Iterator<Object> iterator = expireMap.keySet().iterator();
        while (iterator.hasNext() && scanned < maxScan) {
            Object key = iterator.next();
            scanned++;
            if (System.currentTimeMillis() > expireMap.getOrDefault(key, 0L)) {
                // 过期：删除
                storage.remove(key);
                iterator.remove(); // 安全删除当前元素
                expiredCount++;
            }
        }

        // 如果过期比例过高，递归继续扫描
        if (scanned > 0 && (double) expiredCount / scanned > expireRatio) {
            cleanExpiredKeys();
        }
    }

    /**
     * 设置键的过期时间（秒）
     */
    public static void expire(String key, long seconds, TimeUnit unit) {
        if (seconds <= 0) {
            del(key); // 过期时间<=0直接删除
            return;
        }
        if (storage.containsKey(key)) { // 仅对存在的键设置过期
            expireMap.put(key, System.currentTimeMillis() + unit.toMillis(seconds));
        }
    }

    /**
     * 设置键的过期时间（毫秒，更精确）
     */
    public static void expire(String key, long milliseconds) {
        if (milliseconds <= 0) {
            del(key);
            return;
        }
        if (storage.containsKey(key)) {
            expireMap.put(key, System.currentTimeMillis() + milliseconds);
        }
    }


    // ========================== String 类型操作 ==========================

    /**
     * 设置String键值对
     */
    public static void set(String key, String value) {
        storage.put(key, value);
        expireMap.remove(key); // 覆盖设置时清除过期时间
    }

    /**
     * 获取String值（自动检查过期）
     */
    public static String get(String key) {
        if (cleanExpiredKey(key)) return null; // 已过期返回null
        Object value = storage.get(key);
        return (value instanceof String) ? (String) value : null;
    }

    /**
     * 删除键（通用方法，所有类型适用）
     */
    public static void del(String key) {
        storage.remove(key);
        expireMap.remove(key);
    }

    /**
     * String自增（+1，原子操作）
     */
    public static Long incr(String key) {
        return incrBy(key, 1);
    }

    /**
     * String自减（-1，原子操作）
     */
    public static Long decr(String key) {
        return incrBy(key, -1);
    }

    /**
     * 指定步长增减（原子操作，线程安全）
     */
    public static Long incrBy(String key, long step) {
        if (cleanExpiredKey(key)) { // 先检查过期
            // 过期键视为不存在，从0开始增减
            storage.put(key, String.valueOf(step));
            return step;
        }
        // 使用compute确保原子性（读-改-写整体同步）
        Object result = storage.compute(key, (k, value) -> {
            if (value == null) {
                return String.valueOf(step);
            }
            if (!(value instanceof String)) {
                throw new RuntimeException("Key is not a String");
            }
            try {
                long num = Long.parseLong((String) value) + step;
                return String.valueOf(num);
            } catch (NumberFormatException e) {
                throw new RuntimeException("String value is not a number");
            }
        });
        return Long.parseLong(result.toString());
    }

    /**
     * 获取String值长度
     */
    public static int strLen(String key) {
        String value = get(key);
        return (value != null) ? value.length() : 0;
    }


    // ========================== Hash 类型操作 ==========================

    /**
     * 向Hash添加字段（原子操作）
     */
    public static void hashSet(String key, String field, String value) {
        getOrCreateHash(key).put(field, value);
    }

    /**
     * 获取Hash字段值（自动检查过期）
     */
    public static String hashGet(String key, String field) {
        ConcurrentHashMap<String, String> hash = getHash(key);
        return (hash != null) ? hash.get(field) : null;
    }

    /**
     * 删除Hash字段（原子操作）
     */
    public static void hashDel(String key, String field) {
        ConcurrentHashMap<String, String> hash = getHash(key);
        if (hash != null) {
            hash.remove(field);
        }
    }

    /**
     * 获取Hash字段数量
     */
    public static int hashLen(String key) {
        ConcurrentHashMap<String, String> hash = getHash(key);
        return (hash != null) ? hash.size() : 0;
    }

    /**
     * Hash字段值自增（原子操作）
     */
    public static Long hashIncrBy(String key, String field, long step) {
        ConcurrentHashMap<String, String> hash = getOrCreateHash(key);
        // 用compute确保原子性
        String result = hash.compute(field, (f, valueStr) -> {
            long value = (valueStr == null) ? 0 : Long.parseLong(valueStr);
            return String.valueOf(value + step);
        });
        return Long.parseLong(result);
    }

    // 辅助：获取或创建线程安全的Hash
    private static ConcurrentHashMap<String, String> getOrCreateHash(String key) {
        if (cleanExpiredKey(key)) { // 检查过期
            // 过期后重建Hash
            ConcurrentHashMap<String, String> newHash = new ConcurrentHashMap<>();
            Object old = storage.putIfAbsent(key, newHash);
            return (old == null) ? newHash : (ConcurrentHashMap<String, String>) old;
        }
        Object obj = storage.get(key);
        if (obj == null) {
            ConcurrentHashMap<String, String> newHash = new ConcurrentHashMap<>();
            Object old = storage.putIfAbsent(key, newHash);
            return (old == null) ? newHash : (ConcurrentHashMap<String, String>) old;
        }
        if (!(obj instanceof ConcurrentHashMap)) {
            throw new RuntimeException("Key is not a Hash");
        }
        return (ConcurrentHashMap<String, String>) obj;
    }

    // 辅助：获取Hash（不创建）
    private static ConcurrentHashMap<String, String> getHash(String key) {
        if (cleanExpiredKey(key)) return null;
        Object obj = storage.get(key);
        return (obj instanceof ConcurrentHashMap) ? (ConcurrentHashMap<String, String>) obj : null;
    }


    // ========================== Set 类型操作 ==========================

    /**
     * 向Set添加元素（原子操作）
     */
    public static void setAdd(String key, String member) {
        getOrCreateSet(key).add(member);
    }

    /**
     * 获取Set所有元素
     */
    public static Set<String> setMembers(String key) {
        Set<String> set = getSet(key);
        return (set != null) ? new HashSet<>(set) : new HashSet<>(); // 返回副本避免并发修改异常
    }

    /**
     * 从Set删除元素（原子操作）
     */
    public static void setRemove(String key, String member) {
        Set<String> set = getSet(key);
        if (set != null) {
            set.remove(member);
        }
    }

    /**
     * 获取Set元素数量
     */
    public static int setCard(String key) {
        Set<String> set = getSet(key);
        return (set != null) ? set.size() : 0;
    }

    // 辅助：获取或创建线程安全的Set（基于ConcurrentHashMap实现）
    private static Set<String> getOrCreateSet(String key) {
        if (cleanExpiredKey(key)) {
            Set<String> newSet = Collections.newSetFromMap(new ConcurrentHashMap<>());
            Object old = storage.putIfAbsent(key, newSet);
            return (old == null) ? newSet : (Set<String>) old;
        }
        Object obj = storage.get(key);
        if (obj == null) {
            Set<String> newSet = Collections.newSetFromMap(new ConcurrentHashMap<>());
            Object old = storage.putIfAbsent(key, newSet);
            return (old == null) ? newSet : (Set<String>) old;
        }
        if (!(obj instanceof Set)) {
            throw new RuntimeException("Key is not a Set");
        }
        return (Set<String>) obj;
    }

    // 辅助：获取Set（不创建）
    private static Set<String> getSet(String key) {
        if (cleanExpiredKey(key)) return null;
        Object obj = storage.get(key);
        return (obj instanceof Set) ? (Set<String>) obj : null;
    }


    // ========================== ZSet 类型操作 ==========================

    /**
     * 向ZSet添加元素（带分数，原子操作）
     */
    public static void zSetAdd(String key, double score, String member) {
        getOrCreateZSet(key).put(member, score);
    }

    /**
     * 获取ZSet指定范围元素（按分数升序）
     */
    public static List<String> zSetRange(String key, int start, int end) {
        ConcurrentHashMap<String, Double> zset = getZSet(key);
        if (zset == null || zset.isEmpty()) {
            return new ArrayList<>();
        }
        // 排序并处理索引（线程安全：先获取快照再排序）
        List<Map.Entry<String, Double>> sorted = new ArrayList<>(zset.entrySet()).stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());
        int size = sorted.size();
        start = adjustIndex(start, size);
        end = adjustIndex(end, size);
        return (start > end) ? new ArrayList<>() :
                sorted.subList(start, end + 1).stream()
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());
    }

    /**
     * 从ZSet删除元素（原子操作）
     */
    public static void zSetRemove(String key, String member) {
        ConcurrentHashMap<String, Double> zset = getZSet(key);
        if (zset != null) {
            zset.remove(member);
        }
    }

    /**
     * 获取ZSet元素数量
     */
    public static int zSetCard(String key) {
        ConcurrentHashMap<String, Double> zset = getZSet(key);
        return (zset != null) ? zset.size() : 0;
    }

    /**
     * ZSet元素分数自增（原子操作）
     */
    public static Double zincrby(String key, double increment, String member) {
        return getOrCreateZSet(key).merge(member, increment, Double::sum);
    }

    // 辅助：获取或创建线程安全的ZSet
    private static ConcurrentHashMap<String, Double> getOrCreateZSet(String key) {
        if (cleanExpiredKey(key)) {
            ConcurrentHashMap<String, Double> newZSet = new ConcurrentHashMap<>();
            Object old = storage.putIfAbsent(key, newZSet);
            return (old == null) ? newZSet : (ConcurrentHashMap<String, Double>) old;
        }
        Object obj = storage.get(key);
        if (obj == null) {
            ConcurrentHashMap<String, Double> newZSet = new ConcurrentHashMap<>();
            Object old = storage.putIfAbsent(key, newZSet);
            return (old == null) ? newZSet : (ConcurrentHashMap<String, Double>) old;
        }
        if (!(obj instanceof ConcurrentHashMap)) {
            throw new RuntimeException("Key is not a ZSet");
        }
        return (ConcurrentHashMap<String, Double>) obj;
    }

    // 辅助：获取ZSet（不创建）
    private static ConcurrentHashMap<String, Double> getZSet(String key) {
        if (cleanExpiredKey(key)) return null;
        Object obj = storage.get(key);
        return (obj instanceof ConcurrentHashMap) ? (ConcurrentHashMap<String, Double>) obj : null;
    }


    // ========================== List 类型操作 ==========================

    /**
     * 向List左侧（头部）添加元素（原子操作）
     */
    public static void listLeftPush(String key, String value) {
        getOrCreateList(key).addFirst(value);
    }

    /**
     * 向List右侧（尾部）添加元素（原子操作）
     */
    public static void listRightPush(String key, String value) {
        getOrCreateList(key).addLast(value);
    }

    /**
     * 获取List指定范围元素
     */
    public static List<String> listRange(String key, int start, int end) {
        ConcurrentLinkedDeque<String> list = getList(key);
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        // 转为ArrayList处理索引（避免并发修改）
        List<String> snapshot = new ArrayList<>(list);
        int size = snapshot.size();
        start = adjustIndex(start, size);
        end = adjustIndex(end, size);
        return (start > end) ? new ArrayList<>() : snapshot.subList(start, end + 1);
    }

    /**
     * 从List左侧（头部）删除并返回元素（原子操作）
     */
    public static String listLeftPop(String key) {
        ConcurrentLinkedDeque<String> list = getList(key);
        return (list != null) ? list.pollFirst() : null;
    }

    /**
     * 从List右侧（尾部）删除并返回元素（原子操作）
     */
    public static String listRightPop(String key) {
        ConcurrentLinkedDeque<String> list = getList(key);
        return (list != null) ? list.pollLast() : null;
    }

    /**
     * 获取List长度
     */
    public static int listLen(String key) {
        ConcurrentLinkedDeque<String> list = getList(key);
        return (list != null) ? list.size() : 0;
    }

    // 辅助：获取或创建线程安全的List（双向队列）
    private static ConcurrentLinkedDeque<String> getOrCreateList(String key) {
        if (cleanExpiredKey(key)) {
            ConcurrentLinkedDeque<String> newList = new ConcurrentLinkedDeque<>();
            Object old = storage.putIfAbsent(key, newList);
            return (old == null) ? newList : (ConcurrentLinkedDeque<String>) old;
        }
        Object obj = storage.get(key);
        if (obj == null) {
            ConcurrentLinkedDeque<String> newList = new ConcurrentLinkedDeque<>();
            Object old = storage.putIfAbsent(key, newList);
            return (old == null) ? newList : (ConcurrentLinkedDeque<String>) old;
        }
        if (!(obj instanceof ConcurrentLinkedDeque)) {
            throw new RuntimeException("Key is not a List");
        }
        return (ConcurrentLinkedDeque<String>) obj;
    }

    // 辅助：获取List（不创建）
    private static ConcurrentLinkedDeque<String> getList(String key) {
        if (cleanExpiredKey(key)) return null;
        Object obj = storage.get(key);
        return (obj instanceof ConcurrentLinkedDeque) ? (ConcurrentLinkedDeque<String>) obj : null;
    }


    // ========================== 通用工具方法 ==========================

    /**
     * 调整索引（支持负数，如-1表示最后一个元素）
     */
    private static int adjustIndex(int index, int size) {
        if (size == 0) return 0;
        if (index < 0) {
            index = size + index; // 负数转为正数索引（-1 → size-1）
        }
        return Math.max(0, Math.min(index, size - 1)); // 限制在有效范围内
    }

    /**
     * 判断键是否存在（自动检查过期）
     */
    public static boolean exists(String key) {
        return !cleanExpiredKey(key) && storage.containsKey(key);
    }


    // ========================== 多线程测试 ==========================
    public static void main(String[] args) throws InterruptedException {
        // 1. 测试过期时间
        LocalCacheUtils.set("expireKey", "test");
        LocalCacheUtils.expire("expireKey", 2); // 2秒后过期
        System.out.println("过期前 exists: " + LocalCacheUtils.exists("expireKey")); // true
        Thread.sleep(3000);
        System.out.println("过期后 exists: " + LocalCacheUtils.exists("expireKey")); // false

        // 2. 测试多线程自增（验证线程安全）
        int threadCount = 10;
        int incrementsPerThread = 1000;
        CountDownLatch latch = new CountDownLatch(threadCount);
        LocalCacheUtils.set("counter", "0");

        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    LocalCacheUtils.incr("counter");
                }
                latch.countDown();
            }).start();
        }

        latch.await();
        System.out.println("多线程自增结果: " + LocalCacheUtils.get("counter")); // 预期10000

        // 3. 测试Hash多线程操作
        LocalCacheUtils.hashSet("threadHash", "num", "0");
        CountDownLatch hashLatch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    LocalCacheUtils.hashIncrBy("threadHash", "num", 1);
                }
                hashLatch.countDown();
            }).start();
        }
        hashLatch.await();
        System.out.println("Hash多线程自增结果: " + LocalCacheUtils.hashGet("threadHash", "num")); // 预期10000
    }

}
