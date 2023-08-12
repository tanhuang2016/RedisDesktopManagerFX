package xyz.hashdog.rdm.ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @Author th
 * @Date 2023/8/12 22:10
 */
public class AppendController extends BaseWindowController<BaseKeyController> implements Initializable {
    @FXML
    public BorderPane borderPane;
    @FXML
    public Button ok;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }




    /**
     * 设置内容
     * @param t1
     */
    public void setSubContent(AnchorPane t1) {
        borderPane.setCenter(t1);
    }
}
