package xyz.hashdog.rdm.common.service;

/**
 * @Author th
 * @Date 2023/7/26 21:22
 */
public enum ValueTypeEnum {
    TEXT("Text"),
    HEX("Hex"),
    BINARY("Binary"),
    ZIP("Zip"),
    JSON("Json"),
    IMAGE("Image"),
    IMAGE_BASE64("Image(Base64)"),
    ;


    public String name;

    ValueTypeEnum(String name) {
        this.name = name;
    }
}
