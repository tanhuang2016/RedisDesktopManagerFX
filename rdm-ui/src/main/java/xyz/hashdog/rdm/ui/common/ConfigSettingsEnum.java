package xyz.hashdog.rdm.ui.common;


import xyz.hashdog.rdm.ui.entity.config.ConfigSettings;
import xyz.hashdog.rdm.ui.entity.config.ThemeSetting;

public enum ConfigSettingsEnum {
    THEME("theme", ThemeSetting.class);

    public final String name;
    public final Class<? extends ConfigSettings> clazz;

    ConfigSettingsEnum(String name,Class<? extends ConfigSettings> clazz) {
        this.name=name;
        this.clazz=clazz;
    }
}
