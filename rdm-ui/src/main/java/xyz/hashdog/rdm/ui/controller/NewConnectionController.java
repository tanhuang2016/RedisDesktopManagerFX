package xyz.hashdog.rdm.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import xyz.hashdog.rdm.common.util.DataUtil;
import xyz.hashdog.rdm.redis.Message;
import xyz.hashdog.rdm.redis.RedisConfig;
import xyz.hashdog.rdm.redis.RedisContext;
import xyz.hashdog.rdm.redis.RedisFactorySingleton;
import xyz.hashdog.rdm.ui.common.Applications;
import xyz.hashdog.rdm.ui.entity.ConnectionServerNode;
import xyz.hashdog.rdm.ui.util.GuiUtil;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @Author th
 * @Date 2023/7/19 21:45
 */
public class NewConnectionController extends BaseController<ServerConnectionsController> implements Initializable {
    /**
     * 当前根节点
     */
    public AnchorPane root;

    /**
     * 测试连接按钮
     */
    @FXML
    public Button testConnectButton;
    /**
     * 连接名称
     */
    @FXML
    public TextField name;
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
    /**
     * 连接id,保存的时候会有
     */
    @FXML
    public TextField dataId;



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

    /**
     * 确定之后将新增的节点持久化
     * 再对父窗口视图进行更新
     * @param actionEvent
     */
    @FXML
    public void ok(ActionEvent actionEvent) {
        if(GuiUtil.requiredTextField(name,host, port)){
            return;
        }
        ConnectionServerNode connectionServerNode =new ConnectionServerNode(ConnectionServerNode.SERVER);
        connectionServerNode.setName(name.getText());
        connectionServerNode.setHost(host.getText());
        connectionServerNode.setPort(Integer.parseInt(port.getText()));
        connectionServerNode.setAuth(connectionServerNode.getAuth());
        connectionServerNode.setParentDataId(super.parentController.getSelectedDataId());
        Message message=Applications.addConnectionOrGroup(connectionServerNode);
        if(message.isSuccess()){
            currentStage.close();
        }
        //父窗口树节点新增,切选中新增节点
        parentController.AddConnectionOrGourpNodeAndSelect(connectionServerNode);
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
