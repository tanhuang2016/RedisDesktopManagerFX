/* SPDX-License-Identifier: MIT */

package xyz.hashdog.rdm.ui.sampler.page.custom;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import xyz.hashdog.rdm.common.tuple.Tuple2;
import xyz.hashdog.rdm.ui.sampler.page.AbstractPage;
import xyz.hashdog.rdm.ui.util.GuiUtil;

import java.io.IOException;

public final class KeyTagPage extends AbstractPage {
    public static final String NAME = "Key tag";

    @Override
    public String getName() {
        return NAME;
    }

    public KeyTagPage() throws IOException {
        super();

        addPageHeader();
        addFormattedText("""
            A menu bar is a user interface component that typically appears at the top of \
            an application window or screen, and provides a series of drop-down menus that \
            allow users to access various features and functions of the application."""
        );
        Tuple2<AnchorPane, Object> tuple2 = GuiUtil.doLoadFXML("/fxml/setting/KeyTagPage.fxml");
//        AnchorPane t1 = tuple2.getT1();
//        Node node = t1.getChildren().get(0);
        addNode(tuple2.getT1());
    }


    ///////////////////////////////////////////////////////////////////////////

}
