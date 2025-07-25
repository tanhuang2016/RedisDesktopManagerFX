package xyz.hashdog.rdm.ui.common;


import xyz.hashdog.rdm.ui.entity.config.*;

public enum ConfigSettingsEnum {
    THEME("theme", ThemeSetting.class),
    KEY_TAG("keyTag", KeyTagSetting.class ),
    ADVANCED("advanced", AdvancedSetting.class ),
    SERVER_TAB_PAN("serverTabPan", ServerTabPanSetting.class ),
    ;

    public final String name;
    public final Class<? extends ConfigSettings> clazz;

    ConfigSettingsEnum(String name,Class<? extends ConfigSettings> clazz) {
        this.name=name;
        this.clazz=clazz;
    }
}
