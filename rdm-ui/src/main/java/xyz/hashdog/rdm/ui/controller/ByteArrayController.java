package xyz.hashdog.rdm.ui.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import xyz.hashdog.rdm.common.service.ValueTypeEnum;
import xyz.hashdog.rdm.common.util.EncodeUtil;
import xyz.hashdog.rdm.common.util.FileUtil;
import xyz.hashdog.rdm.ui.util.GuiUtil;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @Author th
 * @Date 2023/8/1 14:46
 */
public class ByteArrayController  extends BaseController<BaseController> implements Initializable {

    @FXML
    public ChoiceBox typeChoiceBox;
    @FXML
    public TextArea value;
    protected static final String SIZE = "Size:%dB";

    @FXML
    public Label size;
    @FXML
    public Label name;
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
        typeChoiceBoxListener();
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


    /**
     * 返回最新的数据
     * @return
     */
    public byte[] getByteArray() {
        //这里需要通过Text即时计算byte数组,根据类型进行转换为byte数组
//        return currentValue;
        return value.getText().getBytes();
    }


    /**
     * 设置数据
     * @param currentValue
     */
    public void setByteArray(byte[] currentValue) {
        this.currentValue=currentValue;
        this.currentSize = currentValue.length;
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
        this.size.setText(String.format(SIZE, currentSize));
        this.value.setText(finalText);
        this.typeChoiceBox.setValue(type.name);
    }

    /**
     * 设置名称
     * @param key
     */
    public void setName(String key) {
        this.name.setText(key);

    }
}
