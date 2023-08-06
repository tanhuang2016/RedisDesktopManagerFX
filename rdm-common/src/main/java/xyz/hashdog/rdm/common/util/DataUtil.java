package xyz.hashdog.rdm.common.util;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Author th
 * @Date 2023/7/20 16:30
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
}
