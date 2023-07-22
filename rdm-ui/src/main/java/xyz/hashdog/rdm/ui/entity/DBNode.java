package xyz.hashdog.rdm.ui.entity;

/**
 *
 * 库单选框实体
 * @Author th
 * @Date 2023/7/22 15:27
 */
public class DBNode {
    /**
     * 名称
     */
    private String name;
    /**
     * 库号
     */
    private int db;

    public DBNode(String name, int db) {
        this.name = name;
        this.db = db;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDb() {
        return db;
    }

    public void setDb(int db) {
        this.db = db;
    }

    @Override
    public String toString() {
        return name;
    }
}
