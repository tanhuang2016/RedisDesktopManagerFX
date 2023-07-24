package xyz.hashdog.rdm.ui.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import xyz.hashdog.rdm.common.pool.ThreadPool;
import xyz.hashdog.rdm.ui.util.GuiUtil;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class StringTabController extends BaseKeyController<ServerTabController> implements Initializable {

    @FXML
    public TextField key;
    @FXML
    public TextField ttl;
    @FXML
    public TextArea value;




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
        super.parameter.addListener((observable, oldValue, newValue) -> {
            initInfo();
        });
    }

    /**
     * 初始化数据展示
     */
    private void initInfo() {
        key.setText(this.getParameter().getKey());
        ThreadPool.getInstance().execute(() -> {
            long ttl = this.exeRedis(j -> j.ttl(this.getParameter().getKey()));
            //todo 根据数据类型,可能是图片,然后是编码变化
//        this.exeRedis(j -> j.get(this.currentKey.getBytes())
            String value = this.exeRedis(j -> j.get(this.getParameter().getKey()));
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
            this.exeRedis(j -> j.rename(this.getParameter().getKey(), this.key.getText()));
            this.getParameter().setKey(this.key.getText());
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
                    this.exeRedis(j -> j.persist(this.getParameter().getKey()));
                    Platform.runLater(()->{
                        GuiUtil.alert(Alert.AlertType.INFORMATION,"设置成功");
                    });
                });
            }
            return;
        }

        asynexec(()->{
            this.exeRedis(j -> j.expire(this.getParameter().getKey(),ttl));
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
        if (GuiUtil.alert(Alert.AlertType.CONFIRMATION, "确认删除?")) {
            exeRedis(j -> j.del(parameter.get().getKey()));
            if(super.parentController.delKey(parameter)){
                super.parentController.removeTabByKeys(Arrays.asList(parameter.get().getKey()));
            }
        }
    }



    /**
     * 刷新数据
     * @param actionEvent
     */
    @FXML
    public void refresh(ActionEvent actionEvent) {
        initInfo();
    }

    /**
     * 复制值
     * @param actionEvent
     */
    @FXML
    public void copy(ActionEvent actionEvent) {
        GuiUtil.copyString(value.getText());
    }

    /**
     * 保存值
     * @param actionEvent
     */
    @FXML
    public void save(ActionEvent actionEvent) {
        asynexec(()->{
            exeRedis(j -> j.set(this.getParameter().getKey(), value.getText()));
            Platform.runLater(()->{
                GuiUtil.alert(Alert.AlertType.INFORMATION,"保存成功");
            });
        });
    }



}
