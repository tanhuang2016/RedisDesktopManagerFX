package xyz.hashdog.rdm.ui.entity.config;

import javafx.geometry.Side;
import xyz.hashdog.rdm.ui.common.ConfigSettingsEnum;
import xyz.hashdog.rdm.ui.common.TabPaneStyleEnum;

public class KeyTabPaneSetting extends TabPaneSetting{



    @Override
    public TabPaneSetting init(){
        super.init();
        side= Side.BOTTOM.name();
        return this;
    }
    @Override
    public String getName() {
        return ConfigSettingsEnum.KEY_TAB_PANE.name;
    }
}
