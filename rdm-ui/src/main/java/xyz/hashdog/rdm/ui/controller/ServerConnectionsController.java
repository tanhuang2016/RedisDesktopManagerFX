package xyz.hashdog.rdm.ui.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import xyz.hashdog.rdm.ui.common.Applications;
import xyz.hashdog.rdm.ui.entity.ConnectionServerNode;

import java.io.IOException;

/**
 * 服务连接控制层
 * @author th
 */
public class ServerConnectionsController extends BaseController<MainController> {
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
        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue==null){
                newValue=treeView.getRoot();
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
            this.selectedNode=newValue.getValue();

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
        expandAllNodes(treeView.getRoot());
        //默认根节点为选中节点
        treeView.getSelectionModel().select(treeView.getRoot());
    }

    /**
     * 初始化树节点的数据
     */
    private void initTreeViewData() {
        TreeItem<ConnectionServerNode> rootItem=Applications.initConnectionTreeView();
        treeView.setRoot(rootItem);
    }

    /**
     * 设置树节点的显示方式
     */
    private void initTreeViewCellFactory() {
        treeView.setCellFactory(new Callback<TreeView<ConnectionServerNode>, TreeCell<ConnectionServerNode>>() {
            @Override
            public TreeCell<ConnectionServerNode> call(TreeView<ConnectionServerNode> param) {

                return new TreeCell<ConnectionServerNode>(){
                    @Override
                    protected void updateItem(ConnectionServerNode item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(item.getName());
                        }
                    }
                };
            }
        });
    }

    // Method to expand all nodes in the tree recursively
    private void expandAllNodes(TreeItem<?> item) {
        if (item != null && !item.isLeaf()) {
            item.setExpanded(true);
            for (TreeItem<?> child : item.getChildren()) {
                expandAllNodes(child);
            }
        }
    }

    /**
     * 新建连接
     * 每次打开新窗口,所以Stage不用缓存
     * @param actionEvent
     */
    @FXML
    public void newConnection(ActionEvent actionEvent) throws IOException {
        super.loadSubWindow("新建连接","/fxml/NewConnectionView.fxml",root.getScene().getWindow());
    }

    /**
     * 新增树节点,并选中
     * @param connectionServerNode
     */
    public void AddConnectionOrGourpNodeAndSelect(ConnectionServerNode connectionServerNode) {
        TreeItem<ConnectionServerNode> connectionServerNodeTreeItem = new TreeItem<>(connectionServerNode);
        if(connectionServerNode.getParentDataId().equals(Applications.ROOT_ID)){
            treeView.getRoot().getChildren().add(connectionServerNodeTreeItem);
        }else {
            TreeItem<ConnectionServerNode> selectedItem = treeView.getSelectionModel().getSelectedItem();
            selectedItem.getChildren().add(connectionServerNodeTreeItem);
        }
        treeView.getSelectionModel().select(connectionServerNodeTreeItem);
    }

    @FXML
    public void newGroup(ActionEvent actionEvent) throws IOException {
        super.loadSubWindow("新建分组","/fxml/NewGroupView.fxml",root.getScene().getWindow());
    }


    /**
     * 获取被选中节点的id
     * @return
     */
    public String getSelectedDataId() {
        return this.selectedNode.getDataId();
    }

    /**
     * 编辑节点
     * @param actionEvent
     */
    @FXML
    public void edit(ActionEvent actionEvent) throws IOException {
        if(this.selectedNode.isConnection()){
            NewConnectionController controller = super.loadSubWindow("新建连接","/fxml/NewConnectionView.fxml",root.getScene().getWindow());
            controller.editInfo(this.selectedNode);
        }else {
            NewGroupController controller = super.loadSubWindow("新建分组","/fxml/NewGroupView.fxml",root.getScene().getWindow());
            controller.editInfo(this.selectedNode);
        }


    }

    /**
     * 节点重新命名
     * @param actionEvent
     */
    @FXML
    public void rename(ActionEvent actionEvent) {
    }

    /**
     * 删除节点,如果该节点有子节点将递归删除掉
     * @param actionEvent
     */
    @FXML
    public void delete(ActionEvent actionEvent) {
    }

    /**
     * 节点名称修改
     * @param name
     */
    public void updateNodeName(String name) {
        this.selectedNode.setName(name);
        treeView.refresh();
    }
}
