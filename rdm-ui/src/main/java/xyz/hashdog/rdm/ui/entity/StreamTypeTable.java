package xyz.hashdog.rdm.ui.entity;

/**
 * @author th
 * @version 2.0.0
 * @since 2025/7/14 22:35
 */
public class StreamTypeTable {


    private String id;
    private byte[] bytes;
    private String value;

    public StreamTypeTable(String id, String value) {
        this.id=id;

        this.bytes = value.getBytes();
        this.value = value;
    }
    // 获取所有属性名称
    public static String[] getProperties() {
        return new String[]{"#row", "id","value"};
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
        this.value= new String(bytes);
    }


}
