package xyz.hashdog.rdm.ui.controller;

import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import xyz.hashdog.rdm.common.pool.ThreadPool;
import xyz.hashdog.rdm.common.tuple.Tuple2;
import xyz.hashdog.rdm.redis.Message;
import xyz.hashdog.rdm.redis.RedisConfig;
import xyz.hashdog.rdm.redis.RedisContext;
import xyz.hashdog.rdm.redis.RedisFactorySingleton;
import xyz.hashdog.rdm.redis.exceptions.RedisException;
import xyz.hashdog.rdm.ui.Main;
import xyz.hashdog.rdm.ui.common.Constant;
import xyz.hashdog.rdm.ui.entity.PassParameter;
import xyz.hashdog.rdm.ui.sampler.layout.ApplicationWindow;
import xyz.hashdog.rdm.ui.sampler.layout.MainLayer;
import xyz.hashdog.rdm.ui.sampler.layout.MainModel;
import xyz.hashdog.rdm.ui.sampler.layout.Sidebar;
import xyz.hashdog.rdm.ui.sampler.theme.ThemeManager;
import xyz.hashdog.rdm.ui.util.GuiUtil;
import xyz.hashdog.rdm.ui.util.RecentHistory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static javafx.scene.input.KeyCombination.ALT_DOWN;
import static javafx.scene.input.KeyCombination.CONTROL_DOWN;

/**
 * 主控制层
 */
public class MainController extends BaseWindowController {
    @FXML
    public AnchorPane root;
    /**
     * tab页容器
     */
    @FXML
    public TabPane serverTabPane;
    public MenuItem fileOpen;
    public MenuItem fileConnect;
    public MenuItem fileSettings;
    public Menu history;
    public Menu servers;
    /**
     * 服务连接的Stage
     */
    private Stage serverConnectionsWindowStage;
    private ServerConnectionsController serverConnectionsController;
    /**
     * 设置的stage
     */
    private Stage settingsStage;
    private RecentHistory<RedisConfig> recentHistory ;

    @FXML
    public void initialize() {
        initListener();
        initMenuIconAndKey();
        initRecentHistory();
    }

    private void initListener() {
        serverTabPaneListener();
    }

    /**
     * tab页监听器，主要用于更新菜单栏的服务器列表和关闭tab页时更新菜单项
     */
    private void serverTabPaneListener() {
        serverTabPane.getTabs().addListener((ListChangeListener<Tab>) change -> {
            ObservableList<MenuItem> items = servers.getItems();
            while (change.next()) {
                // 添加时，在菜单项前面插入一个新元素
                if (change.wasAdded()) {
                    Tab tab = change.getList().get(change.getTo() - 1);
                    MenuItem menuItem = new MenuItem(tab.getText());
                    menuItem.setUserData(tab);
                    menuItem.setOnAction(event -> {
                        Object userData = menuItem.getUserData();
                        GuiUtil.closeTab(serverTabPane, (Tab) userData);
                    });
                    items.addFirst(menuItem);
                    // 删除时，删除对应的菜单项
                } else if (change.wasRemoved()) {
                    for (int i = 0; i < items.size()-2; i++) {
                        MenuItem item= items.get(i);
                        if (item.getUserData() == change.getRemoved().get(0)) {
                            items.remove(item);
                            break;
                        }
                    }
                }
            }
        });
    }

    private void initRecentHistory() {
        //搜索记录 未做持久化 todo
        List<RedisConfig> rc = new ArrayList<>();
        recentHistory = new RecentHistory<RedisConfig>(5,rc,new RecentHistory.Noticer<RedisConfig>() {

            @Override
            public void notice(List<RedisConfig> list) {
                doRecentHistory(list);
            }
        });
        doRecentHistory(recentHistory.get());
    }

