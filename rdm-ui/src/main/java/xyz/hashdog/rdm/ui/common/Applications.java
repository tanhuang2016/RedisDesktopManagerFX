package xyz.hashdog.rdm.ui.common;

import javafx.scene.text.Font;
import xyz.hashdog.rdm.redis.Message;
import xyz.hashdog.rdm.ui.entity.ConnectionServerNode;

import java.util.List;

public class Applications {


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
}
