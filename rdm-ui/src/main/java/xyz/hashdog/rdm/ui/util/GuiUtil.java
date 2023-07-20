package xyz.hashdog.rdm.ui.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * @Author th
 * @Date 2023/7/20 13:10
 */
public class GuiUtil {

    /**
     * 提示框
     *
     * @param alertType 弹框类型
     * @param message   附带消息
     * @return 确定/取消
     */
    public static boolean alert(Alert.AlertType alertType, String message) {
        Alert a = new Alert(alertType);
        a.getDialogPane().getStylesheets().add("/css/global.css");
        a.setContentText(message);
        // 添加响应处理程序
        Optional<ButtonType> result = a.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }
}
