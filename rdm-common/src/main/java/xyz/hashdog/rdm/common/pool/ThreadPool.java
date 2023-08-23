/**
 * <p>Title:public class ThreadPool {.java</p>
 * <p>Description: 守护线程池</p>
 * <p>Copyright:研发部 Copyright(c)2022</p>
 * <p>Date:2022-07-02</p>
 *
 * @author th
 * @version 1.0.0
 * @version 1.0
 */
package xyz.hashdog.rdm.common.pool;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 守护线程池
 * 不一定会用到,所以使用双重检查实现单例
 * @author th
 * @version 1.0.0
 */
public class ThreadPool {
    /**
     * 当前系统核心数
     */
    private static final int NUM = Runtime.getRuntime().availableProcessors();
    /**
     * 最多两个现城
     */
    private static final int DEFAULT_CORE_SIZE = NUM <= 2 ? 1 : 2;
    /**
     * 最大队列数
     */
    private static final int MAX_QUEUE_SIZE = DEFAULT_CORE_SIZE + 2;
    /**
     * 线程池
     */
    private volatile static ThreadPoolExecutor executor;


    private ThreadPool() {
    }

    ;

    /**
     * 获取线程池
     * @return
     */
    public static ThreadPoolExecutor getInstance() {
        if (executor == null) {
            synchronized (ThreadPoolExecutor.class) {
                if (executor == null) {
                    executor = new ThreadPoolExecutor(DEFAULT_CORE_SIZE,// 核心线程数
                            MAX_QUEUE_SIZE, // 最大线程数
                            60 * 1000, // 闲置线程存活时间
                            TimeUnit.MILLISECONDS,// 时间单位
                            new LinkedBlockingDeque<Runnable>(Integer.MAX_VALUE),// 线程队列
//                            Executors.defaultThreadFactory()// 线程工厂
                            new DaemonThreadFactory("task")
                    );
                }
            }
        }
        return executor;
    }


}
