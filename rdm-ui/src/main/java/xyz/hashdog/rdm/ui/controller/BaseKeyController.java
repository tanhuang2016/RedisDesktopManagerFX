package xyz.hashdog.rdm.ui.controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import xyz.hashdog.rdm.redis.client.RedisClient;
import xyz.hashdog.rdm.ui.entity.PassParameter;

import java.util.function.Function;

/**
 * @Author th
 * @Date 2023/7/23 22:30
 */
public class BaseKeyController<T> extends BaseController<T>{
    /**
     * 当前控制层操作的tab所用的redis客户端连接
     * 此客户端可能是单例,也就是共享的
     */
    protected RedisClient redisClient;
    /**
     * 当前db
     */
    protected int  currentDb;

    /**
     * 用于父向子传递db和key
     */
    protected ObjectProperty<PassParameter> parameter = new SimpleObjectProperty<>();

    /**
     * 执行方法
     * 目前用于统一处理jedis执行命令之后的close操作
     *
     * @param execCommand 需要执行的具体逻辑
     * @param <R>         执行jedis命令之后的返回值
     * @return
     */
    public  <R> R exeRedis( Function<RedisClient, R> execCommand) {
        if(redisClient.getDb()!=this.currentDb){
            redisClient.select(currentDb);
        }
        return execCommand.apply(redisClient);
    }

    public PassParameter getParameter() {
        return parameter.get();
    }

    public ObjectProperty<PassParameter> parameterProperty() {
        return parameter;
    }

    public void setParameter(PassParameter parameter) {
        this.parameter.set(parameter);
        this.currentDb=parameter.getDb();
    }
}
