package xyz.hashdog.rdm.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * 主控制层
 */
public class MainController extends BaseController{
    @FXML
    public AnchorPane root;
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
//            this.searchStage.initStyle(StageStyle.TRANSPARENT);
            this.serverConnectionsWindowStage.show();
            controller.setParentController(this);
            controller.setCurrentStage(serverConnectionsWindowStage);


        }

    }
}
