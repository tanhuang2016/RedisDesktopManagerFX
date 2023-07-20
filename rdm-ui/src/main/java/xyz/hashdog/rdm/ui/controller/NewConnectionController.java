package xyz.hashdog.rdm.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import xyz.hashdog.rdm.redis.Message;
import xyz.hashdog.rdm.redis.RedisConfig;
import xyz.hashdog.rdm.redis.RedisContext;
import xyz.hashdog.rdm.redis.RedisFactorySingleton;
import xyz.hashdog.rdm.ui.util.GuiUtil;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @Author th
 * @Date 2023/7/19 21:45
 */
public class NewConnectionController implements Initializable {
    /**
     * 测试连接按钮
     */
    @FXML
    public Button testConnectButton;
    /**
     * 连接名称
     */
    @FXML
    public TextField connectionName;
    /**
     * 地址/域
     */
    @FXML
    public TextField host;
    /**
     * 端口
     */
    @FXML
    public TextField port;
    /**
     * 密码
     */
    @FXML
    public PasswordField auth;


    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initListener();

    }

    /**
     * 初始化监听
     */
    private void initListener() {
        filterIntegerInputListener();
    }

    /**
     * 只让输入整数
     */
    private void filterIntegerInputListener() {
        GuiUtil.filterIntegerInput(port);
    }

    @FXML
    public void testConnect(ActionEvent actionEvent) {
        if(GuiUtil.requiredTextField(host, port)){
            return;
        }
        String hostStr = host.getText();
        String portStr = port.getText();
        String authStr = auth.getText();
        RedisConfig redisConfig = new RedisConfig();
        redisConfig.setHost(hostStr);
        redisConfig.setPort(Integer.parseInt(portStr));
        redisConfig.setAuth(authStr);
        RedisContext redisContext = RedisFactorySingleton.getInstance().createRedisContext(redisConfig);
        Message message = redisContext.testConnect();
        if (message.isSuccess()) {
            GuiUtil.alert(Alert.AlertType.INFORMATION, "连接成功");
        } else {
            GuiUtil.alert(Alert.AlertType.WARNING, message.getMessage());
        }
    }
    @FXML
    public void ok(ActionEvent actionEvent) {
        if(GuiUtil.requiredTextField(connectionName,host, port)){
            return;
        }
    }

    /**
     * port只能为整数
     * @param keyEvent
     */
    @FXML
    public void filterIntegerInput(KeyEvent keyEvent) {
        // 获取用户输入的字符
        String inputChar = keyEvent.getCharacter();
        // 如果输入字符不是整数，则阻止其显示在TextField中
        if (!inputChar.matches("\\d")) {
            keyEvent.consume();
        }
    }



}
