package xyz.hashdog.rdm.ui.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;

/**
 *
 * 用于父子关系
 * @Author th
 * @Date 2023/7/22 10:43
 */
public abstract class BaseController<T> {
    /**
     * 父控制器
     */
    public T parentController;
    /**
     * 用于父向子传递数据
     */
    protected ObjectProperty<Object> userDataProperty = new SimpleObjectProperty<>();

    protected FXMLLoader loadFXML(String fxml) {
        return new FXMLLoader(getClass().getResource(fxml));
    }

    protected void setUserDataProperty(Object userDataProperty) {
        this.userDataProperty.set(userDataProperty);
    }

    protected void setParentController(T parentController) {
        this.parentController = parentController;
    }



}
