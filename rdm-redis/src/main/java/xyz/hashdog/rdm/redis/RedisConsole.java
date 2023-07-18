package xyz.hashdog.rdm.redis;

import java.util.List;

/**
 *控制台交互
 * @Author th
 * @Date 2023/7/18 21:16
 */
public interface RedisConsole {
    /**
     * 发送命令
     * 返回结果集
     * @return
     */
    List<String> sendCommand(String cmd);
}
