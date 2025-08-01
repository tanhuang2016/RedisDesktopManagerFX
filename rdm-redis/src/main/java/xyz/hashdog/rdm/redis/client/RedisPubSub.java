package xyz.hashdog.rdm.redis.client;


/**
 * 发布订阅消息获取
 * @author th
 * @version 2.1.1
 * @since 2025/7/30 22:48
 */
@FunctionalInterface
public interface RedisPubSub {

     void onMessage(String channel,String msg);
}
