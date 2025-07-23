package xyz.hashdog.rdm.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import xyz.hashdog.rdm.redis.Message;
import xyz.hashdog.rdm.ui.common.Applications;
import xyz.hashdog.rdm.ui.entity.config.ConnectionServerNode;
import xyz.hashdog.rdm.ui.util.GuiUtil;

/**
 * 新建分组/分组和连接重命名通用控制层
 * @author th
 * @version 1.0.0
 * @since 2023/7/20 23:36
 */
public class NewGroupController extends BaseWindowController<ServerConnectionsController> {
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
        Message message=null;
        switch (this.model){
            case ADD:
                //父窗口树节点新增,切选中新增节点
                groupNode.setParentDataId(super.parentController.getSelectedDataId());
                message= Applications.addOrUpdateConnectionOrGroup(groupNode);
                parentController.AddConnectionOrGourpNodeAndSelect(groupNode);
                break;
            case UPDATE:
                groupNode.setDataId(dataId.getText());
                message= Applications.addOrUpdateConnectionOrGroup(groupNode);
                //分组修改只会修改名称,直接更新节点名称就行
                parentController.updateNodeName(groupNode.getName());
                break;
            case RENAME:
                groupNode.setDataId(dataId.getText());
                message= Applications.renameConnectionOrGroup(groupNode);
                //分组修改只会修改名称,直接更新节点名称就行
                parentController.updateNodeName(groupNode.getName());
                break;
            default:
                break;

        }
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
