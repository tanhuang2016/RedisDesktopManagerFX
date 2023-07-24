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
    public final  static int CONSOLE=1;
    public final  static int STRING=2;
    public static final int REDIS = 3;

    private int tabType;
    private int db;
    private StringProperty key=new SimpleStringProperty();
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
}
