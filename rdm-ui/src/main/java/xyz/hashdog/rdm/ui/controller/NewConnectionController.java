package xyz.hashdog.rdm.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
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
 * 新建连接的控制层
 * @author th
 * @version 1.0.0
 * @since 2023/7/19 21:45
 */
public class NewConnectionController extends BaseWindowController<ServerConnectionsController> implements Initializable {
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
        filterIntegerInputListener(false,this.port);
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
        Message message = redisContext.newRedisClient().testConnect();
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
        connectionServerNode.setAuth(auth.getText());
        Message message=null;
        switch (this.model){
            case ADD:
                //父窗口树节点新增,切选中新增节点
                connectionServerNode.setParentDataId(super.parentController.getSelectedDataId());
                //更新或修改保存
                message=Applications.addOrUpdateConnectionOrGroup(connectionServerNode);
                //父窗口树节点新增,切选中新增节点
                parentController.AddConnectionOrGourpNodeAndSelect(connectionServerNode);
                break;
            case UPDATE:
                connectionServerNode.setDataId(dataId.getText());
                //更新或修改保存
                message=Applications.addOrUpdateConnectionOrGroup(connectionServerNode);
                //更新
                parentController.updateNodeInfo(connectionServerNode);
                break;
            default:
                break;

        }
        if(message.isSuccess()){
            currentStage.close();
        }

    }




    /**
     * 填充编辑数据
     * @param selectedNode
     */
    public void editInfo(ConnectionServerNode selectedNode) {
        name.setText(selectedNode.getName());
        host.setText(selectedNode.getHost());
        port.setText(String.valueOf(selectedNode.getPort()));
        auth.setText(selectedNode.getAuth());
        dataId.setText(selectedNode.getDataId());
    }
}
