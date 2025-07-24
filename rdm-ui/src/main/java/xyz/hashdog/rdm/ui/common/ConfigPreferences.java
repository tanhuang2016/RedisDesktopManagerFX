package xyz.hashdog.rdm.ui.common;

import xyz.hashdog.rdm.ui.entity.config.ConfigSettings;
import xyz.hashdog.rdm.ui.entity.config.ConnectionServerNode;

import java.util.Map;

/**
 * @author th
 * @version 1.0.0
 * @since 2023/7/20 16:46
 */
public class ConfigPreferences {

    private Map<String, ConnectionServerNode> connectionNodeMap;
    private Map<String, ConfigSettings> configSettingsMap;

    public Map<String, ConnectionServerNode> getConnectionNodeMap() {
        return connectionNodeMap;
    }

    protected void setConnectionNodeMap(Map<String, ConnectionServerNode> connectionNodeMap) {
        this.connectionNodeMap = connectionNodeMap;
    }

    public Map<String, ConfigSettings> getConfigSettingsMap() {
        return configSettingsMap;
    }

    protected void setConfigSettingsMap(Map<String, ConfigSettings> configSettingsMap) {
        this.configSettingsMap = configSettingsMap;
    }
}
