package xyz.hashdog.rdm.ui.controller;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import xyz.hashdog.rdm.common.pool.ThreadPool;
import xyz.hashdog.rdm.common.service.ValueTypeEnum;
import xyz.hashdog.rdm.common.util.EncodeUtil;
import xyz.hashdog.rdm.common.util.FileUtil;
import xyz.hashdog.rdm.ui.util.GuiUtil;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.ResourceBundle;

public class StringTabController extends BaseKeyController<ServerTabController> implements Initializable {

    protected static final String SIZE="size:%dB";

    @FXML
    public TextField key;
    @FXML
    public TextField ttl;
    @FXML
    public TextArea value;
    @FXML
    public Label size;
    @FXML
    public ChoiceBox typeChoiceBox;
    /**
     * 当前value的二进制
     */
    private byte[] currentValue;

    private long currentTtl;

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
     * 初始化监听
     */
    private void initListener() {
        userDataPropertyListener();
        filterIntegerInputListener(this.ttl);
        typeChoiceBoxListener();
    }

    /**
     * 类型选择触发事件
     */
    private void typeChoiceBoxListener() {
        typeChoiceBox.selectionModelProperty().addListener((observable, oldValue, newValue) -> {

        });
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
     * 重新加载
     * todo 需要 先把其他key类型的命令写完再做
     */
    private void reloadInfo() {
        ThreadPool.getInstance().execute(() -> {
            String text=null;
            loadData();



//            String fileTypeByStream = FileUtil.getFileTypeByStream(currentValue);
//            //不是可识别的文件类型,都默认采用16进制展示
//            if(fileTypeByStream==null){
//                boolean isUtf8 = EncodeUtil.isUTF8(bytes);
//                //是utf8编码或则非特殊字符,直接转utf8字符串
//                if(isUtf8||!EncodeUtil.containsSpecialCharacters(bytes)){
//                    text= new String(bytes);
//                }
//            }
//            if(text==null){
//                text = FileUtil.byte2HexString(bytes);
//                type=ValueTypeEnum.HEX;
//            } else {
//                type = ValueTypeEnum.TEXT;
//            }
//            String finalText = text;
//            Platform.runLater(() -> {
//                this.ttl.setText(String.valueOf(ttl));
//                this.value.setText(finalText);
//                this.size.setText(String.format(SIZE, bytes.length));
//                this.typeChoiceBox.setValue(type.name);
//            });
        });

    }

    /**
     * 加载数据
     */
    private void loadData() {
        long ttl = this.exeRedis(j -> j.ttl(this.getParameter().getKey()));
        byte[] bytes = this.exeRedis(j -> j.get(this.getParameter().getKey().getBytes(StandardCharsets.UTF_8)));
        this.currentValue=bytes;
        this.currentTtl=ttl;
        this.currentSize=bytes.length;
    }

    /**
     * 初始化数据展示
     */
    private void initInfo() {
        key.setText(this.getParameter().getKey());
        ThreadPool.getInstance().execute(() -> {
            ValueTypeEnum type;
            String text=null;
            loadData();
            String fileTypeByStream = FileUtil.getFileTypeByStream(currentValue);
            //不是可识别的文件类型,都默认采用16进制展示
            if(fileTypeByStream==null){
                boolean isUtf8 = EncodeUtil.isUTF8(currentValue);
                //是utf8编码或则非特殊字符,直接转utf8字符串
                if(isUtf8||!EncodeUtil.containsSpecialCharacters(currentValue)){
                    text= new String(currentValue);
                }
            }
            if(text==null){
                text = FileUtil.byte2HexString(currentValue);
                type=ValueTypeEnum.HEX;
            } else {
                type = ValueTypeEnum.TEXT;
            }
            String finalText = text;
            Platform.runLater(() -> {
                this.ttl.setText(String.valueOf(currentTtl));
                this.value.setText(finalText);
                this.size.setText(String.format(SIZE, currentSize));
                this.typeChoiceBox.setValue(type.name);
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
        reloadInfo();
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
