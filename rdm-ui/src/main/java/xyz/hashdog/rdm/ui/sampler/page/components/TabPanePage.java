/* SPDX-License-Identifier: MIT */

package xyz.hashdog.rdm.ui.sampler.page.components;

import atlantafx.base.controls.Spacer;
import atlantafx.base.controls.ToggleSwitch;
import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.BBCodeParser;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import xyz.hashdog.rdm.ui.common.RedisDataTypeEnum;
import xyz.hashdog.rdm.ui.common.TabPaneStyleEnum;
import xyz.hashdog.rdm.ui.sampler.event.DefaultEventBus;
import xyz.hashdog.rdm.ui.sampler.event.EventBus;
import xyz.hashdog.rdm.ui.sampler.event.TabPaneEvent;
import xyz.hashdog.rdm.ui.sampler.event.ThemeEvent;
import xyz.hashdog.rdm.ui.sampler.page.ExampleBox;
import xyz.hashdog.rdm.ui.sampler.page.OutlinePage;
import xyz.hashdog.rdm.ui.sampler.page.Snippet;

import java.util.List;
import java.util.UUID;

import static javafx.scene.control.TabPane.TabClosingPolicy;

public final class TabPanePage extends OutlinePage {

    public static final String NAME = "TabPane";
    public static Sub server;
    public static Sub key;
    private static final EventBus EVENT_BUS = DefaultEventBus.getInstance();

    @Override
    public String getName() {
        return NAME;
    }

    public TabPanePage() {
        super();

        addPageHeader();
        addFormattedText("""
            [i]TabPane[/i] is a control that provides a container for a group of tabs. By clicking \
            on a tab, the content of that tab becomes visible, while the content of the previously \
            selected tab gets hidden."""
        );
        server = getSub(1);
        key = getSub(2);
        addSection("Server Playground", server.pane);
        addSection("Key Playground", key.pane);
    }

    private Sub getSub(int type) {
        Sub sub = new Sub(type);
        Pane playground = sub.playground();
        sub.pane = playground;
        return sub;
    }


    ///////////////////////////////////////////////////////////////////////////
    // Playground                                                            //
    ///////////////////////////////////////////////////////////////////////////


    class Sub{
        private final int type;
        private Side tabSide = Side.TOP;
        private boolean fullWidth = false;
        private Pane pane;
        private TabPaneEvent.EventType eventType;

        public Sub(int type) {
            this.type=type;
            if(type==2){
                tabSide=Side.BOTTOM;
                eventType=TabPaneEvent.EventType.KEY_TAB_PANE_CHANGE;
            }else {
                eventType=TabPaneEvent.EventType.SERVER_TAB_PANE_CHANGE;
            }
        }

        private Pane playground() {
            var tabs = createTabPane();
            tabs.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);

            var tabsLayer = new BorderPane();
            tabsLayer.setTop(tabs);
            tabs.getTabs().addListener((ListChangeListener<Tab>) c ->
                    updateTabsWidth(tabsLayer, tabs, fullWidth)
            );

            var controller = createController(tabsLayer, tabs);

            var controllerLayer = new BorderPane(controller);
            controllerLayer.setMinSize(500, 300);
            controllerLayer.setMaxSize(500, 300);

            var description = BBCodeParser.createFormattedText("""
            The playground demonstrates the most important [i]TabPane[/i] features \
            and also serves as an object for monkey testing."""
            );

            var stack = new StackPane(tabsLayer, controllerLayer);
            stack.getStyleClass().add(Styles.BORDERED);
            stack.setMinSize(600, 500);

            return new VBox(VGAP_10, description, stack);
        }


        private TitledPane createController(BorderPane borderPane, TabPane tabs) {
            // == BUTTONS ==

            var toTopBtn = new Button(null, new FontIcon(Feather.ARROW_UP));
            toTopBtn.getStyleClass().addAll(Styles.BUTTON_ICON);
            toTopBtn.setOnAction(e -> rotateTabs(borderPane, tabs, Side.TOP));

            var toRightBtn = new Button(null, new FontIcon(Feather.ARROW_RIGHT));
            toRightBtn.getStyleClass().addAll(Styles.BUTTON_ICON);
            toRightBtn.setOnAction(e -> rotateTabs(borderPane, tabs, Side.RIGHT));

            var toBottomBtn = new Button(null, new FontIcon(Feather.ARROW_DOWN));
            toBottomBtn.getStyleClass().addAll(Styles.BUTTON_ICON);
            toBottomBtn.setOnAction(e -> rotateTabs(borderPane, tabs, Side.BOTTOM));

            var toLeftBtn = new Button(null, new FontIcon(Feather.ARROW_LEFT));
            toLeftBtn.getStyleClass().addAll(Styles.BUTTON_ICON);
            toLeftBtn.setOnAction(e -> rotateTabs(borderPane, tabs, Side.LEFT));

            var appendBtn = new Button(null, new FontIcon(Feather.PLUS));
            appendBtn.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.ACCENT);
            if(type==1){
                appendBtn.setOnAction(e -> tabs.getTabs().add(createRandomTab()));

            }else {
                appendBtn.setOnAction(e -> tabs.getTabs().add(createRandomTabKey()));
            }

