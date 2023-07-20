package xyz.hashdog.rdm.ui.controller;

import javafx.stage.Stage;

/**
 * @Author th
 * @Date 2023/7/20 17:35
 */
public class BaseController<T> {
    /**
     *当前Stage
     */
    public Stage currentStage;
    /**
     * 父控制器
     */
    public T parentController;


    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }

    public void setParentController(T parentController) {
        this.parentController = parentController;
    }
}
