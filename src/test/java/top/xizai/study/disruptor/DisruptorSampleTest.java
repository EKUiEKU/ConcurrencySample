package top.xizai.study.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.junit.jupiter.api.Test;
import top.xizai.study.distuptor.DisruptorSample;
import top.xizai.study.distuptor.LongEvent;
import top.xizai.study.distuptor.LongEventTranslator;

import java.util.concurrent.Executors;

/**
 * @author: WSC
 * @DATE: 2023/1/11
 * @DESCRIBE:
 **/
public class DisruptorSampleTest {
    int bufferSize = 1024 * 1024;//环形队列长度，必须是2的N次方
    /**
     * 定义Disruptor，基于单生产者，阻塞策略
     */
    Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(LongEvent::new, bufferSize, Executors.defaultThreadFactory(), ProducerType.SINGLE, new BlockingWaitStrategy());

    DisruptorSample disruptorSample = new DisruptorSample();

    /**
     * 并行
     */
    @Test
    public void parallelTest() {
        disruptorSample.parallel(disruptor);

        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        ringBuffer.publishEvent(new LongEventTranslator(),10L);
        ringBuffer.publishEvent(new LongEventTranslator(),100L);
    }

    /**
     * 串行
     */
    @Test
    public void serialTest() {
        disruptorSample.serial(disruptor);

        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        ringBuffer.publishEvent(new LongEventTranslator(),10L);
        ringBuffer.publishEvent(new LongEventTranslator(),100L);
    }

    /**
     * 菱形
     */
    @Test
    public void diamondTest() {
        disruptorSample.diamond(disruptor);

        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        ringBuffer.publishEvent(new LongEventTranslator(),10L);
        ringBuffer.publishEvent(new LongEventTranslator(),100L);
    }
    /**
     * 链式并行计算
     */
    @Test
    public void chainTest() {
        disruptorSample.chain(disruptor);

        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        ringBuffer.publishEvent(new LongEventTranslator(),10L);
        ringBuffer.publishEvent(new LongEventTranslator(),100L);
    }

    /**
     * 并行计算
     * 并发消费, 并且防止重复消费·
     */
    @Test
    public void parallelWithPoolTest() {
        disruptorSample.parallelWithPool(disruptor);

        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        ringBuffer.publishEvent(new LongEventTranslator(),10L);
        ringBuffer.publishEvent(new LongEventTranslator(),100L);
    }


    /**
     * 串行执行, 并且每个环境有多个消费
     */
    @Test
    public void serialWithPoolTest() {
        disruptorSample.serialWithPool(disruptor);

        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        ringBuffer.publishEvent(new LongEventTranslator(),10L);
        ringBuffer.publishEvent(new LongEventTranslator(),100L);
    }
}
