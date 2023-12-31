package xyz.hashdog.rdm.ui.handler;

import xyz.hashdog.rdm.common.util.GzipUtil;

import java.nio.charset.Charset;

/**
 * @author th
 * @version 1.0.0
 * @since 2023/8/8 22:48
 */
public class TextGzipConvertHandler implements ValueConvertHandler{

    @Override
    public byte[] text2Byte(String text, Charset charset) {
        return GzipUtil.compress(text,charset);
    }

    @Override
    public String byte2Text(byte[] bytes, Charset charset) {
        return GzipUtil.uncompress(bytes,charset);
    }


}
