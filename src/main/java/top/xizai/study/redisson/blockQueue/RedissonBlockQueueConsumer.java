package top.xizai.study.redisson.blockQueue;

import java.util.concurrent.BlockingQueue;

/**
 * @author: WSC
 * @DATE: 2023/1/11
 * @DESCRIBE:
 **/
public class RedissonBlockQueueConsumer<T> {
    public void doConsumer(BlockingQueue<T> blockingQueue, Function<T> callBack) {
        if (blockingQueue != null) {
            T t = null;
            try {
                t = blockingQueue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            callBack.apply(t);
        }
    }

    public interface Function<T> {
        void apply(T result);
    }
}
