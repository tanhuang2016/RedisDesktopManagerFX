package xyz.hashdog.rdm.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    @FXML
    public AnchorPane root;
    private Stage serverLinkWindowStage;

    public void openServerLinkWindo(ActionEvent actionEvent) throws IOException {
        if(this.serverLinkWindowStage!=null){
            if(this.serverLinkWindowStage.isShowing()){
                //已经设置为WINDOW_MODAL模式,子窗口未关闭是不能操作的,所以子窗口显示在最上方的操作已经没有意义
//                serverLinkWindowStage.setAlwaysOnTop(true);
//                serverLinkWindowStage.setAlwaysOnTop(false);

            }else {
                serverLinkWindowStage.show();
            }
        }else{
            this.serverLinkWindowStage=new Stage();
            serverLinkWindowStage.initModality(Modality.WINDOW_MODAL);
            this.serverLinkWindowStage.setTitle("link");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ServerConnectionsView.fxml"));
            AnchorPane borderPane = fxmlLoader.load();
            ServerConnectionsController controller = fxmlLoader.getController();
//            controller.setMain(this);
            Scene scene = new Scene(borderPane);
//            scene.getStylesheets().add(getClass().getResource("/css/MainSearch.css").toExternalForm());

//            scene.setFill(null);
            this.serverLinkWindowStage.initOwner(root.getScene().getWindow());

            this.serverLinkWindowStage.setScene(scene);
//            this.searchStage.getIcons().add(new Image("/icon/doc.png"));
//            this.searchStage.initStyle(StageStyle.TRANSPARENT);
            this.serverLinkWindowStage.show();


        }
    }
}
