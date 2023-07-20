package xyz.hashdog.rdm.redis.imp;

import org.junit.Before;
import org.junit.Test;
import xyz.hashdog.rdm.redis.RedisConfig;
import xyz.hashdog.rdm.redis.RedisContext;
import xyz.hashdog.rdm.redis.client.RedisClient;
import xyz.hashdog.rdm.redis.client.RedisConsole;
import xyz.hashdog.rdm.redis.exceptions.RedisException;
import xyz.hashdog.rdm.redis.imp.client.JedisPoolClient;

/**
 * @Author th
 * @Date 2023/7/20 10:21
 */
public class RedisContextTest {

    private RedisContext redisContext;

    @Before
    public void before() {
        RedisConfig redisConfig = new RedisConfig();
        redisConfig.setHost("localhost");
        redisConfig.setPort(63791);
        redisContext = new xyz.hashdog.rdm.redis.imp.RedisContext(redisConfig);
    }

    @Test
    public void testConnect() {
        try {
            System.out.println(redisContext.testConnect());
        }catch (RedisException e){
            System.out.println(e.getMessage());
        }
    }
}
