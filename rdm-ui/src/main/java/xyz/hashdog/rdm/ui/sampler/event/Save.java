package xyz.hashdog.rdm.ui.sampler.event;

import javafx.util.Duration;
import xyz.hashdog.rdm.ui.sampler.theme.SamplerTheme;
import xyz.hashdog.rdm.ui.sampler.theme.ThemeManager;


public class Save {

    public static void init() {
        DefaultEventBus.getInstance().subscribe(ThemeEvent.class, e -> {
            var eventType = e.getEventType();
            if (eventType == ThemeEvent.EventType.THEME_CHANGE ) {
                SamplerTheme theme = ThemeManager.getInstance().getTheme();
                System.out.println(theme.getName());
            }
        });
    }
}
