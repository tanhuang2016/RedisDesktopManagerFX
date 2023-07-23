package xyz.hashdog.rdm.ui.util;

import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import xyz.hashdog.rdm.ui.Main;

import java.util.Optional;

/**
 * 针对视图的操作工具
 * @Author th
 * @Date 2023/7/20 13:10
 */
public class GuiUtil {

    public static final Image ICON_GTOUP = new Image(Main.class.getResourceAsStream("/icon/group.png"));
    public static final Image ICON_CONNECTION = new Image(Main.class.getResourceAsStream("/icon/connection.png"));
    public static final Image ICON_REDIS =  new Image(Main.class.getResourceAsStream("/icon/redis.png"));
    private static final Image ICON_KEY =new Image(Main.class.getResourceAsStream("/icon/key.png"));

    /**
     * 创建新的连接图标
     * @return
     */
    public static ImageView creatConnctionImageView() {
        return GuiUtil.creatImageView(ICON_GTOUP,16,16);
    }

    /**
     * 创建新的分组图标
     * @return
     */
    public static ImageView creatGroupImageView() {
        return GuiUtil.creatImageView(ICON_CONNECTION,16,16);
    }
    /**
     * 创建新的key图标
     * @return
     */
    public static ImageView creatKeyImageView() {
        return GuiUtil.creatImageView(ICON_KEY,16,16);
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
     * 只允许输入整数
     * @param textFields
     */
    public static void filterIntegerInput(TextField... textFields) {
        for (TextField textField : textFields) {
            // 绑定监听器，当文本框内容发生变化时进行过滤
            textField.textProperty().addListener((observable, oldValue, newValue) -> {
                // 如果新的文本不是整数，则将文本还原为旧值
                if (!newValue.matches("\\d*")) {
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
        a.setContentText(message);
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
}
