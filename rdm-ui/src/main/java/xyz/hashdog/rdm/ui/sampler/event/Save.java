package xyz.hashdog.rdm.ui.sampler.event;

import javafx.util.Duration;
import xyz.hashdog.rdm.ui.common.Applications;
import xyz.hashdog.rdm.ui.common.ConfigSettingsEnum;
import xyz.hashdog.rdm.ui.entity.config.ThemeSetting;
import xyz.hashdog.rdm.ui.sampler.theme.SamplerTheme;
import xyz.hashdog.rdm.ui.sampler.theme.ThemeManager;


public class Save {

    public static void init() {
        /*
          监听主题事件
         */
        DefaultEventBus.getInstance().subscribe(ThemeEvent.class, e -> {
            var eventType = e.getEventType();
            if (eventType == ThemeEvent.EventType.THEME_CHANGE ) {
                SamplerTheme theme = ThemeManager.getInstance().getTheme();
                ThemeSetting themeSetting=new ThemeSetting();
                themeSetting.setColorTheme(theme.getName());
                Applications.putConfigSettings(themeSetting.getName(),themeSetting);
            }
            if (eventType == ThemeEvent.EventType.FONT_CHANGE) {
                String fontFamily = ThemeManager.getInstance().getFontFamily();
                int fontSize = ThemeManager.getInstance().getFontSize();
                ThemeSetting themeSetting=new ThemeSetting();
                themeSetting.setFont(fontFamily);
                themeSetting.setFontSize(fontSize);
                Applications.putConfigSettings(themeSetting.getName(),themeSetting);
            }

        });
    }
}
