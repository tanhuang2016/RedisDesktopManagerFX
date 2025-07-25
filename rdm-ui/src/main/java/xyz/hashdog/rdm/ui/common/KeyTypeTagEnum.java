package xyz.hashdog.rdm.ui.common;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum KeyTypeTagEnum {
    STRING("string",Constant.COLOR_STRING),
    LIST("list",Constant.COLOR_LIST),
    HASH("hash",Constant.COLOR_HASH),
    SET("set",Constant.COLOR_SET),
    ZSET("zset",Constant.COLOR_ZSET),
    JSON("json",Constant.COLOR_JSON),
    STREAM("stream",Constant.COLOR_STREAM),

    UNKNOWN("unknown",Constant.COLOR_UNKNOWN);
    public String tag;
    public String color;
    KeyTypeTagEnum(String tag, String color) {
        this.tag = tag;
        this.color = color;
    }



    public String getTag() {
        return tag;
    }

    public String getColor() {
        return color;
    }

    public static List<String> tags() {
        return Arrays.stream(KeyTypeTagEnum.values())
                .map(KeyTypeTagEnum::getTag)
                .collect(Collectors.toList());
    }

    public static List<String> colors() {
        return Arrays.stream(KeyTypeTagEnum.values())
                .map(KeyTypeTagEnum::getColor)
                .collect(Collectors.toList());
    }
}
