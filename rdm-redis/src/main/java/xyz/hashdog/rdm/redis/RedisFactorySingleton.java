package xyz.hashdog.rdm.redis;

import xyz.hashdog.rdm.common.util.TUtil;
import xyz.hashdog.rdm.redis.RedisFactory;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * 静态内部类实现单例
 * RedisFactory只用获取一次,使用spi机制
 * @Author th
 * @Date 2023/7/19 9:59
 */
public class RedisFactorySingleton {

    /**
     * 获取RedisFactory单例
     * @return
     */
    public static RedisFactory getInstance(){
        return SingletonHolder.instance;
    }
    private static class SingletonHolder{
        private static final RedisFactory instance ;
        static {
            instance= TUtil.spi(RedisFactory.class);
        }
    }

}
