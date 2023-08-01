package xyz.hashdog.rdm.ui.controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import xyz.hashdog.rdm.common.pool.ThreadPool;
import xyz.hashdog.rdm.common.service.ValueTypeEnum;
import xyz.hashdog.rdm.common.util.EncodeUtil;
import xyz.hashdog.rdm.common.util.FileUtil;
import xyz.hashdog.rdm.ui.util.GuiUtil;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

/**
 * @Author th
 * @Date 2023/7/31 12:08
 */
public class StringTypeController extends BaseKeyController<ServerTabController> implements Initializable {
    protected static final String SIZE = "size:%dB";

    @FXML
    public ChoiceBox typeChoiceBox;
    @FXML
    public TextArea value;
    @FXML
    public Label size;
    /**
     * 当前value的二进制
     */
    private byte[] currentValue;

    private long currentSize;
    /**
     * 当前type
     */
    private ValueTypeEnum type;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTypeChoiceBox();
        initListener();
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        userDataPropertyListener();
        typeChoiceBoxListener();
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
        ThreadPool.getInstance().execute(() -> {
            byte[] bytes = this.exeRedis(j -> j.get(this.getParameter().getKey().getBytes(StandardCharsets.UTF_8)));
            this.currentValue = bytes;
            this.currentSize = bytes.length;
            //根据key的类型切换对应视图
            ValueTypeEnum type;
            String text = null;
            String fileTypeByStream = FileUtil.getFileTypeByStream(currentValue);
            //不是可识别的文件类型,都默认采用16进制展示
            if (fileTypeByStream == null) {
                boolean isUtf8 = EncodeUtil.isUTF8(currentValue);
                //是utf8编码或则非特殊字符,直接转utf8字符串
                if (isUtf8 || !EncodeUtil.containsSpecialCharacters(currentValue)) {
                    text = new String(currentValue);
                }
            }
            if (text == null) {
                text = FileUtil.byte2HexString(currentValue);
                type = ValueTypeEnum.HEX;
            } else {
                type = ValueTypeEnum.TEXT;
            }
            String finalText = text;
            Platform.runLater(() -> {
                this.size.setText(String.format(SIZE, currentSize));
                this.value.setText(finalText);
                this.typeChoiceBox.setValue(type.name);
            });

        });


    }

    /**
     * 初始化单选框
     */
    private void initTypeChoiceBox() {
        ObservableList items = typeChoiceBox.getItems();
        items.clear();
        for (ValueTypeEnum valueTypeEnum : ValueTypeEnum.values()) {
            items.add(valueTypeEnum.name);
        }
    }

    /**
     * 类型选择触发事件
     */
    private void typeChoiceBoxListener() {
        typeChoiceBox.selectionModelProperty().addListener((observable, oldValue, newValue) -> {

        });
    }

    /**
     * 复制值
     *
     * @param actionEvent
     */
    @FXML
    public void copy(ActionEvent actionEvent) {
        GuiUtil.copyString(value.getText());
    }

    @Override
    protected void save() {
        asynexec(() -> {
            exeRedis(j -> j.set(this.getParameter().getKey(), value.getText()));
            Platform.runLater(() -> {
                GuiUtil.alert(Alert.AlertType.INFORMATION, "保存成功");
            });
        });
    }
}
