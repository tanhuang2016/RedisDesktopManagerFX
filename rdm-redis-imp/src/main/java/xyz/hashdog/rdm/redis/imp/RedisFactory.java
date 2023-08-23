package xyz.hashdog.rdm.redis.imp;

import xyz.hashdog.rdm.redis.RedisConfig;

/**
 * @author th
 * @version 1.0.0
 * @since 2023/7/18 10:32
 */
public class RedisFactory implements xyz.hashdog.rdm.redis.RedisFactory {

    @Override
    public xyz.hashdog.rdm.redis.RedisContext createRedisContext(RedisConfig redisConfig) {
        return new RedisContext(redisConfig);
    }
}
