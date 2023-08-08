package xyz.hashdog.rdm.ui.handler;

import xyz.hashdog.rdm.common.util.FileUtil;

import java.nio.charset.Charset;

/**
 * @Author th
 * @Date 2023/8/8 22:48
 */
public class BinaryConvertHandler implements ValueConvertHandler{

    @Override
    public byte[] text2Byte(String text, Charset charset) {
        return FileUtil.binaryStringToByteArray(text);
    }

    @Override
    public String byte2Text(byte[] bytes, Charset charset) {
        return FileUtil.byteArrayToBinaryString(bytes);

    }


}
