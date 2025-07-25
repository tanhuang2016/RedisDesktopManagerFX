package xyz.hashdog.rdm.ui.entity.config;

import xyz.hashdog.rdm.ui.common.ConfigSettingsEnum;
import xyz.hashdog.rdm.ui.common.KeyTypeTagEnum;

import java.util.List;

public class AdvancedSetting implements ConfigSettings{

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
    /**
     * 是否树形显示
     */
    private boolean treeShow;

    @Override
    public String getName() {
        return ConfigSettingsEnum.KEY_TAG.name;
    }

    @Override
    public ConfigSettings init() {
        this.connectionTimeout= 6000;
        this.soTimeout= 6000;
        this.keySeparator= ":";
        this.treeShow= true;
        return this;
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

    public boolean isTreeShow() {
        return treeShow;
    }

    public void setTreeShow(boolean treeShow) {
        this.treeShow = treeShow;
    }
}
