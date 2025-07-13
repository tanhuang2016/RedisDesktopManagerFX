package xyz.hashdog.rdm.ui.controller;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.controls.Popover;
import atlantafx.base.theme.Styles;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2MZ;
import xyz.hashdog.rdm.common.pool.ThreadPool;
import xyz.hashdog.rdm.common.tuple.Tuple2;
import xyz.hashdog.rdm.ui.Main;
import xyz.hashdog.rdm.ui.common.Constant;
import xyz.hashdog.rdm.ui.common.RedisDataTypeEnum;
import xyz.hashdog.rdm.ui.entity.PassParameter;
import xyz.hashdog.rdm.ui.util.GuiUtil;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class KeyTabController extends BaseKeyController<ServerTabController> implements Initializable {


    @FXML
    public CustomTextField key;
    @FXML
    public CustomTextField ttl;
    @FXML
    public Label keyType;
    @FXML
    public BorderPane borderPane;
    public Button keyRefresh;
    public Button keyDelete;
    public Label keyRename;
    public Label keyEditTTL;


    private long currentTtl;


    /**
     * 刷新弹框的延迟显示
     */
    private PauseTransition showRefreshPopoverDelay;
    private Popover refreshPopover;


    /**
     * 子类型控制层
     */
    private BaseKeyController subTypeController;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initListener();
        initButton();
        initTextField();
        initLabel();

    }

    private void initLabel() {
        keyRename.getStyleClass().addAll( Styles.BUTTON_ICON,Styles.SUCCESS,Styles.FLAT);
        keyRename.setGraphic(new FontIcon(Feather.CHECK));
        keyRename.setCursor(Cursor.HAND);
        keyEditTTL.getStyleClass().addAll( Styles.BUTTON_ICON,Styles.SUCCESS,Styles.FLAT);
        keyEditTTL.setGraphic(new FontIcon(Feather.CHECK));
        keyEditTTL.setCursor(Cursor.HAND);

    }

    private void initTextField() {
        key.setRight(keyRename);
        ttl.setRight(keyEditTTL);
    }


    private void initButton() {
        initButtonIcon();
        initButtonStyles();
    }

    private void initButtonStyles() {
        keyRefresh.getStyleClass().addAll( Styles.BUTTON_ICON,Styles.SUCCESS);
        keyDelete.getStyleClass().addAll( Styles.BUTTON_ICON,Styles.DANGER);
    }

    private void initButtonIcon() {
        FontIcon fontIcon = new FontIcon(Feather.REFRESH_CW);
        GuiUtil.setIcon(keyRefresh,fontIcon);
        GuiUtil.setIcon(keyDelete,new FontIcon(Feather.TRASH_2));


        //todo 刷新开启自动旋转，这里需要替换为png，否则会有抖动感觉
//        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(5), fontIcon);
//        rotateTransition.setByAngle(360); // 一圈
//        rotateTransition.setCycleCount(Animation.INDEFINITE);
//        rotateTransition.setAutoReverse(false);
//        rotateTransition.setInterpolator(Interpolator.LINEAR);
//        rotateTransition.play();
    }
    //停止旋转
//    public void stopRotation() {
//        if (rotateTransition != null) {
//            rotateTransition.stop();
//            rotateTransition.getNode().setRotate(0); // 可选：重置旋转角度
//        }
//    }

    /**
     * 初始化监听
     */
    private void initListener() {
        userDataPropertyListener();
        filterIntegerInputListener(true,this.ttl);
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
            // todo 现在只刷新了基本信息,具体类型的信息刷新还没做,需要调用具体类型的子控制层
            //subTypeController.刷新

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
                PassParameter passParameter = new PassParameter(te.tabType);
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

//    /**
//     * key重命名
//     *
//     * @param actionEvent
//     */
//    @FXML
//    public void rename(ActionEvent actionEvent) {
//        if (GuiUtil.requiredTextField(this.key)) {
//            return;
//        }
//        asynexec(() -> {
//            this.exeRedis(j -> j.rename(this.getParameter().getKey(), this.key.getText()));
//            this.getParameter().setKey(this.key.getText());
//            Platform.runLater(() -> {
//                GuiUtil.alert(Alert.AlertType.INFORMATION, "重命名成功");
//            });
//        });
//
//    }
    public void rename(MouseEvent mouseEvent) {
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
     * @param mouseEvent
     */
    @FXML
    public void editTTL(MouseEvent mouseEvent) {
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
        if (GuiUtil.alert(Alert.AlertType.CONFIRMATION, Main.RESOURCE_BUNDLE.getString(Constant.ALERT_MESSAGE_DEL))) {
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


    public void openRefreshPopover(MouseEvent mouseEvent) {
        showRefreshPopoverDelay = new PauseTransition(Duration.seconds(1.5));
        showRefreshPopoverDelay.setOnFinished(event -> {
            if(refreshPopover!=null){
                refreshPopover.show(keyRefresh);
            }else {
                FXMLLoader fxmlLoader = GuiUtil.loadFXML("/fxml/RefreshPopover.fxml");
                try {
                    AnchorPane root = fxmlLoader.load();
                    var pop = new Popover(root);
                    pop.setHeaderAlwaysVisible(false);
                    pop.setDetachable(false);
                    pop.setArrowLocation(Popover.ArrowLocation.TOP_CENTER);
                    pop.show(keyRefresh);
                    refreshPopover= pop;
                    // 设置：当鼠标移出 Popover 时自动关闭，这里延迟隐藏没什么意义了，所以设置1秒，因为延迟隐藏，需要考虑鼠标移出又立马移进的问题
                    //todo 后续可以优化一下，在鼠标移除又马上移进的时候，又不隐藏
                    refreshPopover.getScene().addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
                        PauseTransition delayHide = new PauseTransition(Duration.seconds(0.1));
                        delayHide.setOnFinished(ev -> {
                            if (refreshPopover.isShowing()) {
                                refreshPopover.hide();
                            }
                        });
                        delayHide.play();
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        });
        showRefreshPopoverDelay.play();
    }

    @Deprecated
    public void closeRefreshPopover(MouseEvent mouseEvent) {
//        if(refreshPopover!=null && refreshPopover.isShowing()){
//            refreshPopover.hide();
//        }
    }
}
