package xyz.hashdog.rdm.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import xyz.hashdog.rdm.redis.Message;
import xyz.hashdog.rdm.ui.common.Applications;
import xyz.hashdog.rdm.ui.entity.ConnectionServerNode;
import xyz.hashdog.rdm.ui.util.GuiUtil;

/**
 * @Author th
 * @Date 2023/7/20 23:36
 */
public class NewGroupController extends BaseController<ServerConnectionsController>{
    /**
     * 根
     */
    @FXML
    public AnchorPane root;
    /**
     * 名称
     */
    @FXML
    public TextField name;
    /**
     * id
     */
    @FXML
    public TextField dataId;


    /**
     * 新增/修改的确定
     * @param actionEvent
     */
    @FXML
    public void ok(ActionEvent actionEvent) {
        if(GuiUtil.requiredTextField(name)){
            return;
        }
        ConnectionServerNode groupNode =new ConnectionServerNode(ConnectionServerNode.GROUP);
        groupNode.setName(name.getText());
        groupNode.setParentDataId(super.parentController.getSelectedDataId());
        Message message= Applications.addConnectionOrGroup(groupNode);
        if(message.isSuccess()){
            currentStage.close();
        }
        //父窗口树节点新增,切选中新增节点
        parentController.AddConnectionOrGourpNodeAndSelect(groupNode);
    }


}
