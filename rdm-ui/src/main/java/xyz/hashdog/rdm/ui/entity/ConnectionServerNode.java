package xyz.hashdog.rdm.ui.entity;

import xyz.hashdog.rdm.ui.common.Applications;

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
    /**
     * 时间戳排序
     */
    private long timestampSort;

    public ConnectionServerNode() {
    }

    public long getTimestampSort() {
        return timestampSort;
    }

    public void setTimestampSort(long timestampSort) {
        this.timestampSort = timestampSort;
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

    /**
     * 是否是连接
     * @return
     */
    public boolean isConnection() {
        return type==2;
    }

    /**
     * 是否是跟
     * @return
     */
    public boolean isRoot() {
        return dataId.equals(Applications.ROOT_ID) ;
    }
}
