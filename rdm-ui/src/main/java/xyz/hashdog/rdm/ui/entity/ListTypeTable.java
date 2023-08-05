package xyz.hashdog.rdm.ui.entity;

/**
 * @Author th
 * @Date 2023/8/3 22:35
 */
public class ListTypeTable {

    private byte[] bytes;
    private String value;

    public ListTypeTable( byte[] bytes) {
        this.bytes = bytes;
        this.value = new String(bytes);
    }
    // 获取所有属性名称
    public static String[] getProperties() {
        return new String[]{"row", "value"};
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

}
