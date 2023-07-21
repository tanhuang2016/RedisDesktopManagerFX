package xyz.hashdog.rdm.common.util;

import java.util.UUID;

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
}
