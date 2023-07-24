package xyz.hashdog.rdm.redis.imp.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;
import xyz.hashdog.rdm.common.util.DataUtil;
import xyz.hashdog.rdm.common.util.TUtil;
import xyz.hashdog.rdm.redis.Message;
import xyz.hashdog.rdm.redis.client.RedisClient;
import xyz.hashdog.rdm.redis.exceptions.RedisException;
import xyz.hashdog.rdm.redis.imp.console.RedisConsole;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;

/**
 * 单机版jedis,用JedisPool包装实现
 *
 * @Author th
 * @Date 2023/7/18 12:59
 */
public class JedisPoolClient implements RedisClient {
    protected static Logger log = LoggerFactory.getLogger(JedisPoolClient.class);

    
    private Jedis jedis;
    public JedisPoolClient(JedisPool pool) {
        jedis = pool.getResource();
    }


    private int db;
    /**
     * 这里通过message进行传输异常
     * 可以优化为统一异常处理,这个方法暂时保留 FIXME
     * @return
     */
    @Override
    public Message testConnect()  {
        Message message=new Message();
        try {
            jedis.ping();
            message.setSuccess(true);
        }catch (JedisConnectionException e) {
            message.setSuccess(false);
            message.setMessage(e.getMessage());
        }
        return message;
    }

    @Override
    public int getDb() {
        return db;
    }

    /**
     * 执行命令的封装
     * 统一命令的异常转换
     * @param execCommand
     * @return
     * @param <R>
     */
    private  <R> R execut( Function<Jedis, R> execCommand) {
        try {
            return TUtil.execut(this.jedis,execCommand,Jedis::close);

        }catch (JedisException e){
            log.info("redis api exception",e);
            throw new RedisException(e.getMessage());
        }
    }

    @Override
    public boolean isConnected() {
        return execut(jedis->jedis.isConnected());
    }

    /**
     * info Keyspace返回
     * db0:keys=6,expires=0,avg_ttl=0
     * db1:keys=1,expires=0,avg_ttl=0
     * 拆分获取
     * @return
     */
    @Override
    public Map<Integer, String> dbSize() {
        return execut(jedis->{
            Map<Integer,String> map = new LinkedHashMap<>();
            for (int i = 0; i < 15; i++) {
                map.put(i,"DB"+i);
            }
            String info = jedis.info("Keyspace");
            String[] line = info.split("\r\n");
            for (String row : line) {
                if(!row.startsWith("db")){
                    continue;
                }
                String[] a = row.split(":");
                int db =Integer.parseInt(a[0].substring(2));
                int size =Integer.parseInt(a[1].split(",")[0].substring(5)) ;
                map.put(db,map.get(db)+String.format("[%d]",size));
            }
            return map;
        });
    }

    @Override
    public String select(int db) {
        String execut = execut(jedis -> jedis.select(db));
        this.db=db;
        return execut;
    }

    @Override
    public List<String> scanAll(String pattern) {
        return execut(jedis -> {
            List<String> keys = new ArrayList<>();
            // 定义SCAN命令参数，匹配所有键
            ScanParams scanParams = new ScanParams();
            scanParams.count(5000);
            if(DataUtil.isNotBlank(pattern)){
                scanParams.match(pattern);
            }
            // 开始SCAN迭代
            String cursor = "0";
            do {
                ScanResult<String> scanResult = jedis.scan(cursor, scanParams);
                for (String key : scanResult.getResult()) {
                    keys.add(key);
                }
                cursor = scanResult.getCursor();
            } while (!cursor.equals("0"));
            return keys;
        });
    }



    @Override
    public Set<String> keys(String pattern) {
        return execut(jedis->jedis.keys(pattern));
    }

    @Override
    public String type(String key) {
        return execut(jedis->jedis.type(key));
    }

    @Override
    public long ttl(String key) {
        return execut(jedis->jedis.ttl(key));
    }

    @Override
    public String ping() {
        return execut(Jedis::ping);

    }
    @Override
    public String info() {
        return execut(jedis->jedis.info());

    }
    @Override
    public String rename(String oldkey, String newkey) {
        return execut(jedis->jedis.rename(oldkey,newkey));

    }
    @Override
    public long expire(String key, long seconds) {
        return execut(jedis->jedis.expire(key,seconds));

    }
    @Override
    public boolean exists(String key) {
        return execut(jedis->jedis.exists(key));
    }
    @Override
    public long del(String... key) {
        return execut(jedis->jedis.del(key));

    }
    @Override
    public long persist(String key) {
        return execut(jedis->jedis.persist(key));

    }

    @Override
    public String restore(String key, long ttl, byte[] serializedValue) {
        return execut(jedis->jedis.restore(key,ttl,serializedValue));
    }
    @Override
    public byte[] dump(String key) {
        return execut(jedis->jedis.dump(key));
    }
    @Override
    public String flushDB() {
        return execut(jedis->jedis.flushDB());
    }


    @Override
    public String get(String key) {
        return execut(jedis->jedis.get(key));
    }
    @Override
    public byte[] get(byte[] key) {
        return execut(jedis->jedis.get(key));
    }
    @Override
    public String set(String key,String value) {
        return execut(jedis->jedis.set(key,value));
    }
    @Override
    public String set(byte[] key,byte[] value) {
        return execut(jedis->jedis.set(key,value));
    }


    /**
     * 传了一个SocketAcquirer匿名内部类实现
     * SocketAcquirer 每次都是从pool获取最新的socket
     * 但是使用socket后没关流,如果有必要可以用warp包装socket多传1个回调函数,
     * 进行cmd调用完之后关流
     * @return
     */
    @Override
    public RedisConsole getRedisConsole() {
        return new RedisConsole(() -> {
            try (Jedis jedis = this.jedis) {
                return TUtil.getField(jedis.getConnection(), "socket");
            }
        });
    }

    @Override
    public void close() throws IOException {
        if(this.jedis!=null){
            this.jedis.close();
        }
    }
}
