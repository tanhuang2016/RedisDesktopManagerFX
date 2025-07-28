package xyz.hashdog.rdm.ui.entity.config;

import xyz.hashdog.rdm.ui.common.ConfigSettingsEnum;

import java.util.Locale;

public class LanguageSetting implements ConfigSettings{

    /**
     * 连接超时
     */
    private String localLanguage;
    private String localCountry;


    @Override
    public String getName() {
        return ConfigSettingsEnum.LANGUAGE.name;
    }

    @Override
    public LanguageSetting init() {
        this.localLanguage= Locale.getDefault().getLanguage();
        this.localCountry= Locale.getDefault().getCountry();
        return this;
    }

}
