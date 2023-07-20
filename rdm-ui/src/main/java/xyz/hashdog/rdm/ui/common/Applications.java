package xyz.hashdog.rdm.ui.common;

import javafx.scene.control.TreeItem;
import javafx.scene.text.Font;
import xyz.hashdog.rdm.common.util.TUtil;
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
     * 树的根节点id
     */
    public static final String ROOT_ID = "-1";
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
     * 初始化连接树
     * 从缓存去的连接集合,进行递归组装成树4
     * @return
     */
    public static TreeItem<ConnectionServerNode> initConnectionTreeView() {
        List<ConnectionServerNode> list = new ArrayList<>(CacheConfigSingleton.CONFIG.getConnectionNodeMap().values());
        TreeItem<ConnectionServerNode> root=new TreeItem<>();
        //得造一个隐形的父节点
        ConnectionServerNode node = new ConnectionServerNode(ConnectionServerNode.GROUP);
        node.setDataId(Applications.ROOT_ID);
        root.setValue(node);
        TUtil.RecursiveList2Tree.recursive(root, list, new TUtil.RecursiveList2Tree<TreeItem<ConnectionServerNode>, ConnectionServerNode>() {
            @Override
            public List<ConnectionServerNode> findSubs(TreeItem<ConnectionServerNode> tree, List<ConnectionServerNode> list) {
                List<ConnectionServerNode> subs = new ArrayList<>();
                String dataId = tree.getValue().getDataId();
                for (ConnectionServerNode connectionServerNode : list) {
                    if(connectionServerNode.getParentDataId().equals(dataId)){
                        subs.add(connectionServerNode);
                    }
                }
                return subs;
            }
            @Override
            public List<TreeItem<ConnectionServerNode>> toTree(TreeItem<ConnectionServerNode> tree, List<ConnectionServerNode> subs) {
                List<TreeItem<ConnectionServerNode>> trees = new ArrayList<>();
                for (ConnectionServerNode sub : subs) {
                    trees.add(new TreeItem<>(sub));
                }
                tree.getChildren().addAll(trees);
                return trees;
            }
            @Override
            public List<ConnectionServerNode> filterList(List<ConnectionServerNode> list, List<ConnectionServerNode> subs) {
                //这里未进行过滤,因为会造成元数据的删除,除非对list进行深度克隆
                return list;
            }

        });
        return root;
    }
}
