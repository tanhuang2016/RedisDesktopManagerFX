package xyz.hashdog.rdm.redis.client;

import java.util.List;

/**
 *控制台交互
 * @author th
 * @version 1.0.0
 * @since 2023/7/18 21:16
 */
public interface RedisConsole {
    /**
     * 发送命令
     * todo 需要改为队列形式
     * 返回结果集
     * @return
     */
    List<String> sendCommand(String cmd);
}
