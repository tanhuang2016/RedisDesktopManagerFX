package xyz.hashdog.rdm.ui.common;

import xyz.hashdog.rdm.redis.Message;
import xyz.hashdog.rdm.redis.client.RedisClient;
import xyz.hashdog.rdm.ui.entity.PassParameter;
import xyz.hashdog.rdm.ui.exceptions.GeneralException;
import xyz.hashdog.rdm.ui.handler.NewKeyHandler;

/**
 * @author th
 * @version 1.0.0
 * @since 2023/7/23 0:42
 */
public enum RedisDataTypeEnum {
    STRING("String","/fxml/StringTypeView.fxml", PassParameter.STRING,((redisClient, db, key, ttl) -> {
        checkDB(redisClient,db);
        redisClient.set(key, Applications.DEFUALT_VALUE);
        checkTTL(redisClient,ttl,key);
        return new Message(true);
    }),KeyTypeTagEnum.STRING),
    JSON("ReJSON-RL","/fxml/JsonTypeView.fxml", PassParameter.JSON,((redisClient, db, key, ttl) -> {
        checkDB(redisClient,db);
        redisClient.jsonSet(key,Applications.DEFUALT_JSON_VALUE);
        checkTTL(redisClient,ttl,key);
        return new Message(true);
    }),KeyTypeTagEnum.JSON),
    STREAM("Stream","/fxml/JsonTypeView.fxml", PassParameter.JSON,((redisClient, db, key, ttl) -> {
        checkDB(redisClient,db);
        redisClient.jsonSet(key,Applications.DEFUALT_JSON_VALUE);
        checkTTL(redisClient,ttl,key);
        return new Message(true);
    }),KeyTypeTagEnum.STREAM),
    HASH("Hash","/fxml/HashTypeView.fxml", PassParameter.HASH,((redisClient, db, key, ttl) -> {
        checkDB(redisClient,db);
        redisClient.hsetnx(key, Applications.DEFUALT_VALUE,Applications.DEFUALT_VALUE);
        checkTTL(redisClient,ttl,key);
        return new Message(true);
    }),KeyTypeTagEnum.HASH),
    LIST("List","/fxml/ListTypeView.fxml", PassParameter.LIST,((redisClient, db, key, ttl) -> {
        checkDB(redisClient,db);
        redisClient.lpush(key, Applications.DEFUALT_VALUE);
        checkTTL(redisClient,ttl,key);
        return new Message(true);
    }),KeyTypeTagEnum.LIST),
    SET("Set","/fxml/SetTypeView.fxml", PassParameter.SET,((redisClient, db, key, ttl) -> {
        checkDB(redisClient,db);
        redisClient.sadd(key, Applications.DEFUALT_VALUE);
        checkTTL(redisClient,ttl,key);
        return new Message(true);
    }),KeyTypeTagEnum.SET),
    ZSET("Zset","/fxml/ZsetTypeView.fxml", PassParameter.ZSET,((redisClient, db, key, ttl) -> {
        checkDB(redisClient,db);
        redisClient.zadd(key,0, Applications.DEFUALT_VALUE);
        checkTTL(redisClient,ttl,key);
        return new Message(true);
    }),KeyTypeTagEnum.ZSET),
    ;

    /**
     * 检查设置有效期
     * @param redisClient
     * @param ttl
     * @param key
     */
    private static void checkTTL(RedisClient redisClient, long ttl, String key) {
        if(ttl>0){
            redisClient.expire(key,ttl);
        }
    }


    /**
     *检查设置db
     * @param redisClient
     * @param db
     */
    private static void checkDB(RedisClient redisClient, int db) {
        if(redisClient.getDb()!=db){
            redisClient.select(db);
        }
    }


    public String type;
    public String fxml;
    public int tabType;
    public NewKeyHandler newKeyHandler;
    public KeyTypeTagEnum tagEnum;
    RedisDataTypeEnum(String type,String fxml,int tabType,NewKeyHandler newKeyHandler,KeyTypeTagEnum keyTypeTagEnum) {
        this.type=type;
        this.fxml=fxml;
        this.tabType=tabType;
        this.newKeyHandler=newKeyHandler;
        this.tagEnum=keyTypeTagEnum;
    }

    /**
     * 根据类型字符串获取
     * @param type
     * @return
     */
    public static RedisDataTypeEnum getByType(String type) {
        //key不存在，返回的是none
        if("none".equals(type)){
            throw new GeneralException("Key with this name does not exist.");
        }
        for (RedisDataTypeEnum value : values()) {
            if(value.type.equalsIgnoreCase(type)){
                return value;
            }
        }
        throw new GeneralException("This type is not supported "+type);
    }
}
