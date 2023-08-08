package xyz.hashdog.rdm.ui.handler;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import xyz.hashdog.rdm.common.util.FileUtil;

import java.nio.charset.Charset;

/**
 * 属于二进制类型
 * @Author th
 * @Date 2023/8/8 22:48
 */
public class ImageConvertHandler implements ValueConvertHandler{

    @Override
    public byte[] text2Byte(String text, Charset charset) {
        return FileUtil.binaryStringToByteArray(text);
    }

    @Override
    public String byte2Text(byte[] bytes, Charset charset) {
        return FileUtil.byteArrayToBinaryString(bytes);
    }

    @Override
    public Parent view(byte[] bytes, Charset charset) {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPadding(new Insets(10));
        anchorPane.setPrefHeight(500);
        anchorPane.setPrefWidth(500);
        return anchorPane;
    }

    @Override
    public boolean isView() {
        return true;
    }
}
