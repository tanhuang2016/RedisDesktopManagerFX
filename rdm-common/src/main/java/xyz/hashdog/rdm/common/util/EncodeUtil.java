package xyz.hashdog.rdm.common.util;

/**
 * @Author th
 * @Date 2023/7/19 16:59
 */
public class EncodeUtil {

    private static int byteToUnsignedInt(byte data) {
        return data & 0xff;
    }

    public static boolean isUTF8(byte[] pBuffer) {
        boolean IsUTF8 = true;
        boolean IsASCII = true;
        int size = pBuffer.length;
        int i = 0;
        while (i < size) {
            int value = byteToUnsignedInt(pBuffer[i]);
            if (value < 0x80) {
                // (10000000): 值小于 0x80 的为 ASCII 字符
                if (i >= size - 1) {
                    if (IsASCII) {
                        // 假设纯 ASCII 字符不是 UTF 格式
                        IsUTF8 = false;
                    }
                    break;
                }
                i++;
            } else if (value < 0xC0) {
                // (11000000): 值介于 0x80 与 0xC0 之间的为无效 UTF-8 字符
                IsASCII = false;
                IsUTF8 = false;
                break;
            } else if (value < 0xE0) {
                // (11100000): 此范围内为 2 字节 UTF-8 字符
                IsASCII = false;
                if (i >= size - 1) {
                    break;
                }

                int value1 = byteToUnsignedInt(pBuffer[i + 1]);
                if ((value1 & (0xC0)) != 0x80) {
                    IsUTF8 = false;
                    break;
                }

                i += 2;
            } else if (value < 0xF0) {
                IsASCII = false;
                // (11110000): 此范围内为 3 字节 UTF-8 字符
                if (i >= size - 2) {
                    break;
                }

                int value1 = byteToUnsignedInt(pBuffer[i + 1]);
                int value2 = byteToUnsignedInt(pBuffer[i + 2]);
                if ((value1 & (0xC0)) != 0x80 || (value2 & (0xC0)) != 0x80) {
                    IsUTF8 = false;
                    break;
                }

                i += 3;
            }  else if (value < 0xF8) {
                IsASCII = false;
                // (11111000): 此范围内为 4 字节 UTF-8 字符
                if (i >= size - 3) {
                    break;
                }

                int value1 = byteToUnsignedInt(pBuffer[i + 1]);
                int value2 = byteToUnsignedInt(pBuffer[i + 2]);
                int value3 = byteToUnsignedInt(pBuffer[i + 3]);
                if ((value1 & (0xC0)) != 0x80
                        || (value2 & (0xC0)) != 0x80
                        || (value3 & (0xC0)) != 0x80) {
                    IsUTF8 = false;
                    break;
                }

                i += 3;
            } else {
                IsUTF8 = false;
                IsASCII = false;
                break;
            }
        }

        return IsUTF8;
    }
}
