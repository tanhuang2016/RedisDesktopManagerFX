package xyz.hashdog.rdm.ui;

import atlantafx.base.theme.Dracula;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.hashdog.rdm.redis.exceptions.RedisException;
import xyz.hashdog.rdm.ui.common.Applications;
import xyz.hashdog.rdm.ui.common.ConfigSettingsEnum;
import xyz.hashdog.rdm.ui.controller.MainController;
import xyz.hashdog.rdm.ui.entity.config.ConfigSettings;
import xyz.hashdog.rdm.ui.entity.config.LanguageSetting;
import xyz.hashdog.rdm.ui.entity.config.ThemeSetting;
import xyz.hashdog.rdm.ui.exceptions.GeneralException;
import xyz.hashdog.rdm.ui.sampler.event.Save;
import xyz.hashdog.rdm.ui.sampler.theme.SamplerTheme;
import xyz.hashdog.rdm.ui.sampler.theme.ThemeManager;
import xyz.hashdog.rdm.ui.util.GuiUtil;
import xyz.hashdog.rdm.ui.util.LanguageManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;
import java.util.ResourceBundle;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Main extends Application {
    protected static Logger log = LoggerFactory.getLogger(Main.class);


    public static ResourceBundle RESOURCE_BUNDLE=ResourceBundle.getBundle(LanguageManager.BASE_NAME, LanguageManager.DEFAULT_LOCALE);



    public static void main(String[] args) {
        Application.launch(args);
    }

    public static void restart() {
        try {
            // 关闭所有窗口
            ObservableList<Window> windows = Window.getWindows();
            windows.getFirst().hide();
            // 重新启动主应用
            Stage primaryStage = new Stage();
            new Main().start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
            // 错误处理
            GuiUtil.alert(Alert.AlertType.ERROR,"Failed to restart application: " + e.getMessage());
        }
    }

    @Override
    public void start(Stage stage)  {
        try {
            Save.init();
            // 设置默认的未捕获异常处理器
            Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
                throwable.printStackTrace();
                log.error("",throwable);
                if(throwable instanceof RedisException||throwable instanceof GeneralException){
                    // 在此处您可以自定义处理异常的逻辑
                    GuiUtil.alert(Alert.AlertType.WARNING,throwable.getLocalizedMessage());
                    return;
                }
                Throwable cause = getRootCause(throwable);
                // 在此处您可以自定义处理异常的逻辑
                GuiUtil.alert(Alert.AlertType.ERROR,cause.getMessage());
            });
            stage.setTitle(Applications.TITLE);
//        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainView.fxml"),RESOURCE_BUNDLE);
//        stage.setTitle(RESOURCE_BUNDLE.getString(""Applications.NODE_APP_NAME"") );

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"),RESOURCE_BUNDLE);
            AnchorPane root = fxmlLoader.load();
            MainController controller = fxmlLoader.getController();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/global.css").toExternalForm());
//        Application.setUserAgentStylesheet();
            stage.setScene(scene);
            controller.setCurrentStage(stage);
            initTm(scene);

            stage.show();


            //先默认打开
            controller.openServerLinkWindo(null);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void initTm(Scene scene) {
        ThemeSetting themeSetting = Applications.getConfigSettings(ConfigSettingsEnum.THEME.name);
        ThemeManager TM = ThemeManager.getInstance();
        TM.setScene(scene);
        SamplerTheme theme = TM.getTheme(themeSetting.getColorTheme());
        TM.setTheme(theme,false);
        TM.setFontSize(themeSetting.getFontSize(),false);
        TM.setFontFamily(themeSetting.getFont(),false);

//
//            TM.setTheme(new SamplerTheme(new Dracula()));
    }

    public Throwable getRootCause(Throwable throwable) {
        Throwable cause = throwable.getCause();
        if (cause == null) {
            return throwable; // This is the root cause
        }
        return getRootCause(cause); // Continue searching deeper
    }
    @Override
    public void init() throws Exception {
        loadApplicationProperties();

//        DEFAULT_LOCALE= new Locale("en", "US");
//        DEFAULT_LOCALE=Locale.JAPAN;
//        DEFAULT_LOCALE=Locale.US;
//        RESOURCE_BUNDLE=ResourceBundle.getBundle(LanguageManager.BASE_NAME, LanguageManager.DEFAULT_LOCALE);
        LanguageSetting configSettings = Applications.getConfigSettings(ConfigSettingsEnum.LANGUAGE.name);
        Main.RESOURCE_BUNDLE= ResourceBundle.getBundle(LanguageManager.BASE_NAME,Locale.of(configSettings.getLocalLanguage(),configSettings.getLocalCountry()));
    }
    private void loadApplicationProperties() {
        Properties properties = new Properties();
        try (InputStreamReader in = new InputStreamReader(Objects.requireNonNull(Main.class.getResourceAsStream("/application.properties")),
                UTF_8)) {
            properties.load(in);
            properties.forEach((key, value) -> System.setProperty(
                    String.valueOf(key),
                    String.valueOf(value)
            ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}