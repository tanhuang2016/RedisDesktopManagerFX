package xyz.hashdog.rdm.redis;

/**
 * @Author th
 * @Date 2023/7/18 10:49
 */
public class RedisConfig {

    /**
     * 地址
     */
    private String host;
    /**
     * 端口
     */
    private int port;

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
}