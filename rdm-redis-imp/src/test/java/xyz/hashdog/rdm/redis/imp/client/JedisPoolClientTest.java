package xyz.hashdog.rdm.redis.imp.client;

import org.junit.Before;
import org.junit.Test;
import xyz.hashdog.rdm.redis.client.RedisClient;
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
        long ttl = redisClient.ttl("AA");
        System.out.println(ttl);
    }
    @Test
    public void ping(){
        String ping = redisClient.ping();
        System.out.println(ping);
    }
    @Test
    public void info(){
        String info = redisClient.info();
        System.out.println(info);
    }
    @Test
    public void rename(){
        String rename = redisClient.rename("AA","AA");
        System.out.println(rename);
    }
    @Test
    public void expire(){
        long expire = redisClient.expire("AA",100000000);
        System.out.println(expire);
    }

    @Test
    public void exists(){
        boolean exists = redisClient.exists("AA");
        System.out.println(exists);
    }

    @Test
    public void del(){
        long del = redisClient.del("AA1");
        System.out.println(del);
    }

    @Test
    public void persist(){
        long persist = redisClient.persist("AA");
        System.out.println(persist);
    }


}
