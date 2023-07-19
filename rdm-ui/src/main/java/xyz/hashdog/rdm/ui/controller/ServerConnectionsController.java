package xyz.hashdog.rdm.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ServerConnectionsController {
    @FXML
    public AnchorPane root;
    private Stage newConnctionWindowStage;

    @FXML
    public TreeView<String> treeView;


    @FXML
    public void initialize() {
        initTreeView();

    }

    private void initTreeView() {
        TreeItem<String> rootItem = treeView.getRoot();
        rootItem.setValue("");

        // 创建第一个子节点及其后代节点
        TreeItem<String> item1 = new TreeItem<>("开发环境");
        item1.getChildren().addAll(
                new TreeItem<>("redis server1"),
                new TreeItem<>("redis server2"),
                new TreeItem<>("redis server3")
        );

        // 创建第二个子节点及其后代节点
        TreeItem<String> item2 = new TreeItem<>("测试环境");
        item2.getChildren().addAll(
                new TreeItem<>("redis server1"),
                new TreeItem<>("redis server2"),
                new TreeItem<>("redis server3")
        );
        // 将子节点添加到根节点
        rootItem.getChildren().addAll(item1, item2);
        // 自动展开根节点
        treeView.setShowRoot(false); // 隐藏根节点
        // Expand all nodes
        expandAllNodes(rootItem);
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
     * @param actionEvent
     */
    public void newConnection(ActionEvent actionEvent) throws IOException {
        if(this.newConnctionWindowStage!=null){
            newConnctionWindowStage.show();
        }else{
            this.newConnctionWindowStage=new Stage();
            newConnctionWindowStage.initModality(Modality.WINDOW_MODAL);
            //去掉最小化和最大化
            newConnctionWindowStage.initStyle(StageStyle.DECORATED);
            //禁用掉最大最小化
            newConnctionWindowStage.setMaximized(false);
            this.newConnctionWindowStage.setTitle("新建连接");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/NewConnectionView.fxml"));
            AnchorPane borderPane = fxmlLoader.load();
            NewConnectionController controller = fxmlLoader.getController();
            Scene scene = new Scene(borderPane);
            this.newConnctionWindowStage.initOwner(root.getScene().getWindow());
            this.newConnctionWindowStage.setScene(scene);
            this.newConnctionWindowStage.show();


        }
    }
}
