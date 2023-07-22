package xyz.hashdog.rdm.ui.controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import xyz.hashdog.rdm.common.pool.ThreadPool;
import xyz.hashdog.rdm.redis.RedisContext;
import xyz.hashdog.rdm.redis.client.RedisClient;
import xyz.hashdog.rdm.ui.common.Applications;
import xyz.hashdog.rdm.ui.entity.DBNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ServerTabController extends BaseController<MainController> {


    /**
     * 根节点有绑定RedisContext
     */
    @FXML
    public AnchorPane root;
    /**
     * 搜索的内容
     */
    @FXML
    public TextField searchText;
    @FXML
    private TreeView<String> treeView;
    @FXML
    private ChoiceBox<DBNode> choiceBox;
    /**
     * redis上下文,由父类传递绑定
     */
    private RedisContext redisContext;
    /**
     * 当前控制层操作的tab所用的redis客户端连接
     */
    private RedisClient redisClient;


    @FXML
    public void initialize() {
        initAutoWah();
        initListener();
    }

    /**
     * 初始化监听时间
     */
    private void initListener() {
        userDataPropertyListener();
        choiceBoxSelectedLinstener();
        initTreeViewMultiple();

    }



    /**
     * db选择框监听
     * db切换后,更新key节点
     */
    private void choiceBoxSelectedLinstener() {
        choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            int db = newValue.getDb();
            Future<Boolean> submit = ThreadPool.getInstance().submit(() -> this.redisClient.select(db), true);
            try {
                if(submit.get()){
                    search(null);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }

//            ThreadPool.getInstance().execute(() -> {
//                this.redisClient.select(db);
//                search(null);
//            });
        });
    }

    /**
     * 父层传送的数据监听
     * 监听到进行db选择框的初始化
     */
    private void userDataPropertyListener() {
        super.userDataProperty.addListener((observable, oldValue, newValue) -> {
            this.redisContext = (RedisContext) newValue;
            this.redisClient = this.redisContext.newRedisClient();
            initDBSelects();
        });
    }

    /**
     * 初始化db选择框
     */
    private void initDBSelects() {
        ObservableList<DBNode> items = choiceBox.getItems();
        ThreadPool.getInstance().execute(() -> {
            Map<Integer, String> map = this.redisClient.dbSize();
            Platform.runLater(() -> {
                for (Map.Entry<Integer, String> en : map.entrySet()) {
                    items.add(new DBNode(en.getValue(), en.getKey()));
                }
                choiceBox.setValue(items.get(0));
            });
        });

    }

    /**
     * 节点多选设置
     */
    private void initTreeViewMultiple() {
        // 启用多选功能
        treeView.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
        //shift 或则ctrol+鼠标单机为选取操作,会触发选中,选择父节点会同步选中子节点
        treeView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) { // Check for single click
                if (event.isShiftDown() || event.isControlDown()) {
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
        treeView.setRoot(new TreeItem<>());
        // 自动展开根节点
        treeView.setShowRoot(false); // 隐藏根节点
        //默认根节点为选中节点
        treeView.getSelectionModel().select(treeView.getRoot());
    }

    /**
     * 数据已经有了,直接更新到视图
     */
    private void initTreeView(List<String> keys) {

        ObservableList<TreeItem<String>> children = treeView.getRoot().getChildren();
        children.clear();
        for (String key : keys) {
            children.add(new TreeItem<>(key, Applications.creatKeyImageView()));

        }
    }





    /**
     * 自适应宽高
     */
    private void initAutoWah() {
        // 设置ChoiceBox的宽度自适应
        choiceBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(choiceBox, javafx.scene.layout.Priority.ALWAYS);
    }

    // Recursive method to select all children of a parent node
    private void selectChildren(TreeItem<String> parent) {
        if (parent == null) {
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

    /**
     * 新增key
     * @param actionEvent
     */
    public void newKey(ActionEvent actionEvent) {

    }

    /**
     * 模糊搜索
     * @param actionEvent
     */
    public void search(ActionEvent actionEvent) {
        ThreadPool.getInstance().execute(()->{
            List<String> keys = this.redisClient.scanAll(searchText.getText());
            Platform.runLater(()->{
                //key已经查出来,只管展示
                initTreeView(keys);
            });
        });
    }

    /**
     * 打开key
     * @param actionEvent
     */
    public void open(ActionEvent actionEvent) {
    }

    /**
     * 删除key
     * @param actionEvent
     */
    public void delete(ActionEvent actionEvent) {
    }

    /**
     * 清空
     * @param actionEvent
     */
    public void flush(ActionEvent actionEvent) {

    }
}
