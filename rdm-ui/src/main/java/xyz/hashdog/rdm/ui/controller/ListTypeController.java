package xyz.hashdog.rdm.ui.controller;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import xyz.hashdog.rdm.common.pool.ThreadPool;
import xyz.hashdog.rdm.common.tuple.Tuple2;
import xyz.hashdog.rdm.common.util.DataUtil;
import xyz.hashdog.rdm.ui.entity.ListTypeTable;
import xyz.hashdog.rdm.ui.util.GuiUtil;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Author th
 * @Date 2023/8/3 9:52
 */
public class ListTypeController extends BaseKeyController<KeyTabController> implements Initializable {
    @FXML
    public TableView<ListTypeTable> tableView;
    @FXML
    public BorderPane borderPane;
    @FXML
    public Label total;
    @FXML
    public Label size;
    protected static final String SIZE = "Size:%dB";
    protected static final String TOTAL = "Total:%d";
    @FXML
    public Button findButton;
    @FXML
    public TextField findTextField;
    @FXML
    public Button save;
    @FXML
    public Button addHead;
    @FXML
    public Button addTail;
    @FXML
    public Button delHead;
    @FXML
    public Button delTail;
    @FXML
    public Button delRow;
    /**
     * 缓存所有表格数据
     */
    private ObservableList<ListTypeTable> list = FXCollections.observableArrayList();
    /**
     * 最后选中的行缓存
     */
    private ListTypeTable lastSelect;
    /**
     * 最后一个选中的行对应的最新的value展示
     */
    private ByteArrayController byteArrayController;

