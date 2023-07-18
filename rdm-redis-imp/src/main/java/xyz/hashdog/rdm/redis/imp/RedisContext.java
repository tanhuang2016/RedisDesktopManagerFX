package xyz.hashdog.rdm.redis.imp;

import xyz.hashdog.rdm.redis.RedisClient;
import xyz.hashdog.rdm.redis.RedisConfig;
import xyz.hashdog.rdm.redis.imp.client.DefaultRedisClientCreator;
import xyz.hashdog.rdm.redis.imp.client.RedisClientCreator;

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
    /**
     * redis客户端
     */
    private RedisClient redisClient;


    public RedisContext(RedisConfig redisConfig) {
        this.redisConfig=redisConfig;
        this.redisClientCreator=new DefaultRedisClientCreator();
    }

    /**
     * 委派给 RedisClientCreator 进行redis客户端的创建
     * @return
     */
    @Override
    public RedisClient getRedisClient() {
        if(redisClient==null){
            redisClient=redisClientCreator.create(redisConfig);
        }
        return redisClient;
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
}
