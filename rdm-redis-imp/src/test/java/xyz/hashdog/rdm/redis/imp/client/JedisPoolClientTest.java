package xyz.hashdog.rdm.redis.imp.client;

import org.junit.Before;
import org.junit.Test;
import xyz.hashdog.rdm.common.util.EncodeUtil;
import xyz.hashdog.rdm.common.util.FileUtil;
import xyz.hashdog.rdm.redis.client.RedisClient;
import xyz.hashdog.rdm.redis.RedisConfig;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
    @Test
    public void dump(){
        byte[] dump = redisClient.dump("AA");
        System.out.println(dump);
    }
    @Test
    public void restore(){
        byte[] dump = redisClient.dump("AA");

        String restore = redisClient.restore("AA2",980000l,dump);
        System.out.println(restore);
    }

    @Test
    public void flushDB (){
//        String flushDB = redisClient.flushDB();
//        System.out.println(flushDB);
    }

    @Test
    public void set (){
        String set = redisClient.set("AA","中文44ik");
        System.out.println(set);
    }

    @Test
    public void get (){
        String get = redisClient.get("AA");
        System.out.println(get);
    }

    @Test
    public void set2 (){
//        String set = redisClient.set("AAA".getBytes(),"中文44ik".getBytes(Charset.forName("gbk")));
        String set = redisClient.set("AAA".getBytes(),"中文44ik".getBytes(StandardCharsets.ISO_8859_1));
        System.out.println(set);
    }
    @Test
    public void setImage () throws IOException {
        String set = redisClient.set("image".getBytes(), FileUtil.file2byte("C:\\Users\\11036\\Desktop\\123.png"));
        System.out.println(set);
    }
    @Test
    public void get2 (){
        byte[] get = redisClient.get("AA".getBytes(StandardCharsets.UTF_8));
        System.out.println(new String(get, Charset.forName("gbk")));
    }
    @Test
    public void testgbk (){
        byte[] get = redisClient.get("image".getBytes(StandardCharsets.UTF_8));
        System.out.println("size:"+get.length);
        String utf8 = new String(get, StandardCharsets.UTF_8);
        System.out.println(utf8);
        System.out.println(EncodeUtil.isUTF8(get));
    }



}
