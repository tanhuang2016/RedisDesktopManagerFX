package xyz.hashdog.rdm.redis.imp.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

/**
 * @Author th
 * @Date 2023/7/18 23:24
 */
public interface ReaderParser {
    /**
     * 解析
     * @param line
     * @param reader
     * @return
     */
    List<String> parse(String line, BufferedReader reader) throws IOException;
}
