package xyz.hashdog.rdm.ui.common;

import javafx.scene.control.TreeItem;
import javafx.scene.text.Font;
import xyz.hashdog.rdm.common.util.DataUtil;
import xyz.hashdog.rdm.common.util.TUtil;
import xyz.hashdog.rdm.redis.Message;
import xyz.hashdog.rdm.ui.entity.config.ConnectionServerNode;
import xyz.hashdog.rdm.ui.util.GuiUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 虚拟化和反虚拟化操作工具
 * 封装对数据持久化,及视图数据初始化的相关操作
 */
public class Applications {

    public static  final String DEFUALT_VALUE="520";


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
    public static final String NODE_APP_NAME = "RedisDesktopManagerFX";
    /**
     * 配置节点
     */
    public static final String NODE_APP_CONE = "Config";
    /**
     * 数据节点
     */
    public static final String NODE_APP_DATA = "Data";
    /**
     * 设置数据
     */
    public static final String NODE_APP_SETTINGS = "Settings";
    public static final String DEFUALT_JSON_VALUE = "{\"520 \": \"520\"}";


    /**
     * 获取系统支持的所有字体
     *
     * @return
     */
    public static List<String> getSystemFontNames() {
        return Font.getFontNames();
    }

    /**
     * 连接/或分组数据的新增和修改
     *
     * @param connectionServerNode
     * @return
     */
    public static Message addOrUpdateConnectionOrGroup(ConnectionServerNode connectionServerNode) {
        //id存在则修改,否则是新增
        if (DataUtil.isNotBlank(connectionServerNode.getDataId())) {
            //修改,需要查久数据,补充父id和时间戳
            ConnectionServerNode old = CacheConfigSingleton.CONFIG.getConnectionNodeMap().get(connectionServerNode.getDataId());
            connectionServerNode.setParentDataId(old.getParentDataId());
            connectionServerNode.setTimestampSort(old.getTimestampSort());
        } else {
            //新增需要设置id和时间戳字段
            connectionServerNode.setDataId(DataUtil.getUUID());
            connectionServerNode.setTimestampSort(System.currentTimeMillis());
        }
        //put进缓存,会触发持久化
        CacheConfigSingleton.CONFIG.getConnectionNodeMap().put(connectionServerNode.getDataId(), connectionServerNode);
        return new Message(true);
    }

    /**
     * 节点重命名,包括连接和分组
     *
     * @param groupNode
     * @return
     */
    public static Message renameConnectionOrGroup(ConnectionServerNode groupNode) {
        ConnectionServerNode old = CacheConfigSingleton.CONFIG.getConnectionNodeMap().get(groupNode.getDataId());
        TUtil.copyProperties(old, groupNode);
        //map在put的时候需要引用地址变更才会触发监听,所以这里进行了域的复制
        CacheConfigSingleton.CONFIG.getConnectionNodeMap().put(old.getDataId(), groupNode);
        return new Message(true);
    }


    /**
     * 根据id递归删除
     *
     * @param tree
     * @return
     */
    public static Message deleteConnectionOrGroup(TreeItem<ConnectionServerNode> tree) {
        List<String> ids = new ArrayList<>();
        TUtil.RecursiveTree2List.recursive(ids, tree, new TUtil.RecursiveTree2List<List<String>, TreeItem<ConnectionServerNode>>() {
            @Override
            public List<TreeItem<ConnectionServerNode>> subset(TreeItem<ConnectionServerNode> connectionServerNodeTreeItem) {
                return connectionServerNodeTreeItem.getChildren();
            }

            @Override
            public void noSubset(List<String> h, TreeItem<ConnectionServerNode> connectionServerNodeTreeItem) {
                h.add(connectionServerNodeTreeItem.getValue().getDataId());
            }

            @Override
            public List<String> hasSubset(List<String> h, TreeItem<ConnectionServerNode> connectionServerNodeTreeItem) {
                h.add(connectionServerNodeTreeItem.getValue().getDataId());
                return h;
            }
        });
        for (String id : ids) {
            CacheConfigSingleton.CONFIG.getConnectionNodeMap().remove(id);
        }
        return new Message(true);
    }

    /**
     * 初始化连接树
     * 从缓存去的连接集合,进行递归组装成树4
     *
     * @return
     */
    public static TreeItem<ConnectionServerNode> initConnectionTreeView() {
        List<ConnectionServerNode> list = new ArrayList<>(CacheConfigSingleton.CONFIG.getConnectionNodeMap().values());
//        for (ConnectionServerNode connectionServerNode : list) {
//            if(connectionServerNode.getParentDataId()==null){
//                connectionServerNode.setParentDataId("-1");
//            }
//        }
        list.sort((a, b) -> a.getTimestampSort() > b.getTimestampSort() ? 1 : -1);
        TreeItem<ConnectionServerNode> root = new TreeItem<>();
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
                    if (connectionServerNode.getParentDataId().equals(dataId)) {
                        subs.add(connectionServerNode);
                    }
                }
                return subs;
            }

            @Override
            public List<TreeItem<ConnectionServerNode>> toTree(TreeItem<ConnectionServerNode> tree, List<ConnectionServerNode> subs) {
                List<TreeItem<ConnectionServerNode>> trees = new ArrayList<>();
                for (ConnectionServerNode sub : subs) {
                    if(sub.isConnection()){
                        trees.add(new TreeItem<>(sub,GuiUtil.creatConnctionImageView()));
                    }else {
                        trees.add(new TreeItem<>(sub, GuiUtil.creatGroupImageView()));
                    }
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