    /**
     * 刷新历史记录
     * @param list
     */
    private void doRecentHistory(List<RedisConfig> list) {
        ObservableList<MenuItem> items = history.getItems();
        //为了一致性，直接清空在重新赋值，虽然单个元素增加会减少消耗，但是复杂度增加，暂时不考虑
        items.remove(0,items.size() - 2);
        List<RedisConfig> reversed = list.reversed();
        for (RedisConfig rc : reversed) {
            items.addFirst(createHistoryMenuItem(rc));

        }
    }

    /**
     * 创建历史菜单项
     * @param str
     * @return
     */
    private MenuItem createHistoryMenuItem(RedisConfig str) {
        MenuItem menuItem = new MenuItem(str.getName());
        menuItem.setUserData(str);
        menuItem.setOnAction(event -> {
            newRedisTab((RedisConfig)menuItem.getUserData());
        });
        return menuItem;
    }

    /**
     * 初始化菜单图标和快捷键
     */
    private void initMenuIconAndKey() {
        GuiUtil.setIconAndKey(fileOpen,new FontIcon(Feather.FOLDER),new KeyCodeCombination(KeyCode.O, CONTROL_DOWN));
        GuiUtil.setIconAndKey(fileConnect,new FontIcon(Feather.LINK),new KeyCodeCombination(KeyCode.C, ALT_DOWN));
        GuiUtil.setIconAndKey(fileSettings,new FontIcon(Feather.SETTINGS),new KeyCodeCombination(KeyCode.Q, ALT_DOWN));
    }

    @FXML
    public void openServerLinkWindo(ActionEvent actionEvent)   {
        if(this.serverConnectionsWindowStage!=null){
            if(this.serverConnectionsWindowStage.isShowing()){
                //已经设置为WINDOW_MODAL模式,子窗口未关闭是不能操作的,所以子窗口显示在最上方的操作已经没有意义
//                serverConnectionsWindowStage.setAlwaysOnTop(true);
//                serverConnectionsWindowStage.setAlwaysOnTop(false);

            }else {
                serverConnectionsWindowStage.show();
            }
        }else{
            this.serverConnectionsWindowStage=new Stage();
            serverConnectionsWindowStage.initModality(Modality.WINDOW_MODAL);
            this.serverConnectionsWindowStage.setTitle(Main.RESOURCE_BUNDLE.getString(Constant.MAIN_FILE_CONNECT));

            Tuple2<AnchorPane,ServerConnectionsController> tuple2 = loadFXML("/fxml/ServerConnectionsView.fxml");
            AnchorPane borderPane =tuple2.getT1();
            serverConnectionsController = tuple2.getT2();
            Scene scene = new Scene(borderPane);
            this.serverConnectionsWindowStage.initOwner(root.getScene().getWindow());
            this.serverConnectionsWindowStage.setScene(scene);
            this.serverConnectionsWindowStage.show();
            serverConnectionsController.setParentController(this);
            serverConnectionsController.setCurrentStage(serverConnectionsWindowStage);


        }

    }

    /**
     * 创建新的tab页
     *
     * @param redisContext redis上下文
     * @param name 服务名称
     */
    public void newRedisTab(RedisContext redisContext, String name) throws IOException {
        Tuple2<AnchorPane,ServerTabController> tuple2 = loadFXML("/fxml/ServerTabView.fxml");
        AnchorPane borderPane = tuple2.getT1();
        ServerTabController controller = tuple2.getT2();
        controller.setParentController(this);
        PassParameter passParameter = new PassParameter(PassParameter.REDIS);
        passParameter.setRedisContext(redisContext);
        passParameter.setRedisClient(redisContext.newRedisClient());
        controller.setParameter(passParameter);
        Tab tab = new Tab(name);
        tab.setContent(borderPane);
        this.serverTabPane.getTabs().add(tab);
        this.serverTabPane.getSelectionModel().select(tab);
        tab.setGraphic(GuiUtil.creatConnctionImageView());

        if(passParameter.getTabType()== PassParameter.REDIS){
            // 监听Tab被关闭事件,但是remove是无法监听的
            tab.setOnClosed(event2 -> {
                ThreadPool.getInstance().execute(()->controller.getRedisContext().close());
            });
        }
        ContextMenu cm= GuiUtil.newTabContextMenu(tab);
        //写入最近连接记录
        recentHistory.add(redisContext.getRedisConfig());
    }

