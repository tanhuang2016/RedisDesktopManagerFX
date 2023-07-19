package xyz.hashdog.rdm.redis.client;

import java.util.Set;

/**
 * redis客户端操作
 * 相关redis操作命令都在这儿
 * @Author th
 * @Date 2023/7/18 11:03
 */
public interface RedisClient {

    /**
     * keys 模糊查新的命令
     * @param pattern
     * @return
     */
    Set<String> keys(String pattern);

    /**
     * 获取key的类型
     * @param key
     * @return
     */
    String type(String key);

    /**
     * key的存活时间是多少秒
     * @param key
     * @return
     */
    long ttl(String key);

    /**
     * ping
     * @return
     */
    String ping();

    /**
     * redis信息
     * @return
     */
    String info();

    /**
     * key 重命名
     * @param oldkey
     * @param newkey
     * @return
     */
    String rename(String oldkey, String newkey);

    /**
     * 控制台交互器
     * @return
     */
    RedisConsole getRedisConsole();

    /**
     * 设置key的时长
     * @param key
     * @param seconds
     * @return
     */
    long expire(String key, long seconds);

    /**
     * key是否存在
     * @param key
     * @return
     */
    boolean exists(String key);

    /**
     * 删除key
     * @param key
     * @return
     */
    long del(String key);

    /**
     * 设置key永不过期
     * @param key
     * @return
     */
    long persist(String key);

    /**
     * 序列化
     * @param key
     * @return
     */
    byte[] dump(String key);

    /**
     * 反序列化
     * @param key
     * @param ttl
     * @param serializedValue
     * @return
     */
    String restore(String key, long ttl, byte[] serializedValue);

    /**
     * 清空当前库
     * @return
     */
    String flushDB();


}
