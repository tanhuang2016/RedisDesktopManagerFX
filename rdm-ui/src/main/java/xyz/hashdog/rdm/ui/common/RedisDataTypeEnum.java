package xyz.hashdog.rdm.ui.common;

import xyz.hashdog.rdm.ui.entity.PassParameter;
import xyz.hashdog.rdm.ui.exceptions.GeneralException;

/**
 * @Author th
 * @Date 2023/7/23 0:42
 */
public enum RedisDataTypeEnum {
    STRING("String","/fxml/StringTypeView.fxml", PassParameter.STRING),
    HASH("Hash","/fxml/HashTypeView.fxml", PassParameter.HASH),
    LIST("List","/fxml/ListTypeView.fxml", PassParameter.LIST),
    SET("Set","/fxml/SetTypeView.fxml", PassParameter.SET),
    ZSET("Zset","/fxml/ZsetTypeView.fxml", PassParameter.ZSET),
    ;


    public String type;
    public String fxml;
    public int tabType;
    RedisDataTypeEnum(String type,String fxml,int tabType) {
        this.type=type;
        this.fxml=fxml;
        this.tabType=tabType;
    }

    /**
     * 根据类型字符串获取
     * @param type
     * @return
     */
    public static RedisDataTypeEnum getByType(String type) {
        for (RedisDataTypeEnum value : values()) {
            if(value.type.equalsIgnoreCase(type)){
                return value;
            }
        }
        throw new GeneralException("This type is not supported "+type);
    }
}
