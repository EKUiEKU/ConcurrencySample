package top.xizai.study.redisson.blockQueue;

import org.redisson.api.RAtomicLong;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import top.xizai.study.redisson.RedissonConstant;
import top.xizai.study.redisson.RedissonInstance;
import top.xizai.study.utils.TimeUtil;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author: WSC
 * @DATE: 2023/1/11
 * @DESCRIBE:
 **/
public class RedissonBlockQueueProductTest {
    public static void main(String[] args) {
        RedissonClient client = RedissonInstance.getRedissonClient(args);
        RBlockingQueue<String> blockingQueue = client.getBlockingQueue(RedissonConstant.REDISSON_BLOCK_QUEUE);
        RAtomicLong incrLong = client.getAtomicLong(RedissonConstant.REDISSON_BLOCK_QUEUE_INCR);

        RedissonBlockQueueProduct<String> product = new RedissonBlockQueueProduct<>();

        while (true) {
            product.product(blockingQueue, "接收消息的索引：" + incrLong.incrementAndGet());
            TimeUtil.randomSleep(10, 100);
        }
    }
}
