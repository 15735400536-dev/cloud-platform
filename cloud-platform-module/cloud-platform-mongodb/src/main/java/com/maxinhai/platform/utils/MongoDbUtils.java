package com.maxinhai.platform.utils;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName：MongoDbUtils
 * @Author: XinHai.Ma
 * @Date: 2025/9/3 14:03
 * @Description: MongoDB操作工具类
 */
@Component
public class MongoDbUtils {

    @Resource
    private MongoClient mongoClient;  // 底层客户端，用于数据库级操作

    /**
     * 获取所有数据库名称
     */
    public List<String> getAllDatabases() {
        List<String> dbNames = new ArrayList<>();
        // 通过MongoClient获取所有数据库
        mongoClient.listDatabaseNames().forEach(dbNames::add);
        return dbNames;
    }

    /**
     * 创建数据库（MongoDB特性：插入数据时自动创建数据库）
     *
     * @param dbName 数据库名称
     * @return 对应数据库的MongoTemplate（方便后续操作）
     */
    public MongoTemplate createDatabase(String dbName) {
        // 1. 获取数据库对象（此时未实际创建）
        MongoDatabase database = mongoClient.getDatabase(dbName);

        // 2. 插入一条初始化数据，触发数据库实际创建（MongoDB特性）
        database.getCollection("init_flag").insertOne(new org.bson.Document("init", "success"));

        // 3. 返回该数据库的MongoTemplate，方便后续操作
        return new MongoTemplate(mongoClient, dbName);
    }

    /**
     * 修改数据库名称（MongoDB无直接重命名API，需通过复制+删除实现）
     *
     * @param oldDbName 原数据库名称
     * @param newDbName 新数据库名称
     */
    public void renameDatabase(String oldDbName, String newDbName) {
        // 1. 检查原数据库是否存在
        if (!getAllDatabases().contains(oldDbName)) {
            throw new RuntimeException("原数据库不存在：" + oldDbName);
        }

        // 2. 获取原数据库和新数据库的操作对象
        MongoDatabase oldDb = mongoClient.getDatabase(oldDbName);
        MongoTemplate newDbTemplate = new MongoTemplate(mongoClient, newDbName);

        // 3. 复制所有集合到新数据库
        oldDb.listCollectionNames().forEach(collectionName -> {
            // 读取原集合所有文档，插入新数据库的同名集合
            newDbTemplate.insert(
                    oldDb.getCollection(collectionName).find(),
                    collectionName
            );
        });

        // 4. 删除原数据库
        dropDatabase(oldDbName);
    }

    /**
     * 删除数据库
     *
     * @param dbName 数据库名称
     */
    public void dropDatabase(String dbName) {
        mongoClient.getDatabase(dbName).drop();
    }

    /**
     * 切换数据库（返回对应数据库的MongoTemplate）
     *
     * @param dbName 目标数据库名称
     */
    public MongoTemplate getMongoTemplate(String dbName) {
        // 若数据库不存在，会在首次插入数据时自动创建
        return new MongoTemplate(mongoClient, dbName);
    }

    /**
     * 切换数据库
     *
     * @param dbName 要切换到的数据库名称
     * @return 切换后的数据库对象
     */
    public MongoDatabase switchDatabase(String dbName) {
        MongoDatabase database = mongoClient.getDatabase(dbName);
        System.out.println("已切换到数据库: " + dbName);
        return database;
    }

    /**
     * 在指定数据库中创建文档（集合）
     *
     * @param dbName         数据库名称
     * @param collectionName 集合（文档表）名称
     * @param document       要插入的文档数据
     */
    public void createDocument(String dbName, String collectionName, Map<String, Object> document) {
        // 切换到目标数据库
        MongoDatabase database = switchDatabase(dbName);

        // 插入文档（集合不存在时会自动创建）
        database.getCollection(collectionName).insertOne(new org.bson.Document(document));
        System.out.println("在数据库 '" + dbName + "' 的集合 '" + collectionName + "' 中创建文档成功");
    }

    /**
     * 使用MongoTemplate操作指定数据库（Spring Data MongoDB推荐方式）
     *
     * @param dbName 数据库名称
     * @return 对应数据库的MongoTemplate实例
     */
    public MongoTemplate getMongoTemplateByDbName(String dbName) {
        // 通过MongoClient创建指定数据库的MongoTemplate
        return new MongoTemplate(mongoClient, dbName);
    }

}
