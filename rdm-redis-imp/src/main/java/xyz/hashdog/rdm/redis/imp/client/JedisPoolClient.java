package xyz.hashdog.rdm.redis.imp.client;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import xyz.hashdog.rdm.common.util.Tutil;
import xyz.hashdog.rdm.redis.RedisClient;
import xyz.hashdog.rdm.redis.RedisConfig;
import xyz.hashdog.rdm.redis.imp.console.RedisConsole;
import xyz.hashdog.rdm.redis.imp.Constant;
import xyz.hashdog.rdm.redis.imp.console.SocketAcquirer;

import java.net.Socket;
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

    /**
     * 传了一个SocketAcquirer匿名内部类实现
     * SocketAcquirer 每次都是从pool获取最新的socket
     * 但是使用socket后没关流,如果有必要可以用warp包装socket多传1个回调函数,
     * 进行cmd调用完之后关流
     * @return
     */
    @Override
    public RedisConsole getRedisConsole() {
        return new RedisConsole(() -> {
            try (Jedis jedis = pool.getResource()) {
                return Tutil.getField(jedis.getConnection(), "socket");
            }
        });
    }
}
