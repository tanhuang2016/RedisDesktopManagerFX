package xyz.hashdog.rdm.ui.common;

import javafx.scene.control.TreeItem;
import javafx.scene.text.Font;
import xyz.hashdog.rdm.redis.Message;
import xyz.hashdog.rdm.ui.entity.ConnectionServerNode;

import java.util.ArrayList;
import java.util.List;

public class Applications {

    /**
     * 连接
     */
    public static final String KEY_CONNECTIONS = "Conections";
    /**
     * 应用名称
     */
    public static String NODE_APP_NAME="RedisDesktopManagerFX";
    /**
     * 配置节点
     */
    public static String NODE_APP_CONE="Config";
    /**
     * 数据节点
     */
    public static String NODE_APP_DATA="Data";


    /**
     * 获取系统支持的所有字体
     *
     * @return
     */
    public static List<String> getSystemFontNames() {
        return Font.getFontNames();
    }

    /**
     * 新建连接数据的新增
     *
     * @param connectionServerNode
     * @return
     */
    public static Message addConnection(ConnectionServerNode connectionServerNode) {
        CacheConfigSingleton.CONFIG.getConnectionNodeMap().put(connectionServerNode.getDataId(), connectionServerNode);
        return new Message(true);
    }

    /**
     * 初始化连接
     * @return
     */
    public static TreeItem<ConnectionServerNode> initConnectionTreeView() {
        List<ConnectionServerNode> list = new ArrayList<>(CacheConfigSingleton.CONFIG.getConnectionNodeMap().values());
        return null;
    }
}
