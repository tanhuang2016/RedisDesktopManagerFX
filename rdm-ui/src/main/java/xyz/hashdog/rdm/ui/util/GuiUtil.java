package xyz.hashdog.rdm.ui.util;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import xyz.hashdog.rdm.common.pool.ThreadPool;
import xyz.hashdog.rdm.common.tuple.Tuple2;
import xyz.hashdog.rdm.ui.Main;
import xyz.hashdog.rdm.ui.common.Constant;
import xyz.hashdog.rdm.ui.controller.BaseController;
import xyz.hashdog.rdm.ui.controller.BaseKeyController;
import xyz.hashdog.rdm.ui.controller.ByteArrayController;
import xyz.hashdog.rdm.ui.entity.PassParameter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 针对视图的操作工具
 * @author th
 * @version 1.0.0
 * @since 2023/7/20 13:10
 */
public class GuiUtil {

    public static final Image ICON_GTOUP = new Image(Main.class.getResourceAsStream("/icon/group.png"));
    public static final Image ICON_CONNECTION = new Image(Main.class.getResourceAsStream("/icon/connection.png"));
    public static final Image ICON_REDIS =  new Image(Main.class.getResourceAsStream("/icon/redis256.png"));
    private static final Image ICON_KEY =new Image(Main.class.getResourceAsStream("/icon/key.png"));
    private static final Image ICON_CONSOLE =new Image(Main.class.getResourceAsStream("/icon/console.png"));

    /**
     * 系统剪贴板
     * @param copyString 文本
     */
    public static void copyString(String copyString) {
        Clipboard systemClipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(copyString);
        systemClipboard.setContent(content);
    }

    /**
     * 创建新的连接图标
     * @return
     */
    public static ImageView creatConnctionImageView() {
        return GuiUtil.creatImageView(ICON_CONNECTION ,16,16);
    }

    /**
     * 创建新的分组图标
     * @return
     */
    public static ImageView creatGroupImageView() {
        return GuiUtil.creatImageView(ICON_GTOUP,16,16);
    }
    /**
     * 创建新的key图标
     * @return
     */
    public static ImageView creatKeyImageView() {
        return GuiUtil.creatImageView(ICON_KEY,16,16);
    }
    /**
     * 创建新的控制台图标
     * @return
     */
    public static ImageView creatConsoleImageView() {
        return GuiUtil.creatImageView(ICON_CONSOLE,16,16);
    }

    /**
     *  创建String绑定对象
     *  重新toString,是为了展示在ui上
     * @param key
     * @return
     */
    public static StringProperty creatStringProperty(String key) {
        return new SimpleStringProperty(key){
            @Override
            public String toString() {
                return get();
            }
        };
    }

    /**
     * 只允许输入整数,可以负数
     * @param textFields
     */
    public static void filterIntegerInput(TextField... textFields) {
        filterIntegerInput(true,textFields);
    }
    /**
     * flg为true允许输入整数,可以负数
     * flg为false只能正整数
     * @param textFields
     */
    public static void filterIntegerInput(boolean flg,TextField... textFields) {
        for (TextField textField : textFields) {
            // 绑定监听器，当文本框内容发生变化时进行过滤
            textField.textProperty().addListener((observable, oldValue, newValue) -> {
                // 如果新的文本不是整数，则将文本还原为旧值
                if (flg&&!newValue.matches("-?\\d*")) {
                    textField.setText(oldValue);
                }
                if (!flg&&!newValue.matches("\\d*")) {
                    textField.setText(oldValue);
                }
            });
        }
    }

    /**
     * 返回为true证明有未填的
     *
     * @param textFields
     * @return
     */
    public static boolean requiredTextField(TextField... textFields) {
        boolean flg = false;
        for (TextField textField : textFields) {
            String text = textField.getText();
            if (text.isEmpty()) {
                // 当名字未填写时，将TextField的边框颜色设置为红色
                textField.setStyle("-fx-border-color: red;");
                flg = true;
            } else {
                // 当名字已填写时，将TextField的边框颜色还原为默认值
                textField.setStyle("");
            }
        }
        return flg;
    }

