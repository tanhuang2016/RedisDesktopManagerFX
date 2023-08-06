package xyz.hashdog.rdm.common;


import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author th
 * @Date 2023/7/18 13:12
 */
public class Constant {
    /**
     * 字符集常量
     */
    public static  final List<String> CHARSETS = Collections.unmodifiableList(Charset.availableCharsets().values().stream().map(e -> e.displayName()).collect(Collectors.toList()));

}
