package xyz.hashdog.rdm.ui.common;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import xyz.hashdog.rdm.common.pool.ThreadPool;
import xyz.hashdog.rdm.common.util.DataUtil;
import xyz.hashdog.rdm.ui.entity.config.ConnectionServerNode;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.prefs.Preferences;

/**
 * 首选项配置是肯定需要用到的
 * 恶汉单例缓存配置
 *
 * @author th
 * @version 1.0.0
 * @since 2023/7/20 16:46
 */
public class CacheConfigSingleton {


    protected final static Config CONFIG;
    private final static Preferences PREFERENCES = Preferences.userRoot().node(Applications.NODE_APP_NAME);

    private CacheConfigSingleton() {
    }

    static {
        CONFIG = new Config();
        //看源码实际上用的ObservableMapWrapper,进行保证,看起来应该对map的增删应该是线程安全的,无所谓客户端能有多大并发量
        CONFIG.setConnectionNodeMap(FXCollections.observableMap(new ConcurrentHashMap<>()));
        CacheConfigSingleton.initData();
        CacheConfigSingleton.addListener();
    }

    /**
     * 初始化数据
     */
    private static void initData() {
        Preferences node = PREFERENCES.node(Applications.NODE_APP_DATA);
        String connectionJsonStr = node.get(Applications.KEY_CONNECTIONS, null);
        if (DataUtil.isBlank(connectionJsonStr)) {
            return;
        }
        // 使用 Gson 将 JSON 字符串转换为 List<ConnectionServerNode>
        Gson gson = new Gson();
        Type type = new TypeToken<List<ConnectionServerNode>>() {
        }.getType();
        List<ConnectionServerNode> list = gson.fromJson(connectionJsonStr, type);
        for (ConnectionServerNode connectionServerNode : list) {
            CONFIG.getConnectionNodeMap().put(connectionServerNode.getDataId(),connectionServerNode);
        }
    }

    /**
     * 监听数据的变动,进行异步保存
     */
    private static void addListener() {
        ((ObservableMap) CONFIG.getConnectionNodeMap()).addListener((MapChangeListener<String, ConnectionServerNode>) change -> {
            if (change.wasAdded() || change.wasRemoved()) {
                ThreadPool.getInstance().execute(()->{
                    Preferences node = PREFERENCES.node(Applications.NODE_APP_DATA);
                    node.put(Applications.KEY_CONNECTIONS,new Gson().toJson(CONFIG.getConnectionNodeMap().values()));
                    System.out.println("触发保存");
                });
            }
        });
    }


}
