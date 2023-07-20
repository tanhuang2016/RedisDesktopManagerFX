package xyz.hashdog.rdm.ui.common;

import xyz.hashdog.rdm.ui.entity.ConnectionGroupNode;

import java.util.Map;

/**
 * @Author th
 * @Date 2023/7/20 16:46
 */
public class Config {

    private Map<String, ConnectionGroupNode> connectionNodeMap;

    public Map<String, ConnectionGroupNode> getConnectionNodeMap() {
        return connectionNodeMap;
    }

    public void setConnectionNodeMap(Map<String, ConnectionGroupNode> connectionNodeMap) {
        this.connectionNodeMap = connectionNodeMap;
    }
}
