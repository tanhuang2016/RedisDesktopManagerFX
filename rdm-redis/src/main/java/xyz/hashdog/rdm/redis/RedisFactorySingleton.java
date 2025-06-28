package xyz.hashdog.rdm.redis;

import xyz.hashdog.rdm.common.util.TUtil;
import xyz.hashdog.rdm.redis.RedisFactory;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * 静态内部类实现单例
 * RedisFactory只用获取一次,使用spi机制
 * @author th
 * @version 1.0.0
 * @since 2023/7/19 9:59
 */
    public class RedisFactorySingleton {
    private RedisFactorySingleton() {
    }

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
            instance= spi(RedisFactory.class);
        }
    }

    /**
     * spi获取此类服务
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T spi(Class<T> clazz) {
        ServiceLoader<T> load = ServiceLoader.load(clazz);
        Iterator<T> iterator = load.iterator();

        //循环获取所需的对象
        while (iterator.hasNext()) {
            T next = iterator.next();
            if (next != null) {
                return next;
            }
        }
        throw new RuntimeException("no such spi:" + clazz.getName());
    }

}
