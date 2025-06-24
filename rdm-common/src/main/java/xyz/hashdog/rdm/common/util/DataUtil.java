package xyz.hashdog.rdm.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.awt.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author th
 * @version 1.0.0
 * @since 2023/7/20 16:30
 */
public class DataUtil {


    /**
     * 获取uuid
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 判断字符是否为空
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        return str==null||str.isEmpty();
    }
    /**
     * 判断字符是否不为空
     * @param str
     * @return
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 获取系统所有字体
     * @param locale
     * @return
     */
    public static java.util.List<String> getFonts(Locale locale) {
        java.util.List<String> fonts = new ArrayList<>();
        // 获取本地图形环境
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        // 获取所有可用的字体
        Font[] allFonts = ge.getAllFonts();
        fonts.addAll(Arrays.asList(allFonts).stream().map(e->e.getFontName(locale)).collect(Collectors.toList()));
        return fonts;
    }

    /**
     * json字符串格式化
     *
     * @param value   待格式化的字符串
     * @param charset 字符集
     * @return
     */
    public static String formatJson(byte[] value, Charset charset,boolean isFormat) {
        String s = new String(value, charset);
        // 创建一个 GsonBuilder 来配置 Gson 的格式化选项
        GsonBuilder gsonBuilder = new GsonBuilder();
        if(isFormat){
            gsonBuilder.setPrettyPrinting(); // 启用格式化输出
        }
        Gson gson = gsonBuilder.create();
        // 使用 Gson 格式化 JSON 字符串
        String formattedJson = gson.toJson(gson.fromJson(s, Object.class)); // 解析 JSON 字符串后再格式化
        return formattedJson;

    }

    /**
     * json转byte[]
     * @param value
     * @param charset
     * @param isFormat true是需要格式化
     * @return
     */
    public static byte[] json2Byte(String value, Charset charset,boolean isFormat) {
       return formatJson(value.getBytes(charset),charset,isFormat).getBytes();

    }
}
