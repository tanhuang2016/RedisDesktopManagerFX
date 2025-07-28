package xyz.hashdog.rdm.ui.controller.setting;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import xyz.hashdog.rdm.common.util.DataUtil;
import xyz.hashdog.rdm.ui.util.LanguageManager;

import java.util.List;
import java.util.Locale;

public class LanguagePageController {
    public Button ok;
    public Button system;
    public AnchorPane root;
    public ComboBox<Locale> langComboBox;

    @FXML
    public void initialize() {
        initLangComboBox();

    }

    private void initLangComboBox() {
        // 获取支持的语言列表
        List<Locale> supportedLocales = LanguageManager.getSupportedLocales();

        // 设置ComboBox显示
        langComboBox.setItems(FXCollections.observableArrayList(supportedLocales));
        langComboBox.setConverter(new StringConverter<Locale>() {
            @Override
            public String toString(Locale locale) {
                if (locale != null) {
//                    if (DataUtil.isBlank(locale.toString())) {
//                        return String.format("%s", "English");
//                    }
                    return String.format("%s", locale.getDisplayName(locale));
                }
                return "";
            }

            @Override
            public Locale fromString(String string) {
                return null;
            }
        });


        // 设置当前语言
        langComboBox.setValue(Locale.getDefault());

    }

    public void ok(ActionEvent actionEvent) {
    }

    public void system(ActionEvent actionEvent) {
    }
}
