package xyz.hashdog.rdm.redis.imp;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Author th
 * @Date 2023/7/18 13:12
 */
public class Constant {

    /**
     * jedis通用连接池配置
     */
    public static final  GenericObjectPoolConfig<Jedis> POOL_CONFIG ;
    static {
        // 创建Jedis连接池配置对象
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(10); // 设置连接池中的最大连接数
        poolConfig.setMaxIdle(2); // 设置连接池中的最大空闲连接数
        poolConfig.setTestOnBorrow(true); // 设置Jedis连接池的测试连接是否有效
        POOL_CONFIG=poolConfig;
    }
}
