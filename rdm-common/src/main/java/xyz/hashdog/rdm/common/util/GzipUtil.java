package xyz.hashdog.rdm.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @Author th
 * @Date 2023/8/9 12:37
 */
public class GzipUtil {
    /**
     * 使用gzip压缩字符串
     */
    public static byte[] compress(String str, Charset charset) {
        if (str == null || str.length() == 0) {
            return new byte[0];
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = null;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes(charset));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (gzip != null) {
                try {
                    gzip.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return out.toByteArray();
    }

    /**
     * 使用gzip解压缩
     */
    public static String uncompress(byte [] compressed,Charset charset) {
        if (compressed == null || compressed.length == 0) {
            return "";
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = null;
        GZIPInputStream zip = null;
        String decompressed = null;
        try {
            in = new ByteArrayInputStream(compressed);
            zip = new GZIPInputStream(in);
            byte[] buffer = new byte[1024];
            int offset = -1;
            while ((offset = zip.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
            decompressed = out.toString(charset.displayName());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (zip != null) {
                    zip.close();
                }
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return decompressed;
    }
}
