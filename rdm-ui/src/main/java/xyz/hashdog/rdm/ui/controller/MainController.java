package xyz.hashdog.rdm.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import xyz.hashdog.rdm.redis.RedisContext;

import java.io.IOException;

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
    /**
     * 服务连接的Stage
     */
    private Stage serverConnectionsWindowStage;

    public void openServerLinkWindo(ActionEvent actionEvent) throws IOException {
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
            this.serverConnectionsWindowStage.setTitle("link");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ServerConnectionsView.fxml"));
            AnchorPane borderPane = fxmlLoader.load();
            ServerConnectionsController controller = fxmlLoader.getController();
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
        FXMLLoader fxmlLoader = loadFXML("/fxml/ServerTabView.fxml");
        AnchorPane borderPane = fxmlLoader.load();
        ServerTabController controller = fxmlLoader.getController();
        controller.setParentController(this);
        controller.setUserDataProperty(redisContext);
        Tab tab = new Tab(name);
        tab.setContent(borderPane);
        this.serverTabPane.getTabs().add(tab);
    }
}
