package xyz.hashdog.rdm.ui.handler;

import java.nio.charset.Charset;

/**
 * @Author th
 * @Date 2023/8/8 22:48
 */
public class TextConvertHandler implements ValueConvertHandler{

    @Override
    public byte[] text2Byte(String text, Charset charset) {
        return text.getBytes(charset);
    }

    @Override
    public String byte2Text(byte[] bytes, Charset charset) {
        return new String(bytes,charset);
    }


}
