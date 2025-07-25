package xyz.hashdog.rdm.ui.common;

import atlantafx.base.theme.Styles;

import java.util.List;

public enum TabPaneStyleEnum {
    DEFAULT("Default", List.of("whatever", Styles.TABS_FLOATING, Styles.TABS_CLASSIC)),
    FLOATING("Default",  List.of(Styles.TABS_FLOATING, "whatever", Styles.TABS_CLASSIC)),
    CLASSIC("Default", List.of(Styles.TABS_CLASSIC, "whatever", Styles.TABS_FLOATING)),
    ;

    public final String name;
    public final List<String> classes;

    TabPaneStyleEnum(String name, List<String> classes) {
        this.name = name;
        this.classes = classes;
    }
}
