package xyz.hashdog.rdm.ui.controller.setting;

import atlantafx.base.theme.Styles;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import xyz.hashdog.rdm.ui.controller.BaseWindowController;
import xyz.hashdog.rdm.ui.controller.MainController;
import xyz.hashdog.rdm.ui.sampler.layout.MainModel;
import xyz.hashdog.rdm.ui.sampler.layout.Sidebar;
import xyz.hashdog.rdm.ui.util.GuiUtil;

public class AdvancedPageController  {
    @FXML
    public AnchorPane root;
    public TextField connectionTimeout;
    public TextField soTimeout;
    public ToggleButton treeShow;
    public ToggleButton listShow;
    public TextField keySeparator;
    public Button ok;
    public Button reset;

    @FXML
    public void initialize() {
        initToggleButton();
    }

    private void initToggleButton() {
        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(treeShow,listShow);
        // 添加监听，确保至少一个按钮被选中
        toggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (toggleGroup.getSelectedToggle() == null) {
                // 如果没有选中任何按钮，恢复上一个或默认选中
                if (oldToggle != null) {
                    oldToggle.setSelected(true);
                } else {
                    treeShow.setSelected(true);
                }
            }
        });
        GuiUtil.setIcon(treeShow,new FontIcon(Material2AL.ACCOUNT_TREE));
        GuiUtil.setIcon(listShow,new FontIcon(Material2MZ.VIEW_LIST));
        treeShow.getStyleClass().addAll(Styles.LEFT_PILL);
        listShow.getStyleClass().addAll(Styles.RIGHT_PILL);
    }

    public void ok(ActionEvent actionEvent) {
    }

    public void reset(ActionEvent actionEvent) {
    }
}
