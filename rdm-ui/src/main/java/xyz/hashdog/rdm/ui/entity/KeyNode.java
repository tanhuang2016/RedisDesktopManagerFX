package xyz.hashdog.rdm.ui.entity;

import xyz.hashdog.rdm.ui.common.Applications;

/**
 * key实体类,，目录和key共用1个实体
 * @author th
 * @version 1.0.0
 * @since 2025/7/09 22:21
 */
public class KeyNode {
    public static final int KEY = 2;
    public static final int DIR = 1;
    /**
     * 类型,目录为1,key为2
     */
    private int type;
    /**
     * 名称
     */
    private String name;
    /**
     * key类型才有
     */
    private String key;
    /**
     * key类型
     */
    private String keyType;

    /**
     * 如果是目录就是key的数量，如果是key那就是key内存占用
     */
    private long size;
    /**
     * key的时效
     */
    private long ttl;


    /**
     * 是否是连接
     * @return
     */
    public boolean isKey() {
        return type==2;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }
}
