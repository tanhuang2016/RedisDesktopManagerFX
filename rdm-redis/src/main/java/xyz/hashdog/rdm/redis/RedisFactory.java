package xyz.hashdog.rdm.redis;

/**
 * @author th
 * @version 1.0.0
 * @since 2023/7/18 9:49
 */
public interface RedisFactory {


    /**
     * 获取redis上下文
     * @param redisConfig
     * @return
     */
    RedisContext createRedisContext(RedisConfig redisConfig);
}
