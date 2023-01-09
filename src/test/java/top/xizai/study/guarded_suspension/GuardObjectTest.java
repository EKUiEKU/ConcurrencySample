package top.xizai.study.guarded_suspension;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import top.xizai.study.utils.ThreadPoolInstance;
import top.xizai.study.utils.TimeUtil;

import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.*;

/**
 * @author: WSC
 * @DATE: 2023/1/9
 * @DESCRIBE:
 **/
@Log4j2
public class GuardObjectTest {
    List<String> messageIdList = new Vector<>();
    ThreadPoolExecutor poolExecutor = ThreadPoolInstance.getInstance();
    private SynchronousQueue<String> queue = new SynchronousQueue<>();

    @Test
    public void test() {
        product();
        consumer();

        while (true) {}
    }

    @Getter
    @Setter
    class Message<T> {
        String messageId;
        Long spendTime;
        Boolean status;
        T what;
    }

    /**
     * 生产者
     */
    public void product() {
        new Thread(() -> {
            while (true) {
                poolExecutor.execute(() -> {
                    // 模拟发送MQ
                    String messageId = UUID.fastUUID().toString(true);
                    GuardObject<Message<String>> guardObject = GuardObject.create(messageId);
                    try {
                        queue.put(messageId);
                        // 接收消息
                        Message<String> message = guardObject.get((t) -> t != null);
                        log.info("消息到达, messageId：{}, 请求状态：{}, 消息内容：{}, 花费时间：{}", message.getMessageId(),
                                message.getStatus(), message.getWhat(), message.getSpendTime() + "ms.");
                    } catch (InterruptedException e) {
                        guardObject.forceRelease(messageId);
                        throw new RuntimeException(e);
                    }
                });
                TimeUtil.randomSleep(50, 100);
            }
        }).start();
    }


    public void consumer() {
        /**
         * 开两条消费线程
         */
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                while (true) {
                    String messageId = null;
                    try {
                        messageId = queue.take();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    if (messageId != null) {
                        long spendTime = System.currentTimeMillis();
                        // 模拟处理时间
                        TimeUtil.randomSleep(100, 200);
                        spendTime = System.currentTimeMillis() - spendTime;

                        Message<String> result = new Message<>();
                        result.setSpendTime(spendTime);
                        result.setMessageId(messageId);
                        result.setStatus(true);
                        result.setWhat(String.format("消息在消费端处理成功, 现在时间是：%s", DateUtil.format(new Date(), "yyyy-MM-dd hh:mm:ss.SSSSSS")));
                        GuardObject.onChange(messageId, result);
                    }
                }
            }).start();
        }
    }
}