    private final static byte[] DEL_MARK = "del_mark".getBytes();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initListener();
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        bindData();
        userDataPropertyListener();
        tableViewListener();
        listListener();


    }

    private void bindData() {
        total.textProperty().bind(Bindings.createStringBinding(() -> String.format(TOTAL,this.list.size()), this.list));
        size.textProperty().bind(Bindings.createStringBinding(() -> String.format(SIZE,this.list.stream().mapToLong(e -> e.getBytes().length).sum()), this.list));
    }

    /**
     * 缓存list数据监听
     */
    private void listListener() {
        this.list.addListener((ListChangeListener<ListTypeTable>) change -> {
            while (change.next()) {
                if (change.wasRemoved()) {
                    System.out.println("Elements were removed: " + change.getRemoved());
                }
            }
        });
    }

    /**
     * 表格监听
     */
    private void tableViewListener() {
        // 监听选中事件
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("有变化");
            if (newValue == null) {
                delRow.setDisable(true);
                save.setDisable(true);
            }
            if (newValue != null) {
                delRow.setDisable(false);
                save.setDisable(false);
                this.lastSelect = newValue;
                Platform.runLater(() -> {
                    Tuple2<AnchorPane, ByteArrayController> tuple2 = loadFXML("/fxml/ByteArrayView.fxml");
                    AnchorPane anchorPane = tuple2.getT1();
                    byteArrayController = tuple2.getT2();
                    byteArrayController.setParentController(this);
                    byteArrayController.setByteArray(newValue.getBytes());
                    borderPane.setCenter(anchorPane);
                });
            }
        });
    }

    /**
     * 父层传送的数据监听
     * 监听到key的传递
     */
    private void userDataPropertyListener() {
        super.parameter.addListener((observable, oldValue, newValue) -> {
            initInfo();
        });
    }

    /**
     * 初始化数据展示
     */
    private void initInfo() {
        ThreadPool.getInstance().execute(() -> {
            long total = this.exeRedis(j -> j.llen(this.parameter.get().getKey()));
            List<byte[]> bytes = this.exeRedis(j -> j.lrange(this.parameter.get().getKey().getBytes(), 0, (int) total));
            for (int i = 0; i < bytes.size(); i++) {
                this.list.add(new ListTypeTable(bytes.get(i)));
            }
            Platform.runLater(() -> {
                ObservableList<TableColumn<ListTypeTable, ?>> columns = tableView.getColumns();
                TableColumn<ListTypeTable, Integer> c0 = (TableColumn) columns.get(0);
                c0.setCellValueFactory(
                        param -> new ReadOnlyObjectWrapper<>(tableView.getItems().indexOf(param.getValue()) + 1)
                );
                for (int i = 1; i < columns.size(); i++) {
                    TableColumn c1 = (TableColumn) columns.get(i);
                    c1.setCellValueFactory(
                            new PropertyValueFactory<ListTypeTable, String>(ListTypeTable.getProperties()[i])
                    );
                }
                find(null);
                //设置默认选中第一行
                tableView.getSelectionModel().selectFirst();
                tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            });


        });

    }

    private Predicate<ListTypeTable> createNameFilter(String query) {
        String regex = query.replace("?", ".?").replace("*", ".*?");
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        return o -> pattern.matcher(o.getValue()).find();
    }


    /**
     * 保存值
     *
     * @param actionEvent
     */
    @FXML
    public void save(ActionEvent actionEvent) {
        byte[] byteArray = byteArrayController.getByteArray();
        asynexec(() -> {
            exeRedis(j -> j.lset(this.getParameter().getKey().getBytes(), this.list.indexOf(lastSelect), byteArray));
            lastSelect.setBytes(byteArray);
            Platform.runLater(() -> {
                tableView.refresh();
                GuiUtil.alert(Alert.AlertType.INFORMATION, "保存成功");
            });
        });
    }

    /**
     * 列表查询
     *
     * @param actionEvent
     */
    public void find(ActionEvent actionEvent) {
        String text = this.findTextField.getText();
        List<ListTypeTable> newList;
        if (DataUtil.isBlank(text)) {
            text = "*";
        }
        Predicate<ListTypeTable> nameFilter = createNameFilter(text);
        newList = this.list.stream().filter(nameFilter).collect(Collectors.toList());
        tableView.setItems(FXCollections.observableList(newList));
        tableView.refresh();
    }

    @FXML
    public void addHead(ActionEvent actionEvent) {
    }

    @FXML
    public void addTail(ActionEvent actionEvent) {

    }

    /**
     * 删除头数据
     *
     * @param actionEvent
     */
    @FXML
    public void delHead(ActionEvent actionEvent) {
        if (!GuiUtil.alertRemove()) {
            return;
        }
        asynexec(() -> {
            exeRedis(j -> j.lpop(this.getParameter().getKey()));
            ListTypeTable listTypeTable = list.get(0);
            remove2UI(listTypeTable);
        });

    }

    /**
     * 删除尾数据
     *
     * @param actionEvent
     */
    @FXML
    public void delTail(ActionEvent actionEvent) {
        if (!GuiUtil.alertRemove()) {
            return;
        }
        asynexec(() -> {
            exeRedis(j -> j.rpop(this.getParameter().getKey()));
            ListTypeTable listTypeTable = list.get(list.size() - 1);
            remove2UI(listTypeTable);
        });

    }


    /**
     * 删除选中行
     *
     * @param actionEvent
     */
    @FXML
    public void delRow(ActionEvent actionEvent) {
        if (!GuiUtil.alertRemove()) {
            return;
        }
        asynexec(() -> {
            exeRedis(j -> j.lset(this.getParameter().getKey().getBytes(), this.list.indexOf(lastSelect), DEL_MARK));
            exeRedis(j -> j.lrem(this.getParameter().getKey().getBytes(), 0, DEL_MARK));
            remove2UI(lastSelect);
        });
    }

    /**
     * 视图上删除对应数据
     *
     * @param lastSelect
     */
    private void remove2UI(ListTypeTable lastSelect) {
        Platform.runLater(() -> {
            //缓存的所有数据需要删除
            this.list.remove(lastSelect);
        });
        int i = tableView.getItems().indexOf(lastSelect);
        if (i > -1) {
            Platform.runLater(() -> {
                //视图需要删除
                tableView.getItems().remove(i);
                tableView.refresh();
            });
        }
    }
}
