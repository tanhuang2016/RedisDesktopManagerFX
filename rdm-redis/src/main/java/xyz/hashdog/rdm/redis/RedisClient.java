package xyz.hashdog.rdm.redis;

import java.io.Closeable;
import java.util.Set;

/**
 * redis客户端操作
 * 相关redis操作命令都在这儿
 * @Author th
 * @Date 2023/7/18 11:03
 */
public interface RedisClient {

    /**
     * keys 模糊查新的命令
     * @param pattern
     * @return
     */
    Set<String> keys(String pattern);

    /**
     * 控制台交互器
     * @return
     */
    RedisConsole getRedisConsole();

}
