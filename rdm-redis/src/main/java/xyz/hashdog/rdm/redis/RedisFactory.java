package xyz.hashdog.rdm.redis;

/**
 * @Author th
 * @Date 2023/7/18 9:49
 */
public interface RedisFactory {


    /**
     * 获取redis上下文
     * @param redisConfig
     * @return
     */
    RedisContext createRedisContext(RedisConfig redisConfig);
}
