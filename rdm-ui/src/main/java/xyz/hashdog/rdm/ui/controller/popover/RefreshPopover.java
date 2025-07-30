package xyz.hashdog.rdm.ui.controller.popover;

import atlantafx.base.controls.ToggleSwitch;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import xyz.hashdog.rdm.ui.util.GuiUtil;

import java.net.URL;
import java.util.ResourceBundle;

public  class RefreshPopover implements Initializable {
    public ToggleSwitch autoRefreshToggleSwitch;
    public TextField rate;
    private Timeline refreshTimeline;
    private Integer rateValue;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GuiUtil.filterIntegerInput(rate);
        autoRefreshToggleSwitch.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                startAutoRefresh();
            } else {
                stopAutoRefresh();
            }
        });
        // 监听光标离开输入框事件（失去焦点）
        rate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            // 如果 newValue 为 false，表示光标离开了输入框
            if (!newValue) {
                // 如果自动刷新正在运行，重启它以应用新的时间间隔
                if (autoRefreshToggleSwitch.isSelected() && rateValue!=Integer.parseInt(rate.getText())) {
                    stopAutoRefresh();
                    startAutoRefresh();
                }
            }
        });

    }

    private void stopAutoRefresh() {
        if (refreshTimeline != null) {
            refreshTimeline.stop();
        }
    }

    private void startAutoRefresh() {
        rateValue=Integer.parseInt(rate.getText());
        // 创建时间线，每5秒执行一次
        refreshTimeline = new Timeline(
                new KeyFrame(Duration.seconds(rateValue), event -> refresh())
        );
        refreshTimeline.setCycleCount(Timeline.INDEFINITE);
        refreshTimeline.play();
    }

    private void refresh() {
        System.out.println("刷新");
    }
}
