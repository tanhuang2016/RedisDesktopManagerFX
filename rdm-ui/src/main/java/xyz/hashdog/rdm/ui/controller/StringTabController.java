package xyz.hashdog.rdm.ui.controller;

import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import xyz.hashdog.rdm.common.pool.ThreadPool;
import xyz.hashdog.rdm.redis.client.RedisClient;
import xyz.hashdog.rdm.ui.util.GuiUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class StringTabController extends BaseController<ServerTabController> implements Initializable {

    @FXML
    public TextField key;
    @FXML
    public TextField ttl;
    @FXML
    public TextArea value;
    /**
     * 根上有绑定userdata,绑定的是key
     */
    public AnchorPane root;
    /**
     * 父层传过来的当前key
     */
    private StringProperty currentKey;
    /**
     * 当前控制层操作的tab所用的redis客户端连接
     */
    private RedisClient redisClient;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initListener();
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        userDataPropertyListener();
        filterIntegerInputListener(this.ttl);
    }

    /**
     * 父层传送的数据监听
     * 监听到key的传递
     */
    private void userDataPropertyListener() {
        super.userDataProperty.addListener((observable, oldValue, newValue) -> {
            this.redisClient = (RedisClient) newValue;
            this.currentKey = (StringProperty) root.getUserData();
            initInfo();
        });
    }

    /**
     * 初始化数据展示
     */
    private void initInfo() {
        key.setText(this.currentKey.get());
        ThreadPool.getInstance().execute(() -> {
            long ttl = this.redisClient.ttl(this.currentKey.get());
            //todo 根据数据类型,可能是图片,然后是编码变化
//        this.redisClient.get(this.currentKey.getBytes())
            String value = this.redisClient.get(this.currentKey.get());
            Platform.runLater(() -> {
                this.ttl.setText(String.valueOf(ttl));
                this.value.setText(value);
            });
        });


    }

    /**
     * key重命名
     *
     * @param actionEvent
     */
    @FXML
    public void rename(ActionEvent actionEvent) {
        if (GuiUtil.requiredTextField(this.key)) {
            return;
        }
        asynexec(() -> {
            this.redisClient.rename(currentKey.get(), this.key.getText());
            this.currentKey.set(this.key.getText());
            Platform.runLater(() -> {
                GuiUtil.alert(Alert.AlertType.INFORMATION, "重命名成功");
            });
        });

    }


    /**
     * 设置有效期
     *
     * @param actionEvent
     */
    @FXML
    public void editTTL(ActionEvent actionEvent) {
        if (GuiUtil.requiredTextField(this.ttl)) {
            return;
        }
        int ttl = Integer.parseInt(this.ttl.getText());
        if (ttl <= -1) {
            if (GuiUtil.alert(Alert.AlertType.CONFIRMATION, "设为负数将永久保存?")) {
                asynexec(()->{
                    this.redisClient.persist(this.currentKey.get());
                    Platform.runLater(()->{
                        GuiUtil.alert(Alert.AlertType.INFORMATION,"设置成功");
                    });
                });
            }
            return;
        }

        asynexec(()->{
            this.redisClient.expire(this.currentKey.get(),ttl);
            Platform.runLater(()->{
                GuiUtil.alert(Alert.AlertType.INFORMATION,"设置成功");
            });
        });
    }

    /**
     * 删除键
     * 切需要关闭当前tab
     * @param actionEvent
     */
    @FXML
    public void delete(ActionEvent actionEvent) {
       boolean a= super.parentController.delKey(this.currentKey.get());
       if(a){
           closeTab();
       }
    }

    /**
     * 调用父类关闭当前tab
     */
    private void closeTab() {
        this.parentController.closeSelectedDbTab();
    }

    @FXML
    public void refresh(ActionEvent actionEvent) {
        initInfo();
    }

    @FXML
    public void copy(ActionEvent actionEvent) {
    }

    @FXML
    public void save(ActionEvent actionEvent) {
        asynexec(()->{
            redisClient.set(this.currentKey.get(), value.getText());
            Platform.runLater(()->{
                GuiUtil.alert(Alert.AlertType.INFORMATION,"保存成功");
            });
        });
    }


}
