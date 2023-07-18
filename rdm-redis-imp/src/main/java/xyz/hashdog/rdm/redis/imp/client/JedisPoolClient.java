package xyz.hashdog.rdm.redis.imp.client;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import xyz.hashdog.rdm.redis.RedisClient;
import xyz.hashdog.rdm.redis.RedisConfig;
import xyz.hashdog.rdm.redis.imp.Constant;

import java.util.Set;

/**
 * 单机版jedis,用JedisPool包装实现
 *
 * @Author th
 * @Date 2023/7/18 12:59
 */
public class JedisPoolClient implements RedisClient {
    /**
     * jedis
     */
    private JedisPool pool;

    public JedisPoolClient(RedisConfig redisConfig) {

        this.pool = new JedisPool(Constant.POOL_CONFIG, redisConfig.getHost(), redisConfig.getPort());
    }

    @Override
    public Set<String> keys(String pattern) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.keys(pattern);
        }
    }
}
