package xyz.hashdog.rdm.redis.imp;

import xyz.hashdog.rdm.redis.RedisConfig;
import xyz.hashdog.rdm.redis.client.RedisClient;
import xyz.hashdog.rdm.redis.imp.client.DefaultRedisClientCreator;
import xyz.hashdog.rdm.redis.imp.client.RedisClientCreator;

import java.io.IOException;

/**
 * @Author th
 * @Date 2023/7/18 10:53
 */
public class RedisContext implements xyz.hashdog.rdm.redis.RedisContext{
    /**
     * redis配置
     */
    private RedisConfig redisConfig;
    /**
     * redis客户创建器
     */
    private RedisClientCreator redisClientCreator;




    public RedisContext(RedisConfig redisConfig) {
        this.redisConfig=redisConfig;
        this.redisClientCreator=new DefaultRedisClientCreator();
    }

    /**
     * 委派给 RedisClientCreator 进行redis客户端的创建
     * @return
     */
    @Override
    public RedisClient newRedisClient() {
        return redisClientCreator.create(redisConfig);
    }

    @Override
    public RedisConfig getRedisConfig() {
        return redisConfig;
    }



    /**
     * 获取创建器,可以进行创建多个客户端实例
     * @return
     */
    public RedisClientCreator getRedisClientCreator() {
        return redisClientCreator;
    }

    /**
     * 设置创建器,可以自定义创建器
     * @param redisClientCreator
     */
    public void setRedisClientCreator(RedisClientCreator redisClientCreator) {
        this.redisClientCreator = redisClientCreator;
    }

    @Override
    public void close() throws IOException {
        redisClientCreator.close();
    }
}
