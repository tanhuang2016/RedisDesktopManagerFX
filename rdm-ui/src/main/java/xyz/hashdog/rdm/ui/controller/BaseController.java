package xyz.hashdog.rdm.ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import xyz.hashdog.rdm.common.pool.ThreadPool;
import xyz.hashdog.rdm.common.tuple.Tuple2;
import xyz.hashdog.rdm.ui.Main;
import xyz.hashdog.rdm.ui.util.GuiUtil;

import java.io.IOException;

/**
 *
 * 用于父子关系
 * 封装通用方法
 * @author th
 * @version 1.0.0
 * @since 2023/7/22 10:43
 */
public abstract class BaseController<T> {
    /**
     * 父控制器
     */
    public T parentController;

    /**
     * port只能为正整数
     * @param keyEvent
     */
    @FXML
    public void filterIntegerInput(KeyEvent keyEvent) {
        // 获取用户输入的字符
        String inputChar = keyEvent.getCharacter();
        // 如果输入字符不是整数，则阻止其显示在TextField中
        if (!inputChar.matches("\\d")) {
            keyEvent.consume();
        }
    }

    /**
     * 线程池异步执行
     * @param runnable
     */
    protected void asynexec(Runnable runnable) {
        ThreadPool.getInstance().execute(runnable);
    }

    /**
     * 只让输入整数
     */
    protected void filterIntegerInputListener(boolean flg,TextField... port) {
        GuiUtil.filterIntegerInput(flg,port);
    }

    public <T1,T2>Tuple2<T1,T2> loadFXML(String fxml) {
        return GuiUtil.doLoadFXML(fxml);
    }



    public void setParentController(T parentController) {
        this.parentController = parentController;
    }





}
