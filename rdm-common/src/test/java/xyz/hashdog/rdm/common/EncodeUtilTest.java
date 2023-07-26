package xyz.hashdog.rdm.common;

import org.junit.Test;
import xyz.hashdog.rdm.common.util.EncodeUtil;

import java.nio.charset.Charset;

/**
 * @Author th
 * @Date 2023/7/25 22:56
 */
public class EncodeUtilTest {

    @Test
    public void containsSpecialCharactersTest(){
        System.out.println(EncodeUtil.containsSpecialCharacters("123".getBytes(Charset.forName("gbk"))));
        System.out.println(EncodeUtil.containsSpecialCharacters("123".getBytes()));
        System.out.println(EncodeUtil.containsSpecialCharacters("你好".getBytes()));
        byte[] gbks = "你好".getBytes(Charset.forName("gbk"));
        System.out.println(EncodeUtil.containsSpecialCharacters(gbks));
    }
}
