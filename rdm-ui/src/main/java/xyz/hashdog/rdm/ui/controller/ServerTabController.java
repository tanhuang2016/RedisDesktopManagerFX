package xyz.hashdog.rdm.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import xyz.hashdog.rdm.redis.RedisContext;

import java.util.ArrayList;
import java.util.List;

public class ServerTabController extends BaseController<MainController>{


    /**
     * 根节点有绑定RedisContext
     */
    @FXML
    public AnchorPane root;
    @FXML
    private TreeView<String> treeView;
    @FXML
    private ChoiceBox<String> choiceBox;
    /**
     * redis上下文,由父类传递绑定
     */
    private RedisContext redisContext;


    @FXML
    public void initialize() {
        initAutoWah();
        initListener();
    }

    /**
     * 初始化监听时间
     */
    private void initListener() {
        super.userDataProperty.addListener((observable, oldValue, newValue) -> {
            this.redisContext=(RedisContext)newValue;
            initTreeView();
        });
    }


    private void initTreeView() {

        // 启用多选功能
        treeView.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
        //shift 或则ctrol+鼠标单机为选取操作,会触发选中,选择父节点会同步选中子节点
        treeView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) { // Check for single click
                if(event.isShiftDown()||event.isControlDown()){
                    System.out.println(222);
                    List<TreeItem<String>> list = new ArrayList<>();
                    for (TreeItem<String> selectedItem : treeView.getSelectionModel().getSelectedItems()) {
                        if (!selectedItem.isLeaf()) { // Check if the selected node is a parent node
                            list.add(selectedItem);

                        }
                    }
                    for (TreeItem<String> selectedItem : list) {
                        // If the selected node is a parent node, select all its children
                        selectChildren((TreeItem<String>) selectedItem);
                    }
                }

            }
        });


        TreeItem<String> rootItem = treeView.getRoot();
        rootItem.setValue("");

        // 创建第一个子节点及其后代节点
        CheckBoxTreeItem<String> item1 = new CheckBoxTreeItem<>("a");
        item1.getChildren().addAll(
                new CheckBoxTreeItem<>("a:123"),
                new CheckBoxTreeItem<>("a:124"),
                new CheckBoxTreeItem<>("a:125")
        );

        // 创建第二个子节点及其后代节点
        CheckBoxTreeItem<String> item2 = new CheckBoxTreeItem<>("b");
        item2.getChildren().addAll(
                new CheckBoxTreeItem<>("b:123"),
                new CheckBoxTreeItem<>("b:124"),
                new CheckBoxTreeItem<>("b:125")
        );
        // 将子节点添加到根节点
        rootItem.getChildren().addAll(item1, item2);

        // 设置TreeCell的显示方式为CheckBoxTreeCell
//        treeView.setCellFactory(CheckBoxTreeCell.<String>forTreeView());


        // 自动展开根节点
        treeView.setShowRoot(false); // 隐藏根节点
    }


    private void initAutoWah() {
        // 设置ChoiceBox的宽度自适应
        choiceBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(choiceBox, javafx.scene.layout.Priority.ALWAYS);
    }

    // Recursive method to select all children of a parent node
    private void selectChildren(TreeItem<String> parent) {
        if (parent == null){
            return;
        }
        if (!parent.isLeaf()) {
            parent.setExpanded(true); // Optional: Expand the parent to show all children
            for (TreeItem<String> child : parent.getChildren()) {
                treeView.getSelectionModel().select(child);
                selectChildren(child); // Recursively select children of the child node
            }
        }
    }

}
