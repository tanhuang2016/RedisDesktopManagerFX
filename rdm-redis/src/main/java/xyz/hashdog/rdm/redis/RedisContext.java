package xyz.hashdog.rdm.redis;

import xyz.hashdog.rdm.redis.client.RedisClient;
import xyz.hashdog.rdm.redis.exceptions.RedisException;

/**
 * redis上下文,提供了对redis操作及相关信息所有的包装
 * 多实例,可以由RedisFactory创建
 * @Author th
 * @Date 2023/7/18 10:48
 */
public interface RedisContext {
    /**
     * redis客户端获取,用于操作redis
     * redis客户端的获取,内部是单例实现,每次都是获取的同一个
     * @return
     */
    RedisClient getRedisClient();

    /**
     * 获取redis的配置
     * @return
     */
    RedisConfig getRedisConfig();

    /**
     * 测试能否连接
     * @return
     */
    Message testConnect();
}
