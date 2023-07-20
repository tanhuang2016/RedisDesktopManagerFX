package xyz.hashdog.rdm.ui.entity;

/**
 * @Author th
 * @Date 2023/7/20 16:21
 */
public class ConnectionServerNode extends ConnectionGroupNode{
    /**
     * 地址
     */
    private String host;
    /**
     * 端口
     */
    private int port;
    /**
     * 密码
     */
    private String auth;

    public ConnectionServerNode() {
        setType(2);
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
}
