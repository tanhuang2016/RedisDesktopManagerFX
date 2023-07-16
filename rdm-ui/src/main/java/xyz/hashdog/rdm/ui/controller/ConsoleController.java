package xyz.hashdog.rdm.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class ConsoleController {

    @FXML
    public TextArea textArea;


    // 绑定属性，使TextArea的高度随内容变化而自动调整
    @FXML
    private void initialize() {
        textArea.addEventFilter(ScrollEvent.SCROLL, ScrollEvent::consume);
//        textArea.addEventFilter(MouseEvent.MOUSE_CLICKED, MouseEvent::consume);
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            // 获取文本行数
            int lineCount = newValue.isEmpty() ? 1 : (newValue.split("\n", -1).length + 1);

            // 设置文本区域的行数和高度
            textArea.setPrefRowCount(lineCount);
            textArea.setPrefHeight(textArea.getFont().getSize() * lineCount );
            textArea.setStyle("-fx-background-color: transparent;");
        });
    }
}























