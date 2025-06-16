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
}
