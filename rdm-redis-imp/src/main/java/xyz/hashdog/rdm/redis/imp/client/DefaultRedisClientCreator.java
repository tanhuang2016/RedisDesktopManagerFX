package xyz.hashdog.rdm.redis.imp.client;

import xyz.hashdog.rdm.redis.client.RedisClient;
import xyz.hashdog.rdm.redis.RedisConfig;

/**
 * @Author th
 * @Date 2023/7/18 12:47
 */
public class DefaultRedisClientCreator implements RedisClientCreator{
    /**
     * 根据RedisConfig 判断创建什么类型的redis客户端
     *  todo
     * @param redisConfig
     * @return
     */
    @Override
    public RedisClient create(RedisConfig redisConfig) {
        return new JedisPoolClient(redisConfig);
    }
}
