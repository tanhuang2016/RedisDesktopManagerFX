package xyz.hashdog.rdm.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * @Author th
 * @Date 2023/7/20 23:36
 */
public class NewGroupController extends BaseController<ServerConnectionsController>{
    public AnchorPane root;
    public TextField connectionName;
    public TextField dataId;
    public TextField parentDataId;

    public void ok(ActionEvent actionEvent) {
    }


}
