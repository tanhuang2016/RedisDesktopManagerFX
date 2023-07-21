package xyz.hashdog.rdm.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import xyz.hashdog.rdm.common.util.DataUtil;
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
        //id存在则是修改,否则是新增
        if(DataUtil.isNotBlank(dataId.getText())){
            groupNode.setDataId(dataId.getText());
            //分组修改只会修改名称,直接更新节点名称就行
            parentController.updateNodeName(groupNode.getName());
        }else{
            groupNode.setParentDataId(super.parentController.getSelectedDataId());
            //父窗口树节点新增,切选中新增节点
            parentController.AddConnectionOrGourpNodeAndSelect(groupNode);
        }
        Message message= Applications.addOrUpdateConnectionOrGroup(groupNode);
        if(message.isSuccess()){
            currentStage.close();
        }
    }

    /**
     * 填充编辑数据
     * @param selectedNode
     */
    public void editInfo(ConnectionServerNode selectedNode) {
        name.setText(selectedNode.getName());
        dataId.setText(selectedNode.getDataId());
    }

}
