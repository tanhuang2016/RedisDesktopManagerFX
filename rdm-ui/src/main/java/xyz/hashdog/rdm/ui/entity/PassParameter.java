package xyz.hashdog.rdm.ui.entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import xyz.hashdog.rdm.redis.RedisContext;
import xyz.hashdog.rdm.redis.client.RedisClient;

/**
 * 参数传递
 * 父向子传递
 * @Author th
 * @Date 2023/7/23 22:28
 */
public class PassParameter {
    public final  static int CONSOLE=2;
    public final  static int STRING=3;
    public final  static int HASH=5;
    public final  static int LIST=6;
    public static final int REDIS = 4;
    public static final int NONE = 1;

    private int tabType;
    private int db;
    private StringProperty key=new SimpleStringProperty();
    private String keyType;
    private RedisContext redisContext;
    private RedisClient redisClient;


    public PassParameter(int tabType) {
        this.tabType = tabType;
    }


    public RedisContext getRedisContext() {
        return redisContext;
    }

    public void setRedisContext(RedisContext redisContext) {
        this.redisContext = redisContext;
    }

    public RedisClient getRedisClient() {
        return redisClient;
    }

    public void setRedisClient(RedisClient redisClient) {
        this.redisClient = redisClient;
    }

    public int getDb() {
        return db;
    }

    public void setDb(int db) {
        this.db = db;
    }

    public int getTabType() {
        return tabType;
    }

    public void setTabType(int tabType) {
        this.tabType = tabType;
    }

    public String getKey() {
        return key.get();
    }

    public StringProperty keyProperty() {
        return key;
    }

    public void setKey(String key) {
        this.key.set(key);
    }

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }
}
