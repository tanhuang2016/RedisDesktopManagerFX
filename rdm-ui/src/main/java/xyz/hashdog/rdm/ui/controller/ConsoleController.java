package xyz.hashdog.rdm.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ConsoleController {

    @FXML
    public TextArea textArea;
    @FXML
    public TextField textField;


    // 绑定属性，使TextArea的高度随内容变化而自动调整
    @FXML
    private void initialize() {
//        textArea.addEventFilter(ScrollEvent.SCROLL, ScrollEvent::consume);
//        textArea.setPrefHeight(calculateContentHeight() );
//
////        textArea.addEventFilter(MouseEvent.MOUSE_CLICKED, MouseEvent::consume);
//        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
//            // 获取文本行数
//            int lineCount = newValue.isEmpty() ? 1 : (newValue.split("\n", -1).length + 1);
//
//            // 设置文本区域的行数和高度
//            textArea.setPrefRowCount(lineCount);
////            textArea.setPrefHeight(textArea.getFont().getSize() * lineCount );
//            textArea.setPrefHeight(calculateContentHeight() );
//            textArea.setStyle("-fx-background-color: transparent;");
//        });
    }

//    private double calculateContentHeight() {
//        // 获取每行文本的高度（不考虑行距）
//        Text tempText = new Text("H"); // 占一个字符的高度
//        tempText.setFont(Font.font(textArea.getFont().getFamily(), textArea.getFont().getSize()));
//        double lineHeight = tempText.getLayoutBounds().getHeight();
//
//        // 获取文本行数
//        int numLines = textArea.getText().split("\n").length;
//
//        // 获取行距
//        double lineSpacing = textArea.getFont().getSize() * 0.08; // 假设行距为字体大小的20%
//
//        // 计算内容高度（包括行距）
//        double contentHeight = lineHeight * numLines - lineSpacing * (numLines - 1);
//
//        return contentHeight;
//    }

    public void addToTextAreaAction(ActionEvent actionEvent) {
        String inputText = textField.getText();
        if (!inputText.isEmpty()) {
            textArea.appendText( "\n"+"docker-localhost:0> "+inputText );
        }
    }
}























