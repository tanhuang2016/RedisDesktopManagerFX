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

    /**
     * 是否ssh
     */
    private boolean ssh;
    /**
     * ssh主机
     */
    private String sshHost;
    /**
     * ssh端口
     */
    private int sshPort;
    /**
     * ssh用户名
     */
    private String sshUserName;
    /**
     * ssh密码
     */
    private String sshPassword;
    /**
     * 私钥文件
     */
    private String sshPrivateKey;
    /**
     * 私钥密码
     */
    private String sshPassphrase;
    /**
     * 连接超时
     */
    private int connectionTimeout;
    /**
     * 读超时
     */
    private int soTimeout;
    /**
     * key 分隔符
     */
    private String keySeparator;


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

    public boolean isSsh() {
        return ssh;
    }

    public void setSsh(boolean ssh) {
        this.ssh = ssh;
    }

    public String getSshHost() {
        return sshHost;
    }

    public void setSshHost(String sshHost) {
        this.sshHost = sshHost;
    }

    public int getSshPort() {
        return sshPort;
    }

    public void setSshPort(int sshPort) {
        this.sshPort = sshPort;
    }

    public String getSshUserName() {
        return sshUserName;
    }

    public void setSshUserName(String sshUserName) {
        this.sshUserName = sshUserName;
    }

    public String getSshPassword() {
        return sshPassword;
    }

    public void setSshPassword(String sshPassword) {
        this.sshPassword = sshPassword;
    }

    public String getSshPrivateKey() {
        return sshPrivateKey;
    }

    public void setSshPrivateKey(String sshPrivateKey) {
        this.sshPrivateKey = sshPrivateKey;
    }

    public String getSshPassphrase() {
        return sshPassphrase;
    }

    public void setSshPassphrase(String sshPassphrase) {
        this.sshPassphrase = sshPassphrase;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getSoTimeout() {
        return soTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    public String getKeySeparator() {
        return keySeparator;
    }

    public void setKeySeparator(String keySeparator) {
        this.keySeparator = keySeparator;
    }
}
