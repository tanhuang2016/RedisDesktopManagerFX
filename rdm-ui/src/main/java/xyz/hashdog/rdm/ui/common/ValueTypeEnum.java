package xyz.hashdog.rdm.ui.common;

import xyz.hashdog.rdm.ui.handler.HexConvertHandler;
import xyz.hashdog.rdm.ui.handler.TextConvertHandler;
import xyz.hashdog.rdm.ui.handler.TextJsonConvertHandler;
import xyz.hashdog.rdm.ui.handler.ValueConvertHandler;

/**
 * @Author th
 * @Date 2023/7/26 21:22
 */
public enum ValueTypeEnum {
    TEXT("Text",new TextConvertHandler()),
    JSON("Text(Json)",new TextJsonConvertHandler()),
    ZIP("Text(Zip)",new TextConvertHandler()),
    HEX("Hex",new HexConvertHandler()),
    BINARY("Binary",new TextConvertHandler()),
    IMAGE("Image",new TextConvertHandler()),
    IMAGE_BASE64("Image(Base64)",new TextConvertHandler()),
    ;


    public String name;
    public ValueConvertHandler handler;

    ValueTypeEnum(String name,ValueConvertHandler handler) {
        this.name = name;
        this.handler=handler;
    }

    public static ValueTypeEnum getByName(String newValue) {
        for (ValueTypeEnum value : ValueTypeEnum.values()) {
            if(value.name.equals(newValue)){
                return value;
            }
        }
        return null;
    }
}
