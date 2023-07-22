package xyz.hashdog.rdm.redis.imp.client;

import redis.clients.jedis.JedisPool;
import xyz.hashdog.rdm.redis.client.RedisClient;
import xyz.hashdog.rdm.redis.RedisConfig;
import xyz.hashdog.rdm.redis.imp.Constant;

import java.io.IOException;

/**
 * @Author th
 * @Date 2023/7/18 12:47
 */
public class DefaultRedisClientCreator implements RedisClientCreator{

    /**
     * jedis
     */
    private JedisPool pool;
    /**
     * 根据RedisConfig 判断创建什么类型的redis客户端
     * @param redisConfig
     * @return
     */
    @Override
    public RedisClient create(RedisConfig redisConfig) {
        this.pool=new JedisPool(Constant.POOL_CONFIG, redisConfig.getHost(), redisConfig.getPort());
        return new JedisPoolClient(pool);
    }

    @Override
    public void close() throws IOException {
        if(pool!=null){
            pool.close();
        }
    }
}
