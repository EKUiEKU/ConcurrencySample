package top.xizai.study.redisson.blockQueue;

import lombok.extern.log4j.Log4j2;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import top.xizai.study.redisson.RedissonConstant;
import top.xizai.study.redisson.RedissonInstance;

/**
 * @author: WSC
 * @DATE: 2023/1/11
 * @DESCRIBE:
 **/
@Log4j2
public class RedissonBlockQueueConsumerTest2 {
    public static void main(String[] args) {
        RedissonClient client = RedissonInstance.getRedissonClient();
        RBlockingQueue<String> blockingQueue = client.getBlockingQueue(RedissonConstant.REDISSON_BLOCK_QUEUE);

        RedissonBlockQueueConsumer<String> consumer = new RedissonBlockQueueConsumer<>();

        while (true) {
            consumer.doConsumer(blockingQueue, (result) -> {
                log.info("receive message：{}", result);
            });
        }
    }
}
