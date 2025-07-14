package xyz.hashdog.rdm.ui.common;

public enum KeyTypeTagEnum {
    STRING("string",Constant.COLOR_STRING),
    HASH("hash",Constant.COLOR_HASH),
    LIST("list",Constant.COLOR_LIST),
    SET("set",Constant.COLOR_SET),
    ZSET("zset",Constant.COLOR_ZSET),
    STREAM("stream",Constant.COLOR_STREAM),
    JSON("json",Constant.COLOR_JSON),
    UNKNOWN("unknown",Constant.COLOR_UNKNOWN);
    public String tag;
    public String color;
    KeyTypeTagEnum(String tag, String color) {
        this.tag = tag;
        this.color = color;
    }


}
