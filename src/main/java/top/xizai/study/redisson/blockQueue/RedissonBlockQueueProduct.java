package top.xizai.study.redisson.blockQueue;

import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import top.xizai.study.redisson.RedissonConstant;

import java.util.concurrent.BlockingQueue;

/**
 * @author: WSC
 * @DATE: 2023/1/11
 * @DESCRIBE:
 **/
public class RedissonBlockQueueProduct<T> {
    public void product(BlockingQueue<T> blockingQueue, T info) {
        if (blockingQueue != null) {
            blockingQueue.offer(info);
        }
    }
}
