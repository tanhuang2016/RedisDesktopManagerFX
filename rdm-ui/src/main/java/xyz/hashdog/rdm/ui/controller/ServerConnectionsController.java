package xyz.hashdog.rdm.ui.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import xyz.hashdog.rdm.redis.Message;
import xyz.hashdog.rdm.redis.RedisConfig;
import xyz.hashdog.rdm.redis.RedisContext;
import xyz.hashdog.rdm.redis.RedisFactorySingleton;
import xyz.hashdog.rdm.ui.common.Applications;
import xyz.hashdog.rdm.ui.entity.ConnectionServerNode;
import xyz.hashdog.rdm.ui.util.GuiUtil;

import java.io.IOException;

/**
 * 服务连接控制层
 *
 * @author th
 */
public class ServerConnectionsController extends BaseWindowController<MainController> {
    @FXML
    public AnchorPane root;
    /**
     * 上边栏的按钮父节点
     */
    @FXML
    public HBox buttonsHBox;
    /**
     * 右键菜单
     */
    @FXML
    public ContextMenu contextMenu;
    @FXML
    public Button bottomConnectButton;

    @FXML
    public TreeView<ConnectionServerNode> treeView;

    /**
     * 被选中节点
     */
    private ConnectionServerNode selectedNode;


    @FXML
    public void initialize() {
        initListener();
        initTreeView();
    }

    private void initListener() {
        treeViewListener();
    }

    private void treeViewListener() {
        buttonIsShowAndSetSelectNode();
        doubleClicked();

    }

