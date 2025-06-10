package xyz.hashdog.rdm.redis.imp.client;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import xyz.hashdog.rdm.common.util.DataUtil;
import xyz.hashdog.rdm.redis.RedisConfig;
import xyz.hashdog.rdm.redis.client.RedisClient;
import xyz.hashdog.rdm.redis.imp.Constant;

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
        if (redisConfig.isCluster()) {
            Set<HostAndPort> nodes = new HashSet<>();
            nodes.add(new HostAndPort(redisConfig.getHost(), redisConfig.getPort()));

            JedisCluster jedisCluster = new JedisCluster(nodes,10000,3000,3,redisConfig.getAuth(),Constant.POOL_CONFIG);
            return new JedisClusterClient(jedisCluster);
        }
        if(DataUtil.isNotBlank(redisConfig.getAuth())){
            this.pool=new JedisPool(Constant.POOL_CONFIG, redisConfig.getHost(), redisConfig.getPort(),500,redisConfig.getAuth());

        }else {
            this.pool=new JedisPool(Constant.POOL_CONFIG, redisConfig.getHost(), redisConfig.getPort());

        }
        return new JedisPoolClient(pool);
    }

    @Override
    public void close()  {
        if(pool!=null){
            pool.close();
        }
    }
}
