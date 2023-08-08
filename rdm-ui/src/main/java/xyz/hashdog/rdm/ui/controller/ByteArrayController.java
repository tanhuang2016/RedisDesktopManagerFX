package xyz.hashdog.rdm.ui.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import xyz.hashdog.rdm.common.Constant;
import xyz.hashdog.rdm.common.util.EncodeUtil;
import xyz.hashdog.rdm.common.util.FileUtil;
import xyz.hashdog.rdm.ui.common.ValueTypeEnum;
import xyz.hashdog.rdm.ui.util.GuiUtil;

import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

/**
 * @Author th
 * @Date 2023/8/1 14:46
 */
public class ByteArrayController extends BaseController<BaseController> implements Initializable {

    @FXML
    public ChoiceBox<String> typeChoiceBox;
    @FXML
    public TextArea value;
    protected static final String SIZE = "Size:%dB";

    @FXML
    public Label size;
    @FXML
    public Label name;
    @FXML
    public ChoiceBox<String> characterChoiceBox;
    @FXML
    public Button into;
    @FXML
    public Button export;
    @FXML
    public AnchorPane root;
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
        initCharacterChoiceBox();
        initTypeChoiceBox();
        initListener();
    }

    /**
     * 初始化字符集选项
     */
    private void initCharacterChoiceBox() {
        ObservableList items = characterChoiceBox.getItems();
        items.clear();
        items.addAll(Constant.CHARSETS);
        characterChoiceBox.setValue(StandardCharsets.UTF_8.displayName());
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
     * text系列才会显示编码字符集
     * 二进制数据,才会显示导入导出
     */
    private void typeChoiceBoxListener() {
        typeChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue==null){
                return;
            }
            boolean isText = newValue.startsWith(ValueTypeEnum.TEXT.name);
            characterChoiceBox.setVisible(isText);
            characterChoiceBox.setManaged(isText);
            boolean isBinary = newValue.equals(ValueTypeEnum.BINARY.name);
            into.setVisible(isBinary);
            into.setManaged(isBinary);
            export.setVisible(isBinary);
            //设置node不可见时不占用空间
            export.setManaged(isBinary);

            this.type=ValueTypeEnum.getByName(newValue);
            this.value.setText(type.handler.byte2Text(this.currentValue,Charset.forName(characterChoiceBox.getValue())));
            if(this.type.handler.isView()){
                Parent view = this.type.handler.view(this.currentValue, Charset.forName(characterChoiceBox.getValue()));
                Scene scene = new Scene(view);
                Stage stage=new Stage();
                stage.initOwner(root.getScene().getWindow());
                stage.setScene(scene);
                stage.show();
            }

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
     *
     * @return
     */
    public byte[] getByteArray() {
        //这里需要通过Text即时计算byte数组,根据类型进行转换为byte数组
//        return currentValue;
        return value.getText().getBytes();
    }


    /**
     * 设置数据
     *
     * @param currentValue
     */
    public void setByteArray(byte[] currentValue) {
        this.currentValue = currentValue;
        this.currentSize = currentValue.length;
        //根据key的类型切换对应视图
        ValueTypeEnum type = null;
        String text = null;
        String fileTypeByStream = FileUtil.getFileTypeByStream(currentValue);
        //不是可识别的文件类型,都默认采用16进制展示
        if (fileTypeByStream == null) {
            boolean isUtf8 = EncodeUtil.isUTF8(currentValue);
            //是utf8编码或则非特殊字符,直接转utf8字符串
            if (isUtf8 || !EncodeUtil.containsSpecialCharacters(currentValue)) {
//                text = new String(currentValue);
                type = ValueTypeEnum.TEXT;
            }
        }
        if (type == null) {
//            text = FileUtil.byte2HexString(currentValue);
            type = ValueTypeEnum.HEX;
        } else {
            type = ValueTypeEnum.TEXT;
        }

        String finalText = type.handler.byte2Text(currentValue, StandardCharsets.UTF_8);
        this.size.setText(String.format(SIZE, currentSize));
        this.value.setText(finalText);
        this.typeChoiceBox.setValue(type.name);
    }

    /**
     * 设置名称
     *
     * @param key
     */
    public void setName(String key) {
        this.name.setText(key);

    }
}
