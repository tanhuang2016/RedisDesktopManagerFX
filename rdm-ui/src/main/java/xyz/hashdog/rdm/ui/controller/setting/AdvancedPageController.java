package xyz.hashdog.rdm.ui.controller.setting;

import javafx.fxml.FXML;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import xyz.hashdog.rdm.ui.controller.BaseWindowController;
import xyz.hashdog.rdm.ui.controller.MainController;
import xyz.hashdog.rdm.ui.sampler.layout.MainModel;
import xyz.hashdog.rdm.ui.sampler.layout.Sidebar;

public class AdvancedPageController  extends BaseWindowController<MainController> {
    @FXML
    public AnchorPane root;
    public TreeView treeView;
    public AnchorPane sidebarParent;

    @FXML
    public void initialize() {
        Sidebar sidebar = new Sidebar(new MainModel());
        sidebarParent.getChildren().add(sidebar);

    }
}
