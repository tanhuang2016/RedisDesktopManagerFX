package xyz.hashdog.rdm.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import xyz.hashdog.rdm.redis.Message;
import xyz.hashdog.rdm.redis.RedisConfig;
import xyz.hashdog.rdm.redis.RedisContext;
import xyz.hashdog.rdm.ui.common.RedisFactorySingleton;
import xyz.hashdog.rdm.ui.util.GuiUtil;

/**
 * @Author th
 * @Date 2023/7/19 21:45
 */
public class NewConnectionController {
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
    public void testConnect(ActionEvent actionEvent) {
        String hostStr = host.getText();
        String portStr = port.getText();
        String authStr = auth.getText();
        RedisConfig redisConfig = new RedisConfig();
        redisConfig.setHost(hostStr);
        redisConfig.setPort(Integer.parseInt(portStr));
        redisConfig.setAuth(authStr);
        RedisContext redisContext = RedisFactorySingleton.getInstance().createRedisContext(redisConfig);
        Message message = redisContext.testConnect();
        if(message.isSuccess()){
            GuiUtil.alert(Alert.AlertType.INFORMATION,"连接成功");
        }else {
            GuiUtil.alert(Alert.AlertType.WARNING,message.getMessage());
        }

    }
}
