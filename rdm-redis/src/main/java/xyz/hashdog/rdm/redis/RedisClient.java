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

    Set<String> keys(String pattern);
}
