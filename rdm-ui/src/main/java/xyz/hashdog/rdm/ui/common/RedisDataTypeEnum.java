package xyz.hashdog.rdm.ui.common;

import xyz.hashdog.rdm.ui.exceptions.GeneralException;

/**
 * @Author th
 * @Date 2023/7/23 0:42
 */
public enum RedisDataTypeEnum {
    STRING("string","/fxml/StringTabView.fxml");


    public String type;
    public String fxml;
    RedisDataTypeEnum(String type,String fxml) {
        this.type=type;
        this.fxml=fxml;
    }

    /**
     * 根据类型字符串获取
     * @param type
     * @return
     */
    public static RedisDataTypeEnum getByType(String type) {
        for (RedisDataTypeEnum value : values()) {
            if(value.type.equals(type)){
                return value;
            }
        }
        throw new GeneralException("This type is not supported "+type);
    }
}
