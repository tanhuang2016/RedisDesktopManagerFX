package xyz.hashdog.rdm.ui.controller;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.controls.Popover;
import atlantafx.base.theme.Styles;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import xyz.hashdog.rdm.common.pool.ThreadPool;
import xyz.hashdog.rdm.common.tuple.Tuple2;
import xyz.hashdog.rdm.ui.Main;
import xyz.hashdog.rdm.ui.common.Constant;
import xyz.hashdog.rdm.ui.common.RedisDataTypeEnum;
import xyz.hashdog.rdm.ui.controller.popover.RefreshPopover;
import xyz.hashdog.rdm.ui.entity.PassParameter;
import xyz.hashdog.rdm.ui.util.GuiUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static xyz.hashdog.rdm.ui.common.Constant.*;
import static xyz.hashdog.rdm.ui.util.LanguageManager.language;

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
    public Label refreshText;


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


//        rotation();


    }

    private void rotation() {
        ImageView fontIcon =  GuiUtil.svgImageView("/svg/refresh.svg");
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(5), fontIcon);
        rotateTransition.setByAngle(360); // 一圈
        rotateTransition.setCycleCount(Animation.INDEFINITE);
        rotateTransition.setAutoReverse(false);
        rotateTransition.setInterpolator(Interpolator.LINEAR);
        rotateTransition.play();
        keyRefresh.setGraphic(fontIcon);
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
            refreshTextUpdate();
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
                GuiUtil.alert(Alert.AlertType.INFORMATION, language(ALERT_MESSAGE_RENAME_SUCCESS));
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
            if (GuiUtil.alert(Alert.AlertType.CONFIRMATION, language(ALERT_MESSAGE_SET_TTL))) {
                asynexec(()->{
                    this.exeRedis(j -> j.persist(this.getParameter().getKey()));
                    Platform.runLater(()->{
                        GuiUtil.alert(Alert.AlertType.INFORMATION,language(ALERT_MESSAGE_SET_SUCCESS));
                    });
                });
            }
            return;
        }

        asynexec(()->{
            this.exeRedis(j -> j.expire(this.getParameter().getKey(),ttl));
            Platform.runLater(()->{
                GuiUtil.alert(Alert.AlertType.INFORMATION,language(ALERT_MESSAGE_SET_SUCCESS));
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
        refreshTextUpdate();
    }

    private long refreshTime;
    private boolean refreshTextFlag=true;
    private Timeline refreshTextTimeline;
    private void refreshTextUpdate() {
        refreshTime=System.currentTimeMillis();
        updateRefreshText();
        if(refreshTextFlag){
            // 如果时间线已经存在，先停止它
            if (refreshTextTimeline != null) {
                refreshTextTimeline.stop();
            }

            // 创建新的时间线，每5秒更新一次
            refreshTextTimeline = new Timeline(
                    new KeyFrame(Duration.seconds(5), event -> updateRefreshText())
            );
            refreshTextTimeline.setCycleCount(Timeline.INDEFINITE);
            refreshTextTimeline.play();
        }

    }

    private void updateRefreshText() {
        if(!refreshTextFlag){
            return;
        }
        long currentTime = System.currentTimeMillis();
        long diffSeconds = (currentTime - refreshTime) / 1000;
        String text;
        if (diffSeconds < 5) {
            text = "now";
        } else if (diffSeconds < 30) {
            text = ">5s";
        } else if (diffSeconds < 60) {
            text = ">30s";
        } else if (diffSeconds < 120) {
            text = ">1min";
        } else if (diffSeconds < 180) {
            text = ">2min";
        } else if (diffSeconds < 240) {
            text = ">3min";
        } else if (diffSeconds < 300) {
            text = ">4min";
        } else if (diffSeconds < 600) {
            text = ">5min";
        } else if (diffSeconds < 900) {
            text = ">10min";
        } else if (diffSeconds < 1200) {
            text = ">15min";
        } else if (diffSeconds < 1500) {
            text = ">20min";
        } else if (diffSeconds < 1800) {
            text = ">25min";
        } else if (diffSeconds < 3600) {
            text = ">30min";
        } else {
            // 超过1小时，按小时计算
            long hours = diffSeconds / 3600;
            text = ">" + hours + "h";
        }
        refreshText.setText(text);
    }


    public void openRefreshPopover(MouseEvent mouseEvent) {
        if(refreshPopover!=null&&refreshPopover.isShowing()){
            return;
        }
        showRefreshPopoverDelay = new PauseTransition(Duration.seconds(1.5));
        showRefreshPopoverDelay.setOnFinished(event -> {
            if(refreshPopover!=null){
                refreshPopover.show(keyRefresh);
            }else {
                Tuple2<AnchorPane, RefreshPopover> tuple2 = loadFXML("/fxml/popover/RefreshPopover.fxml");
                AnchorPane root = tuple2.getT1();
                tuple2.getT2().setParentController(this);
                var pop = new Popover(root);
                pop.setHeaderAlwaysVisible(false);
                pop.setDetachable(false);
                pop.setArrowLocation(Popover.ArrowLocation.TOP_CENTER);
                pop.show(keyRefresh);
                refreshPopover= pop;
            }

        });
        showRefreshPopoverDelay.play();
    }

    @Deprecated
    public void closeRefreshPopover(MouseEvent mouseEvent) {
    }

    /**
     * 自动刷新的话，不显示更新频率
     * @param b
     * @param s
     */
    public void setUpdateRefreshText(boolean b, String s) {
        refreshTextFlag=b;
        refreshText.setText(s);
    }
}
