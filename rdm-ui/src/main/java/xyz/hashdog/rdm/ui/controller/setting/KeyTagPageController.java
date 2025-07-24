package xyz.hashdog.rdm.ui.controller.setting;

import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import xyz.hashdog.rdm.ui.common.KeyTypeTagEnum;

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
       reset();
    }

    private void reset() {
        List<String> tags = KeyTypeTagEnum.tags();
        List<String> colors = KeyTypeTagEnum.colors();
        setTagLabels(tags,colors);
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

    private void initTagColor() {
    }
}
