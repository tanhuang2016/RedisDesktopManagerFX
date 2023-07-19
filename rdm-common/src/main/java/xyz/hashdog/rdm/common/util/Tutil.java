package xyz.hashdog.rdm.common.util;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 泛型/反射相关操作工具
 *
 * @Author th
 * @Date 2023/7/18 21:09
 */
public class Tutil {
    /**
     * 反射获取对象的字段
     *
     * @param obj
     * @param fieldName 获取的字段
     * @param <T>
     * @return
     */
    public static <T> T getField(Object obj, String fieldName) {
        // 使用反射获取字段的值
        try {
            // 获取对象的 Class 对象
            Class<?> objClass = obj.getClass();
            // 获取字段对象
            Field field = objClass.getDeclaredField(fieldName);
            // 设置允许访问私有字段
            field.setAccessible(true);
            // 获取字段的值
            Object fieldValue = field.get(obj);
            return (T) fieldValue;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * spi获取此类服务
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T spi(Class<T> clazz) {
        ServiceLoader<T> load = ServiceLoader.load(clazz);
        Iterator<T> iterator = load.iterator();

        //循环获取所需的对象
        while (iterator.hasNext()) {
            T next = iterator.next();
            if (next != null) {
                return next;
            }
        }
        throw new RuntimeException("no such spi:" + clazz.getName());
    }


    /**
     * 执行方法
     * 目前用于统一处理jedis执行命令之后的close操作
     * @param t           可以是jedis
     * @param execCommand 需要执行的具体逻辑
     * @param callback    执行逻辑之后的回调,比如关流
     * @param <T>         jedis
     * @param <R>         执行jedis命令之后的返回值
     * @return
     */
    public static <T, R> R execut(T t, Function<T, R> execCommand, Consumer<T> callback) {
        try {
            return execCommand.apply(t);
        } finally {
            callback.accept(t);
        }
    }
}
