package xyz.hashdog.rdm.common.util;

import org.junit.Test;

import java.util.List;
import java.util.Locale;

/**
 * @Author th
 * @Date 2023/8/6 23:40
 */
public class DataUtilTest {

    @Test
    public void getFonts() {
        List<String> list= DataUtil.getFonts(Locale.CHINA);
        System.out.println(list);

    }
}
