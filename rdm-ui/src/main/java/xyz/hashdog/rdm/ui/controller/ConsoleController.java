package xyz.hashdog.rdm.ui.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import xyz.hashdog.rdm.common.pool.ThreadPool;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static xyz.hashdog.rdm.ui.common.Constant.ALERT_MESSAGE_CONNECT_SUCCESS;
import static xyz.hashdog.rdm.ui.util.LanguageManager.language;

public class ConsoleController extends BaseKeyController<ServerTabController> implements Initializable {

    @FXML
    public TextArea textArea;
    @FXML
    public TextField textField;
    @FXML
    public Label label;


    @FXML
    public void addToTextAreaAction(ActionEvent actionEvent) {
        String inputText = textField.getText();
        if("clear".equalsIgnoreCase(inputText)){
            textArea.clear();
            textField.clear();
            return;
        }
        if (!inputText.isEmpty()) {
            textArea.appendText( "\n"+"> "+inputText );
            textField.clear();
            ThreadPool.getInstance().execute(()->{
                List<String> strings = redisClient.getRedisConsole().sendCommand(inputText);
                Platform.runLater(()->{
                    if(inputText.trim().startsWith("select")&&!strings.isEmpty()&& "ok".equalsIgnoreCase(strings.get(0))){
                        this.currentDb=Integer.parseInt(inputText.replace("select","").trim());
                        label.setText(redisContext.getRedisConfig().getName()+":"+this.currentDb+">");
                    }
                    for (String string : strings) {
                        textArea.appendText( "\n"+""+string );
                    }
                });
            });
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textArea.setPrefRowCount(10);

        // 监听TextArea的textProperty，每当有新内容添加时滚动到底部
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            // 将光标定位到文本末尾，这会自动将滚动条滚动到最下面
//            textArea.positionCaret(textArea.getText().length());
//            textArea.setScrollTop(-1d);
        });
        super.parameter.addListener((observable, oldValue, newValue) -> {
            label.setText(redisContext.getRedisConfig().getName()+":"+this.currentDb+">");
            textArea.appendText( "\n"+redisContext.getRedisConfig().getName()+" "+language(ALERT_MESSAGE_CONNECT_SUCCESS) );
            if(currentDb!=0){
                ThreadPool.getInstance().execute(()->this.redisClient.select(currentDb));
            }
        });
    }
}























