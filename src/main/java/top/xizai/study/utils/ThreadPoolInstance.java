package top.xizai.study.utils;

import top.xizai.study.thread.MyThreadPoolExecutor;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: WSC
 * @DATE: 2023/1/6
 * @DESCRIBE:
 **/
public class ThreadPoolInstance {
    private static volatile ThreadPoolExecutor threadPool = null;

    private ThreadPoolInstance() {}

    public static ThreadPoolExecutor getInstance() {
        if (threadPool == null) {
            synchronized (ThreadPoolExecutor.class) {
                if (threadPool == null) {
                    threadPool = new MyThreadPoolExecutor(Runtime.getRuntime().availableProcessors() + 1, 50
                            , 30, TimeUnit.SECONDS, new LinkedBlockingQueue<>()
                            , new ThreadPoolExecutor.CallerRunsPolicy());
                }
            }
        }

        return threadPool;
    }
}
