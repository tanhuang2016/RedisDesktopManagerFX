package xyz.hashdog.rdm.ui.common;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import xyz.hashdog.rdm.ui.entity.ConnectionGroupNode;

/**
 * 首选项配置是肯定需要用到的
 * 恶汉单例缓存配置
 * @Author th
 * @Date 2023/7/20 16:46
 */
public class CacheConfigSingleton {

    protected final static Config CONFIG;
    private CacheConfigSingleton() {
    }
    static {
        CONFIG=new Config();
        ObservableMap<String, ConnectionGroupNode> observableMap = FXCollections.observableHashMap();
        CONFIG.setConnectionNodeMap(observableMap);
        observableMap.addListener((MapChangeListener<String, ConnectionGroupNode>) change -> {
            if (change.wasAdded()) {
                System.out.println("Added: " + change.getKey() + " = " + change.getValueAdded());
            } else if (change.wasRemoved()) {
                System.out.println("Removed: " + change.getKey() + " = " + change.getValueRemoved());
            }
        });
    }


}
