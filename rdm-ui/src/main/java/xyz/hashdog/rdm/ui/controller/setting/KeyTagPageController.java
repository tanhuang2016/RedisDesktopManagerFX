package xyz.hashdog.rdm.ui.controller.setting;

import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import xyz.hashdog.rdm.common.util.DataUtil;
import xyz.hashdog.rdm.common.util.TUtil;
import xyz.hashdog.rdm.ui.common.Applications;
import xyz.hashdog.rdm.ui.common.ConfigSettingsEnum;
import xyz.hashdog.rdm.ui.common.KeyTypeTagEnum;
import xyz.hashdog.rdm.ui.entity.config.KeyTagSetting;
import xyz.hashdog.rdm.ui.entity.config.ThemeSetting;
import xyz.hashdog.rdm.ui.util.GuiUtil;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public  class KeyTagPageController implements Initializable {
    public HBox tagLabels;
    public HBox tagTexts;
    public HBox tagColors;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//       reset();
        initData();
        addListener();

    }

    private void initData() {
        KeyTagSetting setting = Applications.getConfigSettings(ConfigSettingsEnum.KEY_TAG.name);
        List<String> tags =setting.getTags();
        List<String> colors = setting.getColors();
        setTagLabels(tags,colors);
        setTagTexts(tags);
        setTagColors(colors);
    }

    private void addListener() {
        addTagTextsListener();
        addTagColorsListener();
    }

    private void addTagColorsListener() {
        for (int i = 0; i < tagColors.getChildren().size(); i++) {
            ColorPicker colorPicker= (ColorPicker) tagColors.getChildren().get(i);
            int finalI = i;
            colorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
                Label tag = getTagLabel(finalI);
                tag.setStyle("-fx-background-color:"+GuiUtil.color2hex(newValue));
            });
        }
    }

    private void addTagTextsListener() {
        for (int i = 0; i < tagTexts.getChildren().size(); i++) {
            TextField node = (TextField) tagTexts.getChildren().get(i);
            int finalI = i;
            node.textProperty().addListener((observable, oldValue, newValue) -> {
                Label tag = getTagLabel(finalI);
                if(DataUtil.isBlank(newValue)){
                    node.setText(oldValue);
                }else {
                    tag.setText(newValue);
                }
            });
        }
    }

    private Label getTagLabel(int i) {
        VBox vBox = (VBox) tagLabels.getChildren().get(i);
        return (Label) vBox.getChildren().get(1);
    }

    private void reset() {
        List<String> tags = KeyTypeTagEnum.tags();
        List<String> colors = KeyTypeTagEnum.colors();
        setTagLabels(tags,colors);
        setTagTexts(tags);
        setTagColors(colors);
    }

    private void setTagColors(List<String> colors) {
        for (int i = 0; i < tagColors.getChildren().size(); i++) {
            ColorPicker c= (ColorPicker) tagColors.getChildren().get(i);
            c.setValue(Color.web(colors.get(i)));
        }
    }

    private void setTagTexts(List<String> tags) {
        for (int i = 0; i < tagTexts.getChildren().size(); i++) {
            TextField node = (TextField) tagTexts.getChildren().get(i);
            node.setText(tags.get(i));
        }
    }

    private void setTagLabels(List<String> tags,List<String> colors) {
        ObservableList<Node> children = tagLabels.getChildren();
        for (int i = 0; i < children.size(); i++) {
            if(children.get(i) instanceof VBox v){
                ObservableList<Node> vc = v.getChildren();
                Label node = (Label)vc.get(1);
                node.setText(tags.get(i));
                node.setStyle("-fx-background-color:"+colors.get(i));

            }
        }
    }


}
