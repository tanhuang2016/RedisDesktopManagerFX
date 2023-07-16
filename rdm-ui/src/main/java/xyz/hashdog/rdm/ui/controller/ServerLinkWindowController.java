package xyz.hashdog.rdm.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class ServerLinkWindowController {


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
}
