package xyz.hashdog.rdm.redis.imp.client;

import com.jcraft.jsch.Session;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;
import xyz.hashdog.rdm.common.util.DataUtil;
import xyz.hashdog.rdm.common.util.TUtil;
import xyz.hashdog.rdm.redis.RedisConfig;
import xyz.hashdog.rdm.redis.client.RedisClient;
import xyz.hashdog.rdm.redis.imp.Constant;
import xyz.hashdog.rdm.redis.imp.Util;

import javax.net.ssl.SSLSocketFactory;
import java.util.HashSet;
import java.util.Set;

/**
 * @author th
 * @version 1.0.0
 * @since 2023/7/18 12:47
 */
public class DefaultRedisClientCreator implements RedisClientCreator{

    /**
     * jedis
     */
    private JedisPool pool;
    /**
     * 根据RedisConfig 判断创建什么类型的redis客户端
     * @param redisConfig
     * @return
     */
    @Override
    public RedisClient create(RedisConfig redisConfig) {
        if(redisConfig.isSentine()){
            Set<String> sentinels = new HashSet<>();
            sentinels.add(redisConfig.getHost()+":"+redisConfig.getPort());
            JedisSentinelPool pool = new JedisSentinelPool(redisConfig.getMasterName(), sentinels,redisConfig.getAuth());
            return new JedisSentinelPoolClient(pool.getResource());
        }
        if (redisConfig.isCluster()) {
            Set<HostAndPort> nodes = new HashSet<>();
            nodes.add(new HostAndPort(redisConfig.getHost(), redisConfig.getPort()));

            JedisCluster jedisCluster = new JedisCluster(nodes,10000,3000,3,redisConfig.getAuth(),Constant.POOL_CONFIG);
            return new JedisClusterClient(jedisCluster);
        }
        int port = redisConfig.getPort();
        String host = redisConfig.getHost();
        Session tunnel = null;
        if(redisConfig.isSsh()){
            tunnel = Util.createTunnel(redisConfig.getSshUserName(), redisConfig.getSshHost(), redisConfig.getSshPort(), redisConfig.getSshPassword(), redisConfig.getSshPrivateKey(), redisConfig.getSshPassphrase());
            port = Util.portForwardingL(tunnel, redisConfig.getHost(), redisConfig.getPort());
            host="127.0.0.1";
        }
        if(redisConfig.isSsl()){
            SSLSocketFactory SSLSocketFactory = Util.getSocketFactory(redisConfig.getCaCrt(), redisConfig.getRedisCrt(), redisConfig.getRedisKey(), redisConfig.getRedisKeyPassword());
            this.pool=new JedisPool(Constant.POOL_CONFIG, host, port,500,redisConfig.getAuth(),true,SSLSocketFactory,null,null);
            return new JedisPoolClient(pool.getResource(),tunnel);
        }
        this.pool=new JedisPool(Constant.POOL_CONFIG, host, port,500, TUtil.ifEmpty(redisConfig.getAuth(),null));
//        this.pool=new JedisPool(Constant.POOL_CONFIG, redisConfig.getHost(), redisConfig.getPort());
        return new JedisPoolClient(pool.getResource(),tunnel);
    }

    @Override
    public void close()  {
        if(pool!=null){
            pool.close();
        }
    }
}
