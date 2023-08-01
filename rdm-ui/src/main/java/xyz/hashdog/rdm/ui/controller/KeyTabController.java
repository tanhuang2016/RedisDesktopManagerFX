package xyz.hashdog.rdm.ui.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import xyz.hashdog.rdm.common.pool.ThreadPool;
import xyz.hashdog.rdm.common.tuple.Tuple2;
import xyz.hashdog.rdm.ui.common.RedisDataTypeEnum;
import xyz.hashdog.rdm.ui.entity.PassParameter;
import xyz.hashdog.rdm.ui.util.GuiUtil;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class KeyTabController extends BaseKeyController<ServerTabController> implements Initializable {


    @FXML
    public TextField key;
    @FXML
    public TextField ttl;
    @FXML
    public Label keyType;
    @FXML
    public BorderPane borderPane;



    private long currentTtl;


    /**
     * 子类型控制层
     */
    private BaseKeyController subTypeController;


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
        this.currentTtl=ttl;
        Platform.runLater(() -> {
            this.key.setText(this.getParameter().getKey());
            this.ttl.setText(String.valueOf(currentTtl));
            this.keyType.setText(this.getParameter().getKeyType());
        });

    }

    /**
     * 初始化数据展示
     */
    private void initInfo()  {
        Future<Boolean> submit = ThreadPool.getInstance().submit(() -> {
            //加载通用数据
            loadData();
        }, true);


        try {
            if(submit.get()){
                RedisDataTypeEnum te = RedisDataTypeEnum.getByType(this.parameter.get().getKeyType());
                Tuple2<AnchorPane,BaseKeyController> tuple2 = loadFXML(te.fxml);
                AnchorPane anchorPane = tuple2.getT1();
                this.subTypeController  = tuple2.getT2();
                this.subTypeController.setParentController(this);
                PassParameter passParameter = new PassParameter(PassParameter.STRING);
                passParameter.setDb(this.currentDb);
                passParameter.setKey(this.parameter.get().getKey());
                passParameter.setKeyType(this.parameter.get().getKeyType());
                passParameter.setRedisClient(redisClient);
                passParameter.setRedisContext(redisContext);
                this.subTypeController.setParameter(passParameter);
                borderPane.setCenter(anchorPane);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }


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









}