            var buttonsPane = new BorderPane();
            buttonsPane.setMinSize(120, 120);
            buttonsPane.setMaxSize(120, 120);

            buttonsPane.setCenter(appendBtn);

            buttonsPane.setTop(toTopBtn);
            BorderPane.setAlignment(toTopBtn, Pos.CENTER);

            buttonsPane.setRight(toRightBtn);
            BorderPane.setAlignment(toRightBtn, Pos.CENTER);

            buttonsPane.setBottom(toBottomBtn);
            BorderPane.setAlignment(toBottomBtn, Pos.CENTER);

            buttonsPane.setLeft(toLeftBtn);
            BorderPane.setAlignment(toLeftBtn, Pos.CENTER);

            // == TOGGLES ==

            var closeableToggle = new ToggleSwitch();
            closeableToggle.selectedProperty().addListener((obs, old, val) -> {
                if (val) {
                    tabs.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
                } else {
                    tabs.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
                }
            });

            var tagToggle = new ToggleSwitch();
            tagToggle.setSelected(false);
            tagToggle.setDisable(true);
            tagToggle.selectedProperty().addListener((obs, old, val) -> {
                if (val != null && val) {
                    tabs.setStyle("");
                } else {
                    tabs.setStyle("""
                    -fx-open-tab-animation:none;\
                    -fx-close-tab-animation:none;"""
                    );
                }
            });

            var animatedToggle = new ToggleSwitch();
            animatedToggle.setSelected(true);
            animatedToggle.selectedProperty().addListener((obs, old, val) -> {
                if (val != null && val) {
                    tabs.setStyle("");
                } else {
                    tabs.setStyle("""
                    -fx-open-tab-animation:none;\
                    -fx-close-tab-animation:none;"""
                    );
                }
                EVENT_BUS.publish(new TabPaneEvent(eventType));
            });

            var fullWidthToggle = new ToggleSwitch();
            fullWidthToggle.selectedProperty().addListener((obs, old, val) -> {
                if (val != null) {
                    updateTabsWidth(borderPane, tabs, val);
                    fullWidth = val;
                    EVENT_BUS.publish(new TabPaneEvent(eventType));
                }
            });

            var denseToggle = new ToggleSwitch();
            denseToggle.selectedProperty().addListener(
                    (obs, old, val) ->{
                        Styles.toggleStyleClass(tabs, Styles.DENSE);
                        EVENT_BUS.publish(new TabPaneEvent(eventType));
                    }

            );

            var disableToggle = new ToggleSwitch();
            disableToggle.selectedProperty().addListener((obs, old, val) -> {
                if (val != null) {
                    tabs.setDisable(val);
                }
            });

            var togglesGrid = new GridPane();
            togglesGrid.setHgap(10);
            togglesGrid.setVgap(10);
//        togglesGrid.addRow(0, createGridLabel("Closeable"), closeableToggle);
            if(type==2){
                togglesGrid.addRow(0, createGridLabel("Key Tag Color"), tagToggle);
            }
            togglesGrid.addRow(1, createGridLabel("Animated"), animatedToggle);
            togglesGrid.addRow(2, createGridLabel("Full width"), fullWidthToggle);
            togglesGrid.addRow(3, createGridLabel("Dense"), denseToggle);
//        togglesGrid.addRow(4, createGridLabel("Disable"), disableToggle);

            // == TAB STYLE ==

            var styleToggleGroup = new ToggleGroup();

            var defaultStyleToggle = new ToggleButton("Default");
            defaultStyleToggle.setToggleGroup(styleToggleGroup);
            defaultStyleToggle.setUserData(
                    TabPaneStyleEnum.DEFAULT.classes
            );
            defaultStyleToggle.setSelected(true);

            var floatingStyleToggle = new ToggleButton("Floating");
            floatingStyleToggle.setToggleGroup(styleToggleGroup);
            floatingStyleToggle.setUserData(
                    TabPaneStyleEnum.FLOATING.classes
            );

