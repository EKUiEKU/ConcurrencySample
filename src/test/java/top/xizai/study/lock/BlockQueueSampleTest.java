package top.xizai.study.lock;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import top.xizai.study.utils.ThreadPoolInstance;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: WSC
 * @DATE: 2023/1/6
 * @DESCRIBE:
 **/
@Log4j2
public class BlockQueueSampleTest {
    private BlockQueueSample<Runnable> blockTaskQueue = new BlockQueueSample<>();
    ThreadPoolExecutor threadPool = ThreadPoolInstance.getInstance();

    private AtomicInteger currentExecuteTaskCount = new AtomicInteger(0);
    private void product() {
        blockTaskQueue.add(() -> {
            String tName = Thread.currentThread().getName();
            log.info("{} 正在执行中...., 当前是第{}个执行的任务", tName, currentExecuteTaskCount.incrementAndGet());
        });
    }

    private void consume() {
        Runnable task = blockTaskQueue.pop();
        threadPool.execute(task);
    }


    @Test
    public void test() {
        new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                product();
            }
        }).start();

        new Thread(() -> {
            while (true) {
                consume();
            }
        }).start();

        while (true);
    }
}
