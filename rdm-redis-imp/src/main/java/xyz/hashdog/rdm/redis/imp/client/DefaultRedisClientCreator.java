package xyz.hashdog.rdm.redis.imp.client;

import xyz.hashdog.rdm.redis.RedisClient;
import xyz.hashdog.rdm.redis.RedisConfig;

/**
 * @Author th
 * @Date 2023/7/18 12:47
 */
public class DefaultRedisClientCreator implements RedisClientCreator{
    @Override
    public RedisClient create(RedisConfig redisConfig) {
        return new JedisPoolClient(redisConfig);
    }
}
