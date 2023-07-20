package xyz.hashdog.rdm.ui.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import xyz.hashdog.rdm.ui.common.Applications;
import xyz.hashdog.rdm.ui.entity.ConnectionServerNode;

import java.io.IOException;

public class ServerConnectionsController {
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



    @FXML
    public void initialize() {
        initTreeView();
        initListener();

    }

    private void initListener() {
        treeViewListener();
    }

    private void treeViewListener() {
        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            boolean isLeafNode = newValue != null && newValue.isLeaf();
            // 使用选择器获取一组按钮,叶子节点才能连接,非叶子节点才能新建分组和新建连接
            buttonsHBox.lookupAll(".isLeafNode").forEach(node -> {
                Button button = (Button) node;
                button.setDisable(!isLeafNode);
            });
            buttonsHBox.lookupAll(".isNotLeafNode").forEach(node -> {
                Button button = (Button) node;
                button.setDisable(isLeafNode);
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
            });
            //连接按钮禁用否
            bottomConnectButton.setDisable(!isLeafNode);

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
    public void newConnection(ActionEvent actionEvent) throws IOException {
        Stage newConnctionWindowStage = new Stage();
        newConnctionWindowStage.initModality(Modality.WINDOW_MODAL);
        //去掉最小化和最大化
        newConnctionWindowStage.initStyle(StageStyle.DECORATED);
        //禁用掉最大最小化
        newConnctionWindowStage.setMaximized(false);
        newConnctionWindowStage.setTitle("新建连接");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/NewConnectionView.fxml"));
        AnchorPane borderPane = fxmlLoader.load();
        NewConnectionController controller = fxmlLoader.getController();
        controller.setParentController(this);
        controller.setCurrentStage(newConnctionWindowStage);
        Scene scene = new Scene(borderPane);
        newConnctionWindowStage.initOwner(root.getScene().getWindow());
        newConnctionWindowStage.setScene(scene);
        newConnctionWindowStage.show();


    }

    /**
     * 新增树节点,并选中
     * @param connectionServerNode
     */
    public void AddConnectionNodeAndSelect(ConnectionServerNode connectionServerNode) {
    }
}
