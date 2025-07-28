/* SPDX-License-Identifier: MIT */

package xyz.hashdog.rdm.ui.sampler.page.custom;

import atlantafx.base.controls.CaptionMenuItem;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import net.datafaker.Faker;
import org.jetbrains.annotations.Nullable;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2OutlinedAL;
import xyz.hashdog.rdm.common.tuple.Tuple2;
import xyz.hashdog.rdm.ui.Main;
import xyz.hashdog.rdm.ui.sampler.page.AbstractPage;
import xyz.hashdog.rdm.ui.util.GuiUtil;

import java.io.IOException;
import java.util.stream.IntStream;

import static javafx.scene.input.KeyCombination.CONTROL_DOWN;
import static javafx.scene.input.KeyCombination.SHIFT_DOWN;
import static xyz.hashdog.rdm.ui.util.LanguageManager.language;

public final class AdvancedPage extends AbstractPage {
    public static final String NAME = language("main.setting.global.advanced");

    @Override
    public String getName() {
        return NAME;
    }

    public AdvancedPage() throws IOException {
        super();

        addPageHeader();
        addFormattedText(language("main.setting.general.advanced.describe"));
        Tuple2<AnchorPane, Object> tuple2 = GuiUtil.doLoadFXML("/fxml/setting/AdvancedPage.fxml");
        addNode(tuple2.getT1());
    }


    ///////////////////////////////////////////////////////////////////////////

}
