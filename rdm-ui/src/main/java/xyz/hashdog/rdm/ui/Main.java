package xyz.hashdog.rdm.ui;

import atlantafx.base.theme.Dracula;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.hashdog.rdm.redis.exceptions.RedisException;
import xyz.hashdog.rdm.ui.common.Applications;
import xyz.hashdog.rdm.ui.common.ConfigSettingsEnum;
import xyz.hashdog.rdm.ui.controller.MainController;
import xyz.hashdog.rdm.ui.entity.config.ConfigSettings;
import xyz.hashdog.rdm.ui.entity.config.ThemeSetting;
import xyz.hashdog.rdm.ui.exceptions.GeneralException;
import xyz.hashdog.rdm.ui.sampler.event.Save;
import xyz.hashdog.rdm.ui.sampler.theme.SamplerTheme;
import xyz.hashdog.rdm.ui.sampler.theme.ThemeManager;
import xyz.hashdog.rdm.ui.util.GuiUtil;

import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {
    protected static Logger log = LoggerFactory.getLogger(Main.class);

    public static final String BASE_NAME = "i18n.messages";

    public static Locale DEFAULT_LOCALE = Locale.getDefault();
    public static ResourceBundle RESOURCE_BUNDLE=ResourceBundle.getBundle(BASE_NAME, DEFAULT_LOCALE);



    public static void main(String[] args) {
        Application.launch(args);
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
            stage.setTitle(Applications.NODE_APP_NAME);
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
//        DEFAULT_LOCALE= new Locale("en", "US");
//        DEFAULT_LOCALE=Locale.JAPAN;
//        DEFAULT_LOCALE=Locale.US;
        RESOURCE_BUNDLE=ResourceBundle.getBundle(BASE_NAME, DEFAULT_LOCALE);
    }


}