package xyz.hashdog.rdm.ui.entity;

/**
 * @Author th
 * @Date 2023/7/20 16:21
 */
public class ConnectionGroupNode {
    /**
     * 类型
     */
    private int type;
    /**
     * 定位id
     */
    private String dataId;
    /**
     * 父节点id
     */
    private String parentDataId;
    /**
     * 名称
     */
    private String name;

    public ConnectionGroupNode() {
        setType(1);
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentDataId() {
        return parentDataId;
    }

    public void setParentDataId(String parentDataId) {
        this.parentDataId = parentDataId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
