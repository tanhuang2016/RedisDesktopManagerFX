package xyz.hashdog.rdm.common.service;

/**
 * @Author th
 * @Date 2023/7/26 21:22
 */
public enum ValueTypeEnum {
    TEXT("Text"),
    JSON("Text(Json)"),
    ZIP("Text(Zip)"),
    HEX("Hex"),
    BINARY("Binary"),
    IMAGE("Image"),
    IMAGE_BASE64("Image(Base64)"),
    ;


    public String name;

    ValueTypeEnum(String name) {
        this.name = name;
    }
}
