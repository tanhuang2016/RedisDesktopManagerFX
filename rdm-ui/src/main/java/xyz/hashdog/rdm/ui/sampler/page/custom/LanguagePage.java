/* SPDX-License-Identifier: MIT */

package xyz.hashdog.rdm.ui.sampler.page.custom;

import javafx.scene.layout.AnchorPane;
import xyz.hashdog.rdm.common.tuple.Tuple2;
import xyz.hashdog.rdm.ui.sampler.page.AbstractPage;
import xyz.hashdog.rdm.ui.util.GuiUtil;

import java.io.IOException;

import static xyz.hashdog.rdm.ui.util.LanguageManager.language;

public final class LanguagePage extends AbstractPage {
    public static final String NAME = language("main.setting.general.language");

    @Override
    public String getName() {
        return NAME;
    }

    public LanguagePage() throws IOException {
        super();

        addPageHeader();
        addFormattedText("""
            A menu bar is a user interface component that typically appears at the top of \
            an application window or screen, and provides a series of drop-down menus that \
            allow users to access various features and functions of the application."""
        );
        Tuple2<AnchorPane, Object> tuple2 = GuiUtil.doLoadFXML("/fxml/setting/LanguagePage.fxml");
        addNode(tuple2.getT1());
    }



}
