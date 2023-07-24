package xyz.hashdog.rdm.ui.controller;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import xyz.hashdog.rdm.common.pool.ThreadPool;
import xyz.hashdog.rdm.ui.common.RedisDataTypeEnum;
import xyz.hashdog.rdm.ui.entity.DBNode;
import xyz.hashdog.rdm.ui.entity.PassParameter;
import xyz.hashdog.rdm.ui.util.GuiUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ServerTabController extends BaseKeyController<MainController> {



    /**
     * 搜索的内容
     */
    @FXML
    public TextField searchText;

    /**
     * 右键菜单
     */
    @FXML
    public ContextMenu contextMenu;
    @FXML
    private TreeView<String> treeView;
    @FXML
    private ChoiceBox<DBNode> choiceBox;
    /**
     * tab页容器
     */
    @FXML
    public TabPane dbTabPane;



    /**
     * 最后一个选中节点
     */
    private TreeItem<String> lastSelectedNode;


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
        initTreeViewRoot();
        treeViewListener();


    }


    /**
     * 根节点初始化一个空的
     */
    private void initTreeViewRoot() {
        treeView.setRoot(new TreeItem<>());
        // 自动展开根节点
        treeView.setShowRoot(false); // 隐藏根节点
        //默认根节点为选中节点
        treeView.getSelectionModel().select(treeView.getRoot());
    }

    /**
     * key树的监听
     */
    private void treeViewListener() {
        initTreeViewMultiple();
        buttonIsShowAndSetSelectNode();
        doubleClicked();
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
            //叶子节点是连接
            boolean isLeafNode = newValue.isLeaf();
            //是否为根
            boolean isRoot = newValue.getParent() == null;
            // 右键菜单显示/隐藏
            ObservableList<MenuItem> items = contextMenu.getItems();
            items.forEach(menuItem -> {
                if (menuItem.getStyleClass().contains("isLeafNode")) {
                    menuItem.setVisible(isLeafNode);
                }
                if (menuItem.getStyleClass().contains("isNotRoot")) {
                    menuItem.setVisible(!isRoot);
                }
            });
            //设置最后一个选中节点
            this.lastSelectedNode = newValue;

        });
    }

    /**
     * treeView双击事件
     * 如果双击节点为连接,则打开链接
     */
    private void doubleClicked() {
        // 添加鼠标点击事件处理器
        treeView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                // 获取选中的节点
                TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
                if (selectedItem != null && selectedItem.isLeaf()) {
                    try {
                        open(null);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    /**
     * db选择框监听
     * db切换后,更新key节点
     */
    private void choiceBoxSelectedLinstener() {
        choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.currentDb=newValue.getDb();
            Future<Boolean> submit = ThreadPool.getInstance().submit(() -> this.redisClient.select(this.currentDb), true);
            try {
                if (submit.get()) {
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
        super.parameter.addListener((observable, oldValue, newValue) -> {
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
                //默认选中第一个
                choiceBox.setValue(choiceBox.getItems().get(0));
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
                        //设置选中
                        selectChildren((TreeItem<String>) selectedItem);
                    }
                }

            }
        });
    }

    /**
     * 数据已经有了,直接更新到视图
     */
    private void initTreeView(List<String> keys) {

        ObservableList<TreeItem<String>> children = treeView.getRoot().getChildren();
        children.clear();
        for (String key : keys) {
            children.add(new TreeItem<>(key, GuiUtil.creatKeyImageView()));

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

    /**
     * 选中父节点就把子节点全选
     *
     * @param parent
     */
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
     *
     * @param actionEvent
     */
    public void newKey(ActionEvent actionEvent) {

    }

    /**
     * 模糊搜索
     *
     * @param actionEvent
     */
    public void search(ActionEvent actionEvent) {
        ThreadPool.getInstance().execute(() -> {
            List<String> keys = exeRedis(j -> j.scanAll(searchText.getText()));
            Platform.runLater(() -> {
                //key已经查出来,只管展示
                initTreeView(keys);
            });
        });
    }

    /**
     * 打开key
     *
     * @param actionEvent
     */
    public void open(ActionEvent actionEvent) throws IOException {
        String key = this.lastSelectedNode.getValue();
        String type = exeRedis(j -> j.type(key));
        RedisDataTypeEnum te = RedisDataTypeEnum.getByType(type);
        FXMLLoader fxmlLoader = loadFXML(te.fxml);
        AnchorPane borderPane = fxmlLoader.load();
        BaseKeyController controller = fxmlLoader.getController();
        controller.setParentController(this);
        PassParameter passParameter = new PassParameter(PassParameter.STRING);
        passParameter.setDb(this.currentDb);
        passParameter.setKey(key);
        passParameter.setRedisClient(redisClient);
        passParameter.setRedisContext(redisContext);
        StringProperty keySend = passParameter.keyProperty();
        //操作的kye和子界面进行绑定,这样更新key就会更新树节点
        this.lastSelectedNode.valueProperty().bind(keySend);
        controller.setParameter(passParameter);
        Tab tab = new Tab(String.format("%s|%s|%s", this.currentDb,type, key));
        ContextMenu cm=GuiUtil.newTabContextMenu(tab);
        tab.setContent(borderPane);
        this.dbTabPane.getTabs().add(tab);
        this.dbTabPane.getSelectionModel().select(tab);

    }

    /**
     * 控制台
     * @param actionEvent
     */
    @FXML
    public void console(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = loadFXML("/fxml/ConsoleView.fxml");
        AnchorPane anchorPane = fxmlLoader.load();

        ConsoleController controller = fxmlLoader.getController();
        controller.setParentController(this);
        PassParameter passParameter = new PassParameter(PassParameter.CONSOLE);
        passParameter.setDb(this.currentDb);
        passParameter.setRedisClient(redisContext.newRedisClient());
        passParameter.setRedisContext(redisContext);
        controller.setParameter(passParameter);
        Tab tab = new Tab("console");
        if(passParameter.getTabType()==PassParameter.CONSOLE){
            // 监听Tab被关闭事件,但是remove是无法监听的
            tab.setOnClosed(event2 -> {
                ThreadPool.getInstance().execute(()->controller.getRedisClient().close());
            });
        }
        ContextMenu cm=GuiUtil.newTabContextMenu(tab);
        tab.setContent(anchorPane);
        this.dbTabPane.getTabs().add(tab);
        this.dbTabPane.getSelectionModel().select(tab);
    }

    /**
     * 删除key,包括多选的
     *
     * @param actionEvent
     */
    public void delete(ActionEvent actionEvent) {
        if(!GuiUtil.alert(Alert.AlertType.CONFIRMATION,"确认删除?" )){
            return;
        }
        List<String> delKeys=new ArrayList<>();
        // 获取选中的节点
        List<TreeItem<String>> delItems =new ArrayList<>();
        treeView.getSelectionModel().getSelectedItems().forEach(item -> {
            if (item != treeView.getRoot()) {
                //叶子节点是连接,需要删除redis上的key
                if(item.isLeaf()){
                    delKeys.add(item.getValue());
                }
                delItems.add(item);
            }
        });
        //删除tree节点,得从新用list装一次再遍历删除,否则会有安全问题
        for (TreeItem<String> delItem : delItems) {
            delItem.getParent().getChildren().remove(delItem); // 将选中的节点从父节点的子节点列表中移除
        }

        //删除服务器的key
        asynexec(()->{
            exeRedis(j -> j.del(delKeys.toArray(new String[delKeys.size()])));
        });

        //删除对应打开的tab
        removeTabByKeys(delKeys);



    }

    /**
     * 删除对应key的tab
     * @param delKeys
     */
    public void removeTabByKeys(List<String> delKeys) {
        List<Tab> delTabs = new ArrayList<>();
        for (Tab tab : dbTabPane.getTabs()) {
            BaseKeyController controller =(BaseKeyController) tab.getContent().getUserData();
            String key = controller.getParameter().getKey();
            if(delKeys.contains(key)){
                delTabs.add(tab);
            }
        }
        dbTabPane.getTabs().removeAll(delTabs);
    }

    /**
     * 清空
     *
     * @param actionEvent
     */
    public void flush(ActionEvent actionEvent) {
        if(!GuiUtil.alert(Alert.AlertType.CONFIRMATION,"确认清空?" )){
            return;
        }
        asynexec(()->{
            exeRedis(j -> j.flushDB());
            Platform.runLater(()->{
                treeView.getRoot().getChildren().clear();
            });
        });
    }



    /**
     * 删除单个treeView对应的key,由子层调用
     * @param p
     * @return
     */
    public boolean delKey(ObjectProperty<PassParameter> p) {
        //如果treeView是的db和删除key的db相同,则需要对应删除treeView中的节点
        if(p.get().getDb()==this.currentDb){
            Platform.runLater(()->{
                GuiUtil.deleteTreeNodeByKey(treeView.getRoot(),p.get().getKey());
            });
        }
        return true;
    }



}
