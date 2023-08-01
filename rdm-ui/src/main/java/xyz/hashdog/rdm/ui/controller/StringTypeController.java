package xyz.hashdog.rdm.ui.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import xyz.hashdog.rdm.common.pool.ThreadPool;
import xyz.hashdog.rdm.common.tuple.Tuple2;
import xyz.hashdog.rdm.ui.util.GuiUtil;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

/**
 * @Author th
 * @Date 2023/7/31 12:08
 */
public class StringTypeController extends BaseKeyController<KeyTabController> implements Initializable {


    public BorderPane borderPane;
    private ByteArrayController byteArrayController;
    /**
     * 当前value的二进制
     */
    private byte[] currentValue;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initListener();
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        userDataPropertyListener();
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

    private void initInfo() {
        ThreadPool.getInstance().execute(() -> {
                    byte[] bytes = this.exeRedis(j -> j.get(this.getParameter().getKey().getBytes(StandardCharsets.UTF_8)));
                    this.currentValue = bytes;
                    Platform.runLater(()->{
                        Tuple2<AnchorPane,ByteArrayController> tuple2 = loadFXML("/fxml/ByteArrayView.fxml");
                        AnchorPane anchorPane = tuple2.getT1();
                        this.byteArrayController  = tuple2.getT2();
                        this.byteArrayController.setParentController(this);
                        this.byteArrayController.setByteArray(this.currentValue);
                        borderPane.setCenter(anchorPane);
                    });
        });


    }


    /**
     * 保存值
     * @param actionEvent
     */
    @FXML
    public void save(ActionEvent actionEvent) {
        byte[] byteArray = byteArrayController.getByteArray();
        asynexec(() -> {
            exeRedis(j -> j.set(this.getParameter().getKey().getBytes(), byteArray));
            Platform.runLater(() -> {
                GuiUtil.alert(Alert.AlertType.INFORMATION, "保存成功");
            });
        });
    }
}
