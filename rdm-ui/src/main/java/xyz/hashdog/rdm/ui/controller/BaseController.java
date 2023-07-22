package xyz.hashdog.rdm.ui.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * 用于父子关系
 * @Author th
 * @Date 2023/7/22 10:43
 */
public abstract class BaseController<T> {
    protected static Logger log = LoggerFactory.getLogger(BaseController.class);
    /**
     * 父控制器
     */
    public T parentController;
    /**
     * 用于父向子传递数据
     */
    protected ObjectProperty<Object> userDataProperty = new SimpleObjectProperty<>();

    public void setUserDataProperty(Object userDataProperty) {
        this.userDataProperty.set(userDataProperty);
    }

    public void setParentController(T parentController) {
        this.parentController = parentController;
    }



}
