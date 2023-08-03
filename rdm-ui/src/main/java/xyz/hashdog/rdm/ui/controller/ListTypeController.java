package xyz.hashdog.rdm.ui.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import xyz.hashdog.rdm.common.pool.ThreadPool;
import xyz.hashdog.rdm.ui.entity.ListTypeTable;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @Author th
 * @Date 2023/8/3 9:52
 */
public class ListTypeController extends BaseKeyController<KeyTabController> implements Initializable {
    @FXML
    public TableView<ListTypeTable> tableView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initListener();
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        userDataPropertyListener();
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
    private void initInfo()  {
        ThreadPool.getInstance().execute(() -> {
           long total = this.exeRedis(j -> j.llen(this.parameter.get().getKey()));
            List<byte[]> bytes = this.exeRedis(j -> j.lrange(this.parameter.get().getKey().getBytes(), 0, (int) total));
            List<ListTypeTable> list = new ArrayList<>();
            for (int i = 0; i < bytes.size(); i++) {
                list.add(new ListTypeTable(i+1,bytes.get(i)));
            }
            Platform.runLater(()->{
                ObservableList<TableColumn<ListTypeTable, ?>> columns = tableView.getColumns();

                TableColumn c1 = (TableColumn) columns.get(0);
                c1.setCellValueFactory(
                        new PropertyValueFactory<ListTypeTable, String>("row")
                );
                TableColumn c2 = (TableColumn) columns.get(1);
                c2.setCellValueFactory(
                        new PropertyValueFactory<ListTypeTable, String>("value")
                );
                tableView.setItems(FXCollections.observableList(list));
            });



//            this.currentValue = bytes;
//            Platform.runLater(()->{
//                Tuple2<AnchorPane,ByteArrayController> tuple2 = loadFXML("/fxml/ByteArrayView.fxml");
//                AnchorPane anchorPane = tuple2.getT1();
//                this.byteArrayController  = tuple2.getT2();
//                this.byteArrayController.setParentController(this);
//                this.byteArrayController.setByteArray(this.currentValue);
//                borderPane.setCenter(anchorPane);
//            });
        });

    }


    /**
     * 保存值
     * @param actionEvent
     */
    @FXML
    public void save(ActionEvent actionEvent) {

    }
}
