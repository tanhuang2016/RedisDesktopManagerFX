package xyz.hashdog.rdm.ui.util;

import java.util.*;

public class LanguageManager {

    public static final String BASE_NAME = "i18n.messages";

    public static Locale DEFAULT_LOCALE = Locale.getDefault();
    public static List<Locale> getAvailableLocales(String baseName) {
        List<Locale> availableLocales = new ArrayList<>();

        // 获取系统支持的所有语言环境
        Locale[] locales = Locale.getAvailableLocales();
        for (Locale locale : locales) {
            try {
                ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale);
                if (bundle.getLocale().equals(locale) ||
                        bundle.getLocale().equals(Locale.ROOT)) {
                    availableLocales.add(locale);
                }
            } catch (MissingResourceException e) {
                // 资源文件不存在，跳过该语言环境
            }
        }

        return availableLocales;
    }
}
