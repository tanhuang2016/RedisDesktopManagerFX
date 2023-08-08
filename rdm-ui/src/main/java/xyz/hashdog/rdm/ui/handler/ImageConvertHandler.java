package xyz.hashdog.rdm.ui.handler;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import xyz.hashdog.rdm.common.util.FileUtil;

import java.nio.charset.Charset;

/**
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
    public Node view(byte[] bytes, Charset charset) {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPadding(new Insets(10));
        return ValueConvertHandler.super.view(bytes, charset);
    }

    @Override
    public boolean isView() {
        return true;
    }
}
