package xyz.hashdog.rdm.ui.controller;

import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import xyz.hashdog.rdm.common.pool.ThreadPool;
import xyz.hashdog.rdm.common.tuple.Tuple2;
import xyz.hashdog.rdm.redis.RedisContext;
import xyz.hashdog.rdm.ui.Main;
import xyz.hashdog.rdm.ui.common.Constant;
import xyz.hashdog.rdm.ui.entity.PassParameter;
import xyz.hashdog.rdm.ui.sampler.layout.ApplicationWindow;
import xyz.hashdog.rdm.ui.sampler.layout.MainLayer;
import xyz.hashdog.rdm.ui.sampler.layout.MainModel;
import xyz.hashdog.rdm.ui.sampler.layout.Sidebar;
import xyz.hashdog.rdm.ui.sampler.theme.ThemeManager;
import xyz.hashdog.rdm.ui.util.GuiUtil;

import java.io.IOException;
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
    /**
     * 服务连接的Stage
     */
    private Stage serverConnectionsWindowStage;
    /**
     * 设置的stage
     */
    private Stage settingsStage;

    @FXML
    public void initialize() {
        initMenuIconAndKey();
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
            ServerConnectionsController controller = tuple2.getT2();
            Scene scene = new Scene(borderPane);
            this.serverConnectionsWindowStage.initOwner(root.getScene().getWindow());
            this.serverConnectionsWindowStage.setScene(scene);
            this.serverConnectionsWindowStage.show();
            controller.setParentController(this);
            controller.setCurrentStage(serverConnectionsWindowStage);


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
}
