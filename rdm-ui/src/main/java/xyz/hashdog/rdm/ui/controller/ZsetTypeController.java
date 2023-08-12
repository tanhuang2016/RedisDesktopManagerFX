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
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import xyz.hashdog.rdm.common.pool.ThreadPool;
import xyz.hashdog.rdm.common.tuple.Tuple2;
import xyz.hashdog.rdm.common.util.DataUtil;
import xyz.hashdog.rdm.ui.entity.ZsetTypeTable;
import xyz.hashdog.rdm.ui.util.GuiUtil;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Author th
 * @Date 2023/8/3 9:41
 */
public class ZsetTypeController extends BaseKeyController<KeyTabController> implements Initializable {
    private static final int ROWS_PER_PAGE = 32;
    @FXML
    public TableView<ZsetTypeTable> tableView;
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
    public Button delRow;
    @FXML
    public Button add;
    @FXML
    public Pagination pagination;
    @FXML
    public TextField score;
    /**
     * 缓存所有表格数据
     */
    private ObservableList<ZsetTypeTable> list = FXCollections.observableArrayList();
    /**
     * 查询后的表格数据
     */
    private ObservableList<ZsetTypeTable> findList = FXCollections.observableArrayList();
    /**
     * 最后选中的行缓存
     */
    private ZsetTypeTable lastSelect;
    /**
     * 最后一个选中的行对应的最新的value展示
     */
    private ByteArrayController byteArrayController;
    private ByteArrayController keyByteArrayController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bindData();
        initListener();
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        userDataPropertyListener();
        tableViewListener();
        listListener();
        paginationListener();
    }

    /**
     * 分页监听
     * 数据显示,全靠分页监听
     */
    private void paginationListener() {
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            int pageIndex = (int) newIndex;
            setCurrentPageIndex(pageIndex);

        });
    }

    /**
     * 可以手动触发分页
     *
     * @param pageIndex
     */
    private void setCurrentPageIndex(int pageIndex) {
        if (pageIndex < pagination.getPageCount() - 1) {
            List<ZsetTypeTable> pageList = findList.subList(pageIndex * ROWS_PER_PAGE, (pageIndex + 1) * ROWS_PER_PAGE + 1);
            tableView.setItems(FXCollections.observableArrayList(pageList));
        } else {
            List<ZsetTypeTable> pageList = findList.subList(pageIndex * ROWS_PER_PAGE, findList.size());
            tableView.setItems(FXCollections.observableArrayList(pageList));
        }

        tableView.refresh();

    }

    /**
     * 缓存list数据监听
     */
    private void listListener() {
        this.list.addListener((ListChangeListener<ZsetTypeTable>) change -> {
            while (change.next()) {
                //删除到最后一个元素时,key也被删了,需要关闭tab
                if (change.wasRemoved() && this.list.size() == 0) {
                    super.parentController.parentController.removeTabByKeys(Arrays.asList(parameter.get().getKey()));
                    super.parentController.parentController.delKey(parameter);
                }
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

    private void bindData() {
        total.textProperty().bind(Bindings.createStringBinding(() -> String.format(TOTAL, this.list.size()), this.list));
        size.textProperty().bind(Bindings.createStringBinding(() -> String.format(SIZE, this.list.stream().mapToLong(e -> e.getBytes().length ).sum()), this.list));
    }

    /**
     * 表格监听
     */
    private void tableViewListener() {
        // 监听选中事件
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                delRow.setDisable(true);
                save.setDisable(true);
            }
            if (newValue != null) {
                delRow.setDisable(false);
                save.setDisable(false);
                this.lastSelect = newValue;
                Platform.runLater(() -> {
                    Tuple2<AnchorPane, ByteArrayController> valueTuple2 = loadByteArrayView(newValue.getBytes());
                    byteArrayController = valueTuple2.getT2();
                    VBox vBox = (VBox) borderPane.getCenter();
                    VBox.setVgrow(valueTuple2.getT1(), Priority.ALWAYS);
                    ObservableList<Node> children = vBox.getChildren();
                    children.set(children.size()-1,valueTuple2.getT1());
                    score.setText(String.valueOf(newValue.getScore()));

                });
            }
        });
    }

    /**
     * 加载byteArrayView
     *
     * @param bytes
     * @return
     */
    private Tuple2<AnchorPane, ByteArrayController> loadByteArrayView(byte[] bytes) {
        Tuple2<AnchorPane, ByteArrayController> tuple2 = loadFXML("/fxml/ByteArrayView.fxml");
        tuple2.getT2().setParentController(this);
        tuple2.getT2().setByteArray(bytes);
        return tuple2;
    }

    /**
     * 初始化数据展示
     */
    private void initInfo() {
        ThreadPool.getInstance().execute(() -> {
            Long total = this.exeRedis(j -> j.zcard(this.parameter.get().getKey()));
            Map<Double, byte[]> map = this.exeRedis(j -> j.zrangeWithScores(this.parameter.get().getKey().getBytes(), 0l, total));
            map.forEach((k, v) -> this.list.add(new ZsetTypeTable(k, v)));
            Platform.runLater(() -> {
                ObservableList<TableColumn<ZsetTypeTable, ?>> columns = tableView.getColumns();
                TableColumn<ZsetTypeTable, Integer> c0 = (TableColumn) columns.get(0);
                c0.setCellValueFactory(
                        param -> new ReadOnlyObjectWrapper<>(tableView.getItems().indexOf(param.getValue()) + 1)
                );
                for (int i = 1; i < columns.size(); i++) {
                    TableColumn c1 = (TableColumn) columns.get(i);
                    c1.setCellValueFactory(
                            new PropertyValueFactory<ZsetTypeTable, String>(ZsetTypeTable.getProperties()[i])
                    );
                    c1.setCellFactory(param -> new GuiUtil.OneLineTableCell<>());
                }
                find(null);
                //设置默认选中第一行
                tableView.getSelectionModel().selectFirst();
                tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
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
        List<ZsetTypeTable> newList;
        if (DataUtil.isBlank(text)) {
            text = "*";
        }
        Predicate<ZsetTypeTable> nameFilter = createNameFilter(text);
        newList = this.list.stream().filter(nameFilter).collect(Collectors.toList());
        findList.clear();
        findList.addAll(newList);
        pagination.setPageCount((int) Math.ceil((double) findList.size() / ROWS_PER_PAGE));
        //当前页就是0页才需要手动触发,否则原事件触发不了
        if (pagination.getCurrentPageIndex() == 0) {
            this.setCurrentPageIndex(0);
        }
    }


    private Predicate<ZsetTypeTable> createNameFilter(String query) {
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
        //修改后的value
        byte[] value = byteArrayController.getByteArray();
        Double score = Double.valueOf(this.score.getText());
        int i = this.list.indexOf(lastSelect);
        asynexec(() -> {
            //value发生变化的情况,需要先删后增
            if (!Arrays.equals(value,lastSelect.getBytes())) {
                exeRedis(j -> j.zrem(this.getParameter().getKey().getBytes(), lastSelect.getBytes()));
                lastSelect.setBytes(value);
            }
            exeRedis(j -> j.zadd(this.getParameter().getKey().getBytes(), score, value));
            lastSelect.setScore(score);
            Platform.runLater(() -> {
                //实际上list存的引用,lastSelect修改,list中的元素也会修改,重新set进去是为了触发更新事件
                this.list.set(i,lastSelect);
                tableView.refresh();
                byteArrayController.setByteArray(value);
                GuiUtil.alert(Alert.AlertType.INFORMATION, "保存成功");
            });
        });
    }

    /**
     * 新增
     *
     * @param actionEvent
     */
    @FXML
    public void add(ActionEvent actionEvent) {
    }

    /**
     * 删除行
     *
     * @param actionEvent
     */
    public void delRow(ActionEvent actionEvent) {
        if (!GuiUtil.alertRemove()) {
            return;
        }
        asynexec(() -> {
            exeRedis(j -> j.zrem(this.getParameter().getKey().getBytes(), lastSelect.getBytes()));
            GuiUtil.remove2UI(this.list,this.tableView,lastSelect);
        });
    }


}
