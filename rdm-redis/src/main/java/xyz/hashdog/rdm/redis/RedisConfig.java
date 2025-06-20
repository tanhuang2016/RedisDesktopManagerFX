package xyz.hashdog.rdm.redis;

/**
 * @author th
 * @version 1.0.0
 * @since 2023/7/18 10:49
 */
public class RedisConfig {

    /**
     * 地址
     */
    private String host;
    /**
     * 名称
     */
    private String name;
    /**
     * 端口
     */
    private int port;
    /**
     * 授权
     */
    private String auth;
    /**
     * 是否集群模式
     */
    private boolean cluster;
    /**
     * 是否哨兵模式
     */
    private boolean sentine;
    /**
     * 主节点名称
     */
    private String masterName;

    /**
     * 是否ssl
     */
    private boolean ssl;
    /**
     * ca证书
     */
    private String caCrt;
    /**
     * 服务端证书
     */
    private String redisCrt;
    /**
     * 私钥文件
     */
    private String redisKey;
    /**
     * 私钥密码
     */
    private String redisKeyPassword;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public boolean isCluster() {
        return cluster;
    }

    public void setCluster(boolean cluster) {
        this.cluster = cluster;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public boolean isSentine() {
        return sentine;
    }

    public void setSentine(boolean sentine) {
        this.sentine = sentine;
    }

    public boolean isSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public String getCaCrt() {
        return caCrt;
    }

    public void setCaCrt(String caCrt) {
        this.caCrt = caCrt;
    }

    public String getRedisCrt() {
        return redisCrt;
    }

    public void setRedisCrt(String redisCrt) {
        this.redisCrt = redisCrt;
    }

    public String getRedisKey() {
        return redisKey;
    }

    public void setRedisKey(String redisKey) {
        this.redisKey = redisKey;
    }

    public String getRedisKeyPassword() {
        return redisKeyPassword;
    }

    public void setRedisKeyPassword(String redisKeyPassword) {
        this.redisKeyPassword = redisKeyPassword;
    }
}
