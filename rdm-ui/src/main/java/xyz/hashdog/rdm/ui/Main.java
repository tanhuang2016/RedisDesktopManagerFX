package xyz.hashdog.rdm.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {
    public static final String BASE_NAME = "i18n.messages";

    public static Locale DEFAULT_LOCALE = Locale.getDefault();
    public static ResourceBundle RESOURCE_BUNDLE=ResourceBundle.getBundle(BASE_NAME, DEFAULT_LOCALE);



    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("");
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainView.fxml"),RESOURCE_BUNDLE);
        stage.setTitle(RESOURCE_BUNDLE.getString("Title") );

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/global.css").toExternalForm());
//        Application.setUserAgentStylesheet();
        stage.setScene(scene);
//        stage.getIcons().add(new Image("/icon/CT.png"));
        stage.show();
    }

    @Override
    public void init() throws Exception {
        DEFAULT_LOCALE= new Locale("en", "US");
        RESOURCE_BUNDLE=ResourceBundle.getBundle(BASE_NAME, DEFAULT_LOCALE);
    }


}