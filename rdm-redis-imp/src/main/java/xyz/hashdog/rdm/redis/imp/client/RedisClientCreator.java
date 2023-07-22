package xyz.hashdog.rdm.redis.imp.client;

import xyz.hashdog.rdm.redis.client.RedisClient;
import xyz.hashdog.rdm.redis.RedisConfig;

import java.io.Closeable;

/**
 *
 * RedisClinent创建器
 * @Author th
 * @Date 2023/7/18 12:45
 */
public interface RedisClientCreator extends Closeable {
    /**
     * 创建redis客户端
     * @param redisConfig
     * @return
     */
    RedisClient create(RedisConfig redisConfig);
}
