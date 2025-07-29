package xyz.hashdog.rdm.ui.util;

import javafx.scene.control.ButtonBase;
import xyz.hashdog.rdm.ui.sampler.event.DefaultEventBus;
import xyz.hashdog.rdm.ui.sampler.event.ThemeEvent;
import xyz.hashdog.rdm.ui.sampler.theme.SamplerTheme;
import xyz.hashdog.rdm.ui.sampler.theme.ThemeManager;

import java.util.ArrayList;
import java.util.List;

public class SvgManager {
    private static final List<SvgManager> list = new ArrayList<>();
    private final ButtonBase base;
    private final String svg;
    private  static boolean light;
    static {
        light=ThemeManager.getInstance().getTheme().getName().contains("Light");
        DefaultEventBus.getInstance().subscribe(ThemeEvent.class, e -> {
            var eventType = e.getEventType();
            if (eventType == ThemeEvent.EventType.THEME_CHANGE ) {
                SamplerTheme theme = ThemeManager.getInstance().getTheme();
                boolean light1 = theme.getName().contains("Light");
                if(light1!=light){
                    light=light1;
                    changeSvg();
                }
            }
        });
    }

    private static void changeSvg() {
        for (SvgManager svgManager : list) {
            svgManager.setGraphic();
        }
    }

    private SvgManager(ButtonBase base, String svg) {
        this.base = base;
        this.svg = svg;
    }

    public static void load(ButtonBase base, String svg) {
        SvgManager svgManager = new SvgManager(base, svg);
        svgManager.setGraphic();
        list.add(svgManager);
    }

    private void setGraphic() {
        if(light){
            GuiUtil.setIcon(this.base,GuiUtil.svgImageView(this.svg));
        }else{
            GuiUtil.setIcon(this.base,GuiUtil.svgImageView(this.svg.replace(".svg","_dark.svg")));
        }
    }
}