    /**
     * treeView双击事件
     * 如果双击节点为连接,则进行连接redis
     */
    private void doubleClicked() {
        // 添加鼠标点击事件处理器
        treeView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                // 获取选中的节点
                TreeItem<ConnectionServerNode> selectedItem = treeView.getSelectionModel().getSelectedItem();
                if (selectedItem != null&&selectedItem.getValue().isConnection()) {
                    connect(null);
                }
            }
        });
    }

    /**
     * 监听treeView选中事件,判断需要显示和隐藏的按钮/菜单
     * 将选中的节点,缓存到类
     */
    private void buttonIsShowAndSetSelectNode() {
        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                newValue = treeView.getRoot();
            }
            //叶子节点是连接,这位原子叶子节点
            boolean isLeafNode = newValue.getValue().isConnection();
            //是否为根
            boolean isRoot = newValue.getValue().isRoot();
            // 使用选择器获取一组按钮,原子叶子节点才能连接,否则是目录才能新建分组和新建连接
            buttonsHBox.lookupAll(".isLeafNode").forEach(node -> {
                Button button = (Button) node;
                button.setDisable(!isLeafNode);
            });
            buttonsHBox.lookupAll(".isNotLeafNode").forEach(node -> {
                Button button = (Button) node;
                button.setDisable(isLeafNode);
            });
            buttonsHBox.lookupAll(".isNotRoot").forEach(node -> {
                Button button = (Button) node;
                button.setDisable(isRoot);
            });
            // 右键菜单显示/隐藏
            ObservableList<MenuItem> items = contextMenu.getItems();
            items.forEach(menuItem -> {
                if (menuItem.getStyleClass().contains("isLeafNode")) {
                    //禁用/隐藏
//                    menuItem.setDisable(!isLeafNode);
                    menuItem.setVisible(isLeafNode);
                }
                if (menuItem.getStyleClass().contains("isNotLeafNode")) {
                    menuItem.setVisible(!isLeafNode);
                }
                if (menuItem.getStyleClass().contains("isNotRoot")) {
                    menuItem.setVisible(!isRoot);
                }
            });
            //连接按钮禁用否
            bottomConnectButton.setDisable(!isLeafNode);
            //设置选中id
            this.selectedNode = newValue.getValue();

        });
    }

    /**
     * 初始化树节点
     */
    private void initTreeView() {
        initTreeViewCellFactory();
        initTreeViewData();
        // 自动展开根节点
        treeView.setShowRoot(false); // 隐藏根节点
        // Expand all nodes
        GuiUtil.expandAllNodes(treeView.getRoot());
        //默认根节点为选中节点
        treeView.getSelectionModel().select(treeView.getRoot());
    }

    /**
     * 初始化树节点的数据
     */
    private void initTreeViewData() {
        TreeItem<ConnectionServerNode> rootItem = Applications.initConnectionTreeView();
        treeView.setRoot(rootItem);
    }

    /**
     * 设置树节点的显示方式
     * 改为直接用TreeView泛型实体类的toString方法来显示
     */
    @Deprecated
    private void initTreeViewCellFactory() {
//        treeView.setCellFactory(new Callback<TreeView<ConnectionServerNode>, TreeCell<ConnectionServerNode>>() {
//            @Override
//            public TreeCell<ConnectionServerNode> call(TreeView<ConnectionServerNode> param) {
//
//                return new TreeCell<ConnectionServerNode>() {
//                    @Override
//                    protected void updateItem(ConnectionServerNode item, boolean empty) {
//                        super.updateItem(item, empty);
//                        if (empty) {
//                            setText(null);
//                        } else {
//                            setText(item.getName());
//                        }
//                    }
//                };
//            }
//        });
    }



    /**
     * 新建连接
     * 每次打开新窗口,所以Stage不用缓存
     *
     * @param actionEvent
     */
    @FXML
    public void newConnection(ActionEvent actionEvent) throws IOException {
        super.loadSubWindow("新建连接", "/fxml/NewConnectionView.fxml", root.getScene().getWindow(), ADD);
    }

    /**
     * 新增树节点,并选中
     *
     * @param connectionServerNode
     */
    public void AddConnectionOrGourpNodeAndSelect(ConnectionServerNode connectionServerNode) {
        TreeItem<ConnectionServerNode> connectionServerNodeTreeItem = new TreeItem<>(connectionServerNode);
        if(connectionServerNode.isConnection()){
            connectionServerNodeTreeItem.setGraphic(Applications.creatGroupImageView());
        }else {
            connectionServerNodeTreeItem.setGraphic(Applications.creatConnctionImageView());
        }
        if (connectionServerNode.getParentDataId().equals(Applications.ROOT_ID)) {
            treeView.getRoot().getChildren().add(connectionServerNodeTreeItem);
        } else {
            TreeItem<ConnectionServerNode> selectedItem = treeView.getSelectionModel().getSelectedItem();
            selectedItem.getChildren().add(connectionServerNodeTreeItem);
        }
        treeView.refresh();
        treeView.getSelectionModel().select(connectionServerNodeTreeItem);
    }

    @FXML
    public void newGroup(ActionEvent actionEvent) throws IOException {
        super.loadSubWindow("新建分组", "/fxml/NewGroupView.fxml", root.getScene().getWindow(), ADD);
    }


    /**
     * 获取被选中节点的id
     *
     * @return
     */
    public String getSelectedDataId() {
        return this.selectedNode.getDataId();
    }

    /**
     * 编辑节点
     *
     * @param actionEvent
     */
    @FXML
    public void edit(ActionEvent actionEvent) throws IOException {
        if (this.selectedNode.isConnection()) {
            NewConnectionController controller = super.loadSubWindow("编辑连接", "/fxml/NewConnectionView.fxml", root.getScene().getWindow(), UPDATE);
            controller.editInfo(this.selectedNode);
        } else {
            NewGroupController controller = super.loadSubWindow("编辑分组", "/fxml/NewGroupView.fxml", root.getScene().getWindow(), UPDATE);
            controller.editInfo(this.selectedNode);
        }


    }

    /**
     * 节点重新命名
     * 该名称不区分连接还是分组
     * 用分组的视图
     *
     * @param actionEvent
     */
    @FXML
    public void rename(ActionEvent actionEvent) throws IOException {
        NewGroupController controller = super.loadSubWindow("重命名", "/fxml/NewGroupView.fxml", root.getScene().getWindow(), BaseWindowController.RENAME);
        controller.editInfo(this.selectedNode);
    }

    /**
     * 删除节点,如果该节点有子节点将递归删除掉
     *
     * @param actionEvent
     */
    @FXML
    public void delete(ActionEvent actionEvent) {
        String message = null;
        if (this.selectedNode.isConnection()) {
            message = "确认删除连接?";
        } else {
            if (treeView.getSelectionModel().getSelectedItem().getChildren().isEmpty()) {
                message = "确认删除分组?";
            } else {
                message = "确认删除分组及其所有连接?";
            }
        }
        if (GuiUtil.alert(Alert.AlertType.CONFIRMATION, message)) {
            Applications.deleteConnectionOrGroup(treeView.getSelectionModel().getSelectedItem());
            treeView.getSelectionModel().getSelectedItem().getParent().getChildren().remove(treeView.getSelectionModel().getSelectedItem());
        }
    }

    /**
     * 节点名称修改
     *
     * @param name
     */
    public void updateNodeName(String name) {
        this.selectedNode.setName(name);
        treeView.refresh();
    }

    /**
     * 节点信息更新
     * 主要是针对连接而不是分组
     *
     * @param connectionServerNode
     */
    public void updateNodeInfo(ConnectionServerNode connectionServerNode) {
        this.selectedNode.setName(connectionServerNode.getName());
        this.selectedNode.setHost(connectionServerNode.getHost());
        this.selectedNode.setPort(connectionServerNode.getPort());
        this.selectedNode.setAuth(connectionServerNode.getAuth());
        treeView.refresh();
    }

    /**
     * todo
     * @param actionEvent
     */
    public void connect(ActionEvent actionEvent) {
        try {
            RedisConfig redisConfig = new RedisConfig();
            redisConfig.setHost(this.selectedNode.getHost());
            redisConfig.setPort(this.selectedNode.getPort());
            redisConfig.setAuth(this.selectedNode.getAuth());
            RedisContext redisContext = RedisFactorySingleton.getInstance().createRedisContext(redisConfig);
            Message message = redisContext.newRedisClient().testConnect();
            if (!message.isSuccess()) {
                GuiUtil.alert(Alert.AlertType.WARNING, message.getMessage());
                return;
            }
            super.currentStage.close();
            super.parentController.newRedisTab(redisContext,this.selectedNode.getName());
        }catch (Exception e){
            log.error("连接错误",e);
            GuiUtil.alert(Alert.AlertType.ERROR,e.getMessage());
        }

    }
}
