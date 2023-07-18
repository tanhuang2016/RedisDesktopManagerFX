package xyz.hashdog.rdm.redis.imp.console;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author th
 * @Date 2023/7/18 23:24
 */
public enum ReaderParseEnum {

    /**
     * 对于简单字符串（Simple Strings），回复的第一个字节是“+”
     */
    SIMPLE_STRINGS('+', (l, r) -> new ArrayList<String>() {{
        add(l.substring(1, l.length()));
    }}),
    /**
     * 对于错误（Errors ），回复的第一个字节是“-”
     */
    ERRORS('-', (l, r) -> new ArrayList<String>() {{
        add(l.substring(1, l.length()));
    }}),
    /**
     * 对于整数（Integers ），回复的第一个字节是“：”
     */
    INTEGERS(':', (l, r) -> new ArrayList<String>() {{
        add(l.substring(1, l.length()));
    }}),
    /**
     * 对于批量字符串（Bulk Strings），回复的第一个字节是“$”
     */
    BULK_STRINGS('$', (l, r) -> {
        final String newl = r.readLine();
        return new ArrayList<String>() {{
            add(newl);
        }};
    }),
    /**
     * 对于数组（Arrays ），回复的第一个字节是“*”
     */
    ARRAYS('*', (l, r) -> {
        List<String> result = new ArrayList<>();
        //返回的次数
        int count = Integer.parseInt(l.substring(1, l.length()));
        for (int i = 0; i < count; i++) {
            String next = r.readLine();
            ReaderParseEnum readerParseEnum = ReaderParseEnum.getByLine(next);
            List<String> parse = readerParseEnum.readerParser.parse(next, r);
            result.addAll(parse);
        }
        return result;
    }),
    ;

    /**
     * 根据响应的单行数据,进行判断对应那个枚举
     *
     * @param line
     * @return
     */
    public static ReaderParseEnum getByLine(String line) {
        char c = line.charAt(0);
        for (ReaderParseEnum value : ReaderParseEnum.values()) {
            if (value.mark == c) {
                return value;
            }
        }
        throw new RuntimeException("no this mark:" + c);
    }

    /**
     * 返回的协议标记
     */
    public char mark;
    /**
     * 解析器
     */
    public ReaderParser readerParser;


    ReaderParseEnum(char mark, ReaderParser readerParser) {
        this.mark = mark;
        this.readerParser = readerParser;
    }
}
