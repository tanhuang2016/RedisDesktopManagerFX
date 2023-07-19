package xyz.hashdog.rdm.redis.imp.client;

import org.junit.Before;
import org.junit.Test;
import xyz.hashdog.rdm.redis.RedisClient;
import xyz.hashdog.rdm.redis.RedisConfig;

import java.util.Set;

/**
 * @Author th
 * @Date 2023/7/19 11:05
 */
public class JedisPoolClientTest {

    private RedisClient redisClient;

    @Before
    public void before(){
        RedisConfig redisConfig =new RedisConfig();
        redisConfig.setHost("localhost");
        redisConfig.setPort(6379);
        redisClient=new JedisPoolClient(redisConfig);
    }

    @Test
    public void keys(){
        Set<String> keys = redisClient.keys("*");
        System.out.println(keys);
    }
    @Test
    public void type(){
        String type = redisClient.type("list");
        System.out.println(type);
    }

    @Test
    public void ttl(){
        long ttl = redisClient.ttl("list");
        System.out.println(ttl);
    }
}
