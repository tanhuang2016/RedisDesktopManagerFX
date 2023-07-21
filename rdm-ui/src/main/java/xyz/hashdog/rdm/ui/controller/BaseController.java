package xyz.hashdog.rdm.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import xyz.hashdog.rdm.ui.common.Applications;

import java.io.IOException;

/**
 * @Author th
 * @Date 2023/7/20 17:35
 */
public abstract class BaseController<T>  {
    /**
     * 模式,默认是NONE
     */
    protected int model;
    protected static final int NONE = 1;
    protected static final int ADD = 2;
    protected static final int UPDATE = 3;
    protected static final int RENAME = 4;
    /**
     * 当前Stage
     */
    public Stage currentStage;
    /**
     * 父控制器
     */
    public T parentController;

    @FXML
    public void cancel(ActionEvent actionEvent) {
        currentStage.close();
    }

    /**
     * 子窗口模态框
     * 每次都是打开最新的
     *
     * @param title  窗口标题
     * @param fxml   fxml路径
     * @param parent 父窗口
     * @param model 模式
     * @throws IOException
     */
    protected <T extends BaseController>T loadSubWindow(String title, String fxml, Window parent, int model) throws IOException {
        Stage newConnctionWindowStage = new Stage();
        newConnctionWindowStage.initModality(Modality.WINDOW_MODAL);
        //去掉最小化和最大化
        newConnctionWindowStage.initStyle(StageStyle.DECORATED);
        //禁用掉最大最小化
        newConnctionWindowStage.setMaximized(false);
        newConnctionWindowStage.setTitle(title);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
        AnchorPane borderPane = fxmlLoader.load();
        T controller = fxmlLoader.getController();
        controller.setParentController(this);
        controller.setCurrentStage(newConnctionWindowStage);
        Scene scene = new Scene(borderPane);
        newConnctionWindowStage.initOwner(parent);
        newConnctionWindowStage.setScene(scene);
        newConnctionWindowStage.show();
        controller.setModel(model);
        return controller;
    }
    /**
     * 子窗口模态框
     * 每次都是打开最新的
     *
     * @param title  窗口标题
     * @param fxml   fxml路径
     * @param parent 父窗口
     * @throws IOException
     */
    public <T extends BaseController>T loadSubWindow(String title, String fxml, Window parent) throws IOException {
        return loadSubWindow(title, fxml, parent,NONE);
    }




    public void setModel(int model) {
        this.model = model;
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
        this.currentStage.getIcons().add(Applications.ICON_REDIS);
    }

    public void setParentController(T parentController) {
        this.parentController = parentController;
    }


}
