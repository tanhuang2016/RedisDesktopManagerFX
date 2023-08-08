package xyz.hashdog.rdm.ui.handler;

import javafx.scene.Node;

import java.nio.charset.Charset;

/**
 * @Author th
 * @Date 2023/8/8 21:48
 */
public interface ValueConvertHandler {

    /**
     * 文本转二进制
     * @param text
     * @param charset
     * @return
     */
    byte[] text2Byte(String text, Charset charset);

    /**
     * 二进制转文本
     * @param bytes
     * @param charset
     * @return
     */
    String byte2Text(byte[] bytes, Charset charset);

    /**
     * 查看窗口的控件
     * @param bytes
     * @param charset
     * @return
     */
    default Node view(byte[] bytes, Charset charset){
        return null;
    }

    /**
     * 是否查看控件
     * @return
     */
    default boolean isView(){
        return false;
    }
}