            var classicStyleToggle = new ToggleButton("Classic");
            classicStyleToggle.setToggleGroup(styleToggleGroup);
            classicStyleToggle.setUserData(
                    TabPaneStyleEnum.CLASSIC.classes
            );

            styleToggleGroup.selectedToggleProperty().addListener((obs, old, val) -> {
                if (val != null) {
                    List<String> classes = (List<String>) val.getUserData();
                    Styles.addStyleClass(tabs, classes.get(0), classes.get(1), classes.get(2));
                    EVENT_BUS.publish(new TabPaneEvent(eventType));
                }
            });

            var styleBox = new InputGroup(
                    defaultStyleToggle, floatingStyleToggle, classicStyleToggle
            );
            styleBox.setAlignment(Pos.CENTER);

            // == LAYOUT ==

            var controls = new HBox(
                    40, new Spacer(), buttonsPane, togglesGrid, new Spacer()
            );
            controls.setAlignment(Pos.CENTER);

            var root = new TitledPane("Controller", new VBox(30, controls, styleBox));
            root.setCollapsible(false);

            return root;
        }


        private void updateTabsWidth(BorderPane borderPane, TabPane tabs, boolean val) {
            tabs.tabMinWidthProperty().unbind();

            // reset tab width
            if (!val) {
                tabs.setTabMinWidth(Region.USE_COMPUTED_SIZE);
                return;
            }

            // There are two issues with full-width tabs.
            // - minWidth is applied to the tab itself but to internal .tab-container,
            //   thus we have to subtract tab paddings that are normally set via CSS.
            // - .control-buttons-tab appears automatically and can't be disabled via
            //   TabPane property.
            // Overall this feature should be supported by the TabPane internally, otherwise
            // it's hard to make it work properly.

            if (tabs.getSide() == Side.TOP || tabs.getSide() == Side.BOTTOM) {
                tabs.tabMinWidthProperty().bind(borderPane.widthProperty()
                        .subtract(18) // .control-buttons-tab width
                        .divide(tabs.getTabs().size())
                        .subtract(28) // .tab paddings
                );
            }
            if (tabs.getSide() == Side.LEFT || tabs.getSide() == Side.RIGHT) {
                tabs.tabMinWidthProperty().bind(borderPane.heightProperty()
                        .subtract(18) // same as above
                        .divide(tabs.getTabs().size())
                        .subtract(28)
                );
            }
        }

        private TabPane createTabPane() {
            var tabs = new TabPane();
            tabs.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
            tabs.setMinHeight(60);

            // NOTE: Individually disabled tab is still closeable even while it looks
            //       like disabled. To prevent it from closing one can use "black hole"
            //       event handler. #javafx-bug
            if(type==1){
                tabs.getTabs().addAll(
                        createRandomTab(),
                        createRandomTab(),
                        createRandomTab()
                );
            }else {
                tabs.getTabs().addAll(
                        createRandomTabKey(),
                        createRandomTabKey(),
                        createRandomTabKey()
                );
            }


            return tabs;
        }


        private void rotateTabs(BorderPane borderPane, TabPane tabs, Side side) {
            if (tabSide == side) {
                return;
            }

            borderPane.getChildren().removeAll(tabs);
            tabSide = side;

            Platform.runLater(() -> {
                tabs.setSide(side);
                switch (side) {
                    case TOP -> borderPane.setTop(tabs);
                    case RIGHT -> borderPane.setRight(tabs);
                    case BOTTOM -> borderPane.setBottom(tabs);
                    case LEFT -> borderPane.setLeft(tabs);
                }
                updateTabsWidth(borderPane, tabs, fullWidth);
            });
            EVENT_BUS.publish(new TabPaneEvent(eventType));
        }

        private Tab createRandomTab() {
            var tab = new Tab(FAKER.cat().name());
            tab.setGraphic(new FontIcon(randomIcon()));
            return tab;
        }
        private Label createGridLabel(String text) {
            var label = new Label(text);
            label.setAlignment(Pos.CENTER_RIGHT);
            label.setMaxWidth(Double.MAX_VALUE);
            return label;
        }

        private Tab createRandomTabKey() {
            int i = RANDOM.nextInt(RedisDataTypeEnum.values().length);
            RedisDataTypeEnum value = RedisDataTypeEnum.values()[i];
            var tab = new Tab(String.format("%s|%s|%s", 0,value.type, UUID.randomUUID().toString().split("-")[0]));
            tab.setGraphic(new FontIcon(Feather.KEY));
            return tab;
        }

    }









}
