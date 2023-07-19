package xyz.hashdog.rdm.redis.imp.console;

import org.junit.Before;
import org.junit.Test;
import xyz.hashdog.rdm.redis.RedisClient;
import xyz.hashdog.rdm.redis.RedisConfig;
import xyz.hashdog.rdm.redis.RedisConsole;
import xyz.hashdog.rdm.redis.RedisContext;
import xyz.hashdog.rdm.redis.imp.RedisFactory;

import java.util.List;

/**
 * @Author th
 * @Date 2023/7/19 9:26
 */
public class RedisConsoleTest {
    private RedisContext redisContext;

    @Before
    public void before(){
        xyz.hashdog.rdm.redis.RedisFactory redisFactory=new RedisFactory();
        RedisConfig redisConfig =new RedisConfig();
        redisConfig.setHost("localhost");
        redisConfig.setPort(6379);
         this.redisContext = redisFactory.createRedisContext(redisConfig);
    }

    @Test
    public void scan(){
        RedisClient redisClient=this.redisContext.getRedisClient();
        RedisConsole redisConsole=redisClient.getRedisConsole();
        List<String> result=redisConsole.sendCommand("SCAN 0 MATCH A* COUNT 10");
        result.forEach(e-> System.out.println(e));
    }
    @Test
    public void keys(){
        RedisClient redisClient=this.redisContext.getRedisClient();
        RedisConsole redisConsole=redisClient.getRedisConsole();
        List<String> result=redisConsole.sendCommand("keys *");
        result.forEach(e-> System.out.println(e));
    }
    @Test
    public void lrange(){
        RedisClient redisClient=this.redisContext.getRedisClient();
        RedisConsole redisConsole=redisClient.getRedisConsole();
        List<String> result=redisConsole.sendCommand("lrange list 0 99");
        result.forEach(e-> System.out.println(e));
    }
}