    /**
     * 提示框
     *
     * @param alertType 弹框类型
     * @param message   附带消息
     * @return 确定/取消
     */
    public static boolean alert(Alert.AlertType alertType, String message) {
        Alert a=createAlert(alertType,message);
        // 添加响应处理程序
        Optional<ButtonType> result = a.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 瞬间显示
     * @param message
     */
    public static void alertMoment( String message) {
        Alert a=createAlert(Alert.AlertType.NONE,message);
        Stage window = (Stage) a.getDialogPane().getScene().getWindow();
        a.show();
        // 设置PauseTransition暂停1秒
        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        delay.setOnFinished(event -> {
            // 在暂停时间结束后关闭对话框
            window.close();
        });
        // 启动PauseTransition
        delay.play();

    }

    /**
     * 创建alert
     * @param alertType 类型
     * @param message 消息
     * @return
     */
    private static Alert createAlert(Alert.AlertType alertType, String message) {
        Alert a = new Alert(alertType);
        a.getDialogPane().getStylesheets().add("/css/global.css");
        a.setHeaderText(Main.RESOURCE_BUNDLE.getString("alert."+alertType.name().toLowerCase()));
        a.setContentText(message);


        // 设置按钮文本
        Button okButton = (Button) a.getDialogPane().lookupButton(ButtonType.OK);
        if(okButton!=null){
            okButton.setText(Main.RESOURCE_BUNDLE.getString(Constant.OK));
        }

        Button cancelButton = (Button) a.getDialogPane().lookupButton(ButtonType.CANCEL);
        if(cancelButton!=null){
            cancelButton.setText(Main.RESOURCE_BUNDLE.getString(Constant.CANCEL));
        }
        return a;
    }

    /**
     * 创建新的ImageView
     * @param icon 图标
     * @param w 宽
     * @param h 高
     * @return
     */
    public static ImageView creatImageView(Image icon, int w, int h) {
        ImageView imageView = new ImageView(icon);
        imageView.setFitWidth(w);
        imageView.setFitHeight(h);
        return imageView;

    }


    /**
     * 所有节点展开
     * @param item
     */
    public static void expandAllNodes(TreeItem<?> item) {
        if (item != null && !item.isLeaf()) {
            item.setExpanded(true);
            for (TreeItem<?> child : item.getChildren()) {
                expandAllNodes(child);
            }
        }
    }

    /**
     * 递归方法，根据key查找并删除TreeView中的节点
     * @param parent
     * @param key
     */
    public static <T>void deleteTreeNodeByKey(TreeItem<T> parent, T key) {
        for (TreeItem<T> child : parent.getChildren()) {
            if (child.getValue().equals(key)) {
                parent.getChildren().remove(child); // 找到节点后，从父节点的子节点列表中移除它
                return;
            } else {
                deleteTreeNodeByKey(child, key); // 递归查找子节点
            }
        }
    }

    /**
     * 创建新的邮件菜单,切添加菜单事件
     * @param tab
     * @return
     */
    public static ContextMenu newTabContextMenu(Tab tab) {
        MenuItem close = new MenuItem(Main.RESOURCE_BUNDLE.getString(Constant.CLOSE));
        MenuItem closeOther = new MenuItem(Main.RESOURCE_BUNDLE.getString(Constant.CLOSE_OTHER));
        MenuItem closeLeft = new MenuItem(Main.RESOURCE_BUNDLE.getString(Constant.CLOSE_LEFT));
        MenuItem closeRight = new MenuItem(Main.RESOURCE_BUNDLE.getString(Constant.CLOSE_RIGHT));
        MenuItem closeAll = new MenuItem(Main.RESOURCE_BUNDLE.getString(Constant.CLOSE_ALL));
        ContextMenu cm = new ContextMenu(close,closeOther,closeLeft,closeRight,closeAll);
//        cm.setOpacity(0.8d);
        tab.setContextMenu(cm);
        // 关闭当前
        close.setOnAction(event -> {
            // 获取触发事件的MenuItem
            MenuItem clickedMenuItem = (MenuItem) event.getSource();
            // 获取与MenuItem关联的ContextMenu
            ContextMenu triggeredMenu = clickedMenuItem.getParentPopup();
            TabPane tabPane = tab.getTabPane();
            for (Tab tab1 : tabPane.getTabs()) {
                if (tab1.getContextMenu() == triggeredMenu) {
                    //关闭redis连接,移除tab
                    closeTab(tabPane,tab1);
                    break;
                }
            }
        });

        // 关闭其他
        closeOther.setOnAction(event -> {
            // 获取触发事件的MenuItem
            MenuItem clickedMenuItem = (MenuItem) event.getSource();
            // 获取与MenuItem关联的ContextMenu
            ContextMenu triggeredMenu = clickedMenuItem.getParentPopup();
            TabPane tabPane = tab.getTabPane();
            List<Tab> dels  = new ArrayList<>();
            for (Tab tab1 : tabPane.getTabs()) {
                if (tab1.getContextMenu() != triggeredMenu) {
                    dels.add(tab1);
                }
            }
            //关闭redis连接,移除tab
            closeTab(tabPane,dels);
        });

        // 关闭左边所有
        closeLeft.setOnAction(event -> {
            // 获取触发事件的MenuItem
            MenuItem clickedMenuItem = (MenuItem) event.getSource();
            // 获取与MenuItem关联的ContextMenu
            ContextMenu triggeredMenu = clickedMenuItem.getParentPopup();
            TabPane tabPane = tab.getTabPane();
            List<Tab> dels  = new ArrayList<>();
            for (Tab tab1 : tabPane.getTabs()) {
                if (tab1.getContextMenu() != triggeredMenu) {
                    dels.add(tab1);
                }else{
                    break;
                }
            }
            //关闭redis连接,移除tab
            closeTab(tabPane,dels);
        });

        // 关闭右边所有
        closeRight.setOnAction(event -> {
            // 获取触发事件的MenuItem
            MenuItem clickedMenuItem = (MenuItem) event.getSource();
            // 获取与MenuItem关联的ContextMenu
            ContextMenu triggeredMenu = clickedMenuItem.getParentPopup();
            TabPane tabPane = tab.getTabPane();
            boolean flg=false;
            List<Tab> dels  = new ArrayList<>();
            for (Tab tab1 : tabPane.getTabs()) {
                if (tab1.getContextMenu() == triggeredMenu) {
                    flg=true;
                }else{
                    if(flg){
                        dels.add(tab1);
                    }
                }
            }
            //关闭redis连接,移除tab
            closeTab(tabPane,dels);
        });
        // 关闭所有
        closeAll.setOnAction(event -> {
            TabPane tabPane = tab.getTabPane();
            List<Tab> dels = new ArrayList<>(tabPane.getTabs());
            //关闭redis连接,移除tab
            closeTab(tabPane,dels);
        });
        return cm;
    }

    /**
     * 关闭多个
     * @param tabPane
     * @param dels
     */
    private static void closeTab(TabPane tabPane, List<Tab> dels) {
        for (Tab del : dels) {
            closeTab(tabPane,del);
        }
    }

    /**
     * 关闭redis连接,移除tab
     * @param tabPane
     * @param selectedTab
     */
    private static void closeTab(TabPane tabPane,Tab selectedTab) {
        BaseKeyController userData = (BaseKeyController)selectedTab.getContent().getUserData();
        //CONSOLE类型需要关闭redis连接
        if(userData.getParameter().getTabType()== PassParameter.CONSOLE){
            ThreadPool.getInstance().execute(()->userData.getRedisClient().close());
        }
        if(userData.getParameter().getTabType()== PassParameter.REDIS){
            ThreadPool.getInstance().execute(()->userData.getRedisContext().close());
        }
        tabPane.getTabs().remove(selectedTab);
    }

    /**
     * 是否删除弹窗
     * @return
     */
    public static boolean alertRemove() {
       return GuiUtil.alert(Alert.AlertType.CONFIRMATION, Main.RESOURCE_BUNDLE.getString(Constant.ALERT_MESSAGE_DEL));
    }

    /**
     * 视图上删除对应数据
     *
     * @param lastSelect
     */
    public static <T>void remove2UI(ObservableList<T> list, TableView<T> tableView, T lastSelect) {
        Platform.runLater(() -> {
            //缓存的所有数据需要删除
            list.remove(lastSelect);
        });
        int i = tableView.getItems().indexOf(lastSelect);
        if (i > -1) {
            Platform.runLater(() -> {
                //视图需要删除
                tableView.getItems().remove(i);
                tableView.refresh();
            });
        }
    }

    /**
     * 创建通用子stae
     * @param title
     * @param anchorPane
     * @param window
     * @return
     */
    public static Stage createSubStage(String title, Parent anchorPane, Window window) {
        Stage stage = new Stage();
        stage.setTitle(title);
        Scene scene = new Scene(anchorPane);
        stage.initOwner(window);
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        return stage;
    }



    /**
     * 加载byteArrayView
     *
     * @param bytes
     * @return
     */
    public static Tuple2<AnchorPane, ByteArrayController> loadByteArrayView(byte[] bytes, BaseController baseController) {
        Tuple2<AnchorPane, ByteArrayController> tuple2 = baseController.loadFXML("/fxml/ByteArrayView.fxml");
        tuple2.getT2().setParentController(baseController);
        tuple2.getT2().setByteArray(bytes);
        return tuple2;
    }


    /**
     * 用于tableView压缩为单行
     * @param <T>
     */
    public static class OneLineTableCell<T> extends TableCell<T, Object> {
        @Override
        protected void updateItem(Object item, boolean empty) {
            super.updateItem(item, empty);

            if (item != null) {
                if(item instanceof String){
                    String item2=(String)item;
                    setText(item2.replaceAll("\\s+", " ").trim());
                }else if(item instanceof Number) {
                    String item2=item.toString();
                    setText(item2.replaceAll("\\s+", " ").trim());
                }
            } else {
                setText(null);
            }
        }
    }


    /**
     * 文件选择
     *
     * @param ownerWindow
     * @param last
     * @return
     */
    public static File fileChoose(Window ownerWindow, File last) {
        return fileChoose(ownerWindow,last,"file(*)","*.*");
    }
    public static File fileChoose(Window ownerWindow, File last, String description, String... extensions) {
        FileChooser fileChooser =createFileChooser(last,"文件选择",description,extensions);
        File chooseFile = fileChooser.showOpenDialog(ownerWindow);
        return chooseFile;
    }

    /**
     * 创建文件选择器
     * @param last 最后一个文件
     * @param title 标题
     * @param description
     * @param extensions
     * @return
     */
    private static FileChooser createFileChooser(File last, String title, String description, String... extensions) {
        FileChooser fileChooser = new FileChooser();
        if (last != null && last.exists() && last.isDirectory()) {
            fileChooser.setInitialDirectory(last);
        }
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(description, extensions);
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().add(extFilter);
        return fileChooser;
    }

    /**
     * 文件选择
     *
     * @param ownerWindow
     * @param last
     * @return
     */
    public static File savaFileChoose(Window ownerWindow, File last) {
        FileChooser fileChooser =createFileChooser(last,"文件保存","file(*)","*.*");
        File chooseFile =  fileChooser.showSaveDialog(ownerWindow);
        return chooseFile;
    }
}