    /**
     * 新建redis连接tab页
     * @param redisConfig
     * @throws IOException
     */
    public void newRedisTab(RedisConfig redisConfig)  {
        RedisContext redisContext = RedisFactorySingleton.getInstance().createRedisContext(redisConfig);
        Message message = redisContext.newRedisClient().testConnect();
        if (!message.isSuccess()) {
            GuiUtil.alert(Alert.AlertType.WARNING, message.getMessage());
            return;
        }
        try {
            this.newRedisTab(redisContext,redisConfig.getName());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RedisException(e.getMessage());
        }
    }

    /**
     * 打开设置窗口
     * @param actionEvent
     */
    public void openSettings(ActionEvent actionEvent) {
        if(this.settingsStage!=null){
            if(this.settingsStage.isShowing()){
            }else {
                settingsStage.show();
            }
        }else{
            this.settingsStage=new Stage();
            settingsStage.initModality(Modality.WINDOW_MODAL);
            this.settingsStage.setTitle(Main.RESOURCE_BUNDLE.getString(Constant.MAIN_FILE_CONNECT));

//            Tuple2<AnchorPane,SettingsController> tuple2 = loadFXML("/fxml/AdvancedPage.fxml");
//            AnchorPane borderPane =tuple2.getT1();
            ApplicationWindow applicationWindow = new ApplicationWindow();
//            SettingsController controller = tuple2.getT2();
            var antialiasing = Platform.isSupported(ConditionalFeature.SCENE3D)
                    ? SceneAntialiasing.BALANCED
                    : SceneAntialiasing.DISABLED;
            Scene scene = new Scene(applicationWindow,ApplicationWindow.MIN_WIDTH + 80, 768, false, antialiasing);
            ThemeManager TM = ThemeManager.getInstance();
            TM.setScene(scene);
            TM.setTheme(TM.getDefaultTheme());
            this.settingsStage.initOwner(root.getScene().getWindow());
            this.settingsStage.setScene(scene);
            this.settingsStage.show();
//            controller.setParentController(this);
//            controller.setCurrentStage(settingsStage);


        }
    }

    /**
     * 清除所有最近连接记录
     * @param actionEvent
     */
    public void clearHistory(ActionEvent actionEvent) {
        this.recentHistory.clear();
    }

    /**
     * 关闭tabPan当前服务
     * @param actionEvent
     */
    public void closeCurrentServer(ActionEvent actionEvent) {
        Tab selectedItem = this.serverTabPane.getSelectionModel().getSelectedItem();
        if(selectedItem!=null){
            GuiUtil.closeTab(this.serverTabPane,selectedItem);
        }
    }

    /**
     * 关闭所有服务
     * @param actionEvent
     */
    public void closeServerAll(ActionEvent actionEvent) {
        ObservableList<Tab> tabs = this.serverTabPane.getTabs();
        GuiUtil.closeTab(this.serverTabPane,new ArrayList<>(tabs));
    }

    /**
     * 快速新建连接
     * @param actionEvent
     */
    public void newConnection(ActionEvent actionEvent) throws IOException {
        openServerLinkWindo(actionEvent);
        serverConnectionsController.newConnection(actionEvent);
    }

    /**
     * 快速新建分组
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    public void newGroup(ActionEvent actionEvent) throws IOException {
        openServerLinkWindo(actionEvent);
        serverConnectionsController.newGroup(actionEvent);
    }

    /**
     * 快速连接
     * @param actionEvent
     */
    public void quickConnection(ActionEvent actionEvent) throws IOException {
        openServerLinkWindo(actionEvent);
        serverConnectionsController.quickConnection();
    }

    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void Maximized(ActionEvent actionEvent) {
        currentStage.setMaximized(true);
    }

    public void Minimized(ActionEvent actionEvent) {
        currentStage.setIconified(true);

    }
}
