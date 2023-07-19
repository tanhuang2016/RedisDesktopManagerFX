package xyz.hashdog.rdm.redis.imp.client;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.commands.JedisCommands;
import xyz.hashdog.rdm.common.util.Tutil;
import xyz.hashdog.rdm.redis.client.RedisClient;
import xyz.hashdog.rdm.redis.RedisConfig;
import xyz.hashdog.rdm.redis.imp.console.RedisConsole;
import xyz.hashdog.rdm.redis.imp.Constant;
import java.util.Set;
import java.util.function.Function;

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

    /**
     * 执行命令的封装
     * @param execCommand
     * @return
     * @param <R>
     */
    private  <R> R execut( Function<Jedis, R> execCommand) {
        return Tutil.execut(pool.getResource(),execCommand,Jedis::close);
    }

    @Override
    public Set<String> keys(String pattern) {
        return execut(jedis->jedis.keys(pattern));
    }

    @Override
    public String type(String key) {
        return Tutil.execut(pool.getResource(),jedis->jedis.type(key),Jedis::close);
    }

    @Override
    public long ttl(String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.ttl(key);
        }
    }

    @Override
    public String ping() {
        return execut(Jedis::ping);

    }
    @Override
    public String info() {
        try (Jedis jedis = pool.getResource()) {
            return jedis.info();
        }
    }
    @Override
    public String rename(String oldkey, String newkey) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.rename(oldkey,newkey);
        }
    }
    @Override
    public long expire(String key, long seconds) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.expire(key,seconds);
        }
    }
    @Override
    public boolean exists(String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.exists(key);
        }
    }
    @Override
    public long del(String key) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.del(key);
        }
    }
    @Override
    public long persist(String key) {
        try (Jedis jedis = pool.getResource()) {
            JedisCommands a=jedis;
            return jedis.persist(key);
        }
    }

    @Override
    public String restore(String key, long ttl, byte[] serializedValue) {
        return execut(jedis->jedis.restore(key,ttl,serializedValue));
    }
    @Override
    public byte[] dump(String key) {
        return execut(jedis->jedis.dump(key));
    }
    @Override
    public String flushDB() {
        return execut(jedis->jedis.flushDB());
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
