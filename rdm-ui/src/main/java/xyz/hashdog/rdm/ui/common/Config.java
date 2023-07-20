package xyz.hashdog.rdm.ui.common;

import xyz.hashdog.rdm.ui.entity.ConnectionServerNode;

import java.util.Map;

/**
 * @Author th
 * @Date 2023/7/20 16:46
 */
public class Config {

    private Map<String, ConnectionServerNode> connectionNodeMap;

    public Map<String, ConnectionServerNode> getConnectionNodeMap() {
        return connectionNodeMap;
    }

    public void setConnectionNodeMap(Map<String, ConnectionServerNode> connectionNodeMap) {
        this.connectionNodeMap = connectionNodeMap;
    }
}
