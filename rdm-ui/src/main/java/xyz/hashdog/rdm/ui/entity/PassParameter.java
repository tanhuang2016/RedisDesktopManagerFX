package xyz.hashdog.rdm.ui.entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * 参数传递
 * 父向子传递
 * @Author th
 * @Date 2023/7/23 22:28
 */
public class PassParameter {
    private int db;
    private StringProperty key=new SimpleStringProperty();

    public int getDb() {
        return db;
    }

    public void setDb(int db) {
        this.db = db;
    }

    public String getKey() {
        return key.get();
    }

    public StringProperty keyProperty() {
        return key;
    }

    public void setKey(String key) {
        this.key.set(key);
    }
}
