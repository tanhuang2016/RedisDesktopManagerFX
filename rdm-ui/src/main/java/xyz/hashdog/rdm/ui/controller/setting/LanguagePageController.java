package xyz.hashdog.rdm.ui.controller.setting;

import atlantafx.base.theme.Styles;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import xyz.hashdog.rdm.common.util.DataUtil;
import xyz.hashdog.rdm.ui.Main;
import xyz.hashdog.rdm.ui.common.Applications;
import xyz.hashdog.rdm.ui.common.ConfigSettingsEnum;
import xyz.hashdog.rdm.ui.entity.config.ConfigSettings;
import xyz.hashdog.rdm.ui.entity.config.LanguageSetting;
import xyz.hashdog.rdm.ui.util.GuiUtil;
import xyz.hashdog.rdm.ui.util.LanguageManager;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class LanguagePageController {
    public Button ok;
    public Button system;
    public AnchorPane root;
    public ComboBox<Locale> langComboBox;

    @FXML
    public void initialize() {
        initLangComboBox();
        initListener();
        initButton();

    }
    private void initButton() {
        initButtonStyles();
    }
    private void initButtonStyles() {
        ok.getStyleClass().add(Styles.ACCENT);
    }

    private void initListener() {
        langComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue != oldValue) {
                this.ok.setDisable(false);
            }
        });
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

        LanguageSetting configSettings = Applications.getConfigSettings(ConfigSettingsEnum.LANGUAGE.name);
        // 设置当前语言
        langComboBox.setValue(Locale.of(configSettings.getLocalLanguage(), configSettings.getLocalCountry()));

    }

    public void ok(ActionEvent actionEvent) {
        this.ok.setDisable(true);
        LanguageSetting configSettings = new LanguageSetting();
        configSettings.setLocalCountry(langComboBox.getValue().getCountry());
        configSettings.setLocalLanguage(langComboBox.getValue().getLanguage());
        Applications.putConfigSettings(configSettings.getName(), configSettings);
        if (GuiUtil.alert(Alert.AlertType.CONFIRMATION, "重启界面以使更改生效？")) {
            Main.RESOURCE_BUNDLE= ResourceBundle.getBundle(LanguageManager.BASE_NAME,Locale.of(configSettings.getLocalLanguage(),configSettings.getLocalCountry()));
            Main.restart();
        }


    }

    public void system(ActionEvent actionEvent) {
        langComboBox.setValue(Locale.getDefault());
    }
}
