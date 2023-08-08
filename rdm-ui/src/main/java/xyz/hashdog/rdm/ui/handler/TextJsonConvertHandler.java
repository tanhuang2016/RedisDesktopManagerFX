package xyz.hashdog.rdm.ui.handler;

import xyz.hashdog.rdm.common.util.DataUtil;

import java.nio.charset.Charset;

/**
 * @Author th
 * @Date 2023/8/8 22:48
 */
public class TextJsonConvertHandler implements ValueConvertHandler{

    @Override
    public byte[] text2Byte(String text, Charset charset) {
        return DataUtil.json2Byte(text,charset,false);
    }

    @Override
    public String byte2Text(byte[] bytes, Charset charset) {
        return DataUtil.formatJson(bytes,charset,true);
    }


}
