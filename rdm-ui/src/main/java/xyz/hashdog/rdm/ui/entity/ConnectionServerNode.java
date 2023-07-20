package xyz.hashdog.rdm.ui.entity;

/**
 * @Author th
 * @Date 2023/7/20 16:21
 */
public class ConnectionServerNode {
    public static final int SERVER = 2;
    public static final int GROUP = 1;
    /**
     * 类型,分组为1,连接为2
     */
    private int type;
    /**
     * 定位id
     */
    private String dataId;
    /**
     * 父节点id
     */
    private String parentDataId;
    /**
     * 名称
     */
    private String name;
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
    }

    public ConnectionServerNode(int type) {
        this.type=type;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getParentDataId() {
        return parentDataId;
    }

    public void setParentDataId(String parentDataId) {
        this.parentDataId = parentDataId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
