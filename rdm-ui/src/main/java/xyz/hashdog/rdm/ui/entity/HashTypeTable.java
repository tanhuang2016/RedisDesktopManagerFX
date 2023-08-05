package xyz.hashdog.rdm.ui.entity;

/**
 * @Author th
 * @Date 2023/8/3 22:35
 */
public class HashTypeTable {

    private byte[] keyBytes;
    private String key;
    private byte[] bytes;
    private String value;

    public HashTypeTable(byte[] keyBytes,byte[] bytes) {
        this.keyBytes=keyBytes;
        this.key = new String(keyBytes);
        this.bytes = bytes;
        this.value = new String(bytes);
    }
    // 获取所有属性名称
    public static String[] getProperties() {
        return new String[]{"#row", "key","value"};
    }


    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
        this.value= new String(bytes);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public byte[] getKeyBytes() {
        return keyBytes;
    }

    public void setKeyBytes(byte[] keyBytes) {
        this.keyBytes = keyBytes;
        this.key= new String(keyBytes);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
