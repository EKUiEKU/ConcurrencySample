package top.xizai.study.distuptor;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import top.xizai.study.distuptor.consumer.C11EventHandler;
import top.xizai.study.distuptor.consumer.C12EventHandler;
import top.xizai.study.distuptor.consumer.C21EventHandler;
import top.xizai.study.distuptor.consumer.C22EventHandler;

/**
 * @author: WSC
 * @DATE: 2023/1/11
 * @DESCRIBE: 高性能的无锁队列
 **/
public class DisruptorSample {
    /**
     * 并行计算实现,c1,c2互相不依赖
     * p --> c11
     * --> c21
     */
    public void parallel(Disruptor<LongEvent> disruptor) {
        disruptor.handleEventsWith(new C11EventHandler(), new C21EventHandler());
        disruptor.start();
    }

    /**
     * 串行依次执行
     * p --> c11 --> c21
     *
     * @param disruptor
     */
    public void serial(Disruptor<LongEvent> disruptor) {
        disruptor.handleEventsWith(new C11EventHandler()).then(new C21EventHandler());
        disruptor.start();
    }

    /**
     * 菱形方式执行
     * <br/>
     * --> c11
     * p         --> c21
     * --> c12
     *
     * @param disruptor
     */
    public void diamond(Disruptor<LongEvent> disruptor) {
        disruptor.handleEventsWith(new C11EventHandler(), new C12EventHandler()).then(new C21EventHandler());
        disruptor.start();
    }

    /**
     * 链式并行计算
     * <br/>
     * --> c11 --> c12
     * p
     * --> c21 --> c22
     *
     * @param disruptor
     */
    public void chain(Disruptor<LongEvent> disruptor) {
        disruptor.handleEventsWith(new C11EventHandler()).then(new C12EventHandler());
        disruptor.handleEventsWith(new C21EventHandler()).then(new C22EventHandler());
        disruptor.start();
    }

    /**
     * 并行计算实现,c1,c2互相不依赖,同时C1，C2分别有2个实例
     * <br/>
     * p --> c11
     * --> c21
     */
    public void parallelWithPool(Disruptor<LongEvent> disruptor) {
        disruptor.handleEventsWithWorkerPool(new C11EventHandler(), new C11EventHandler());
        disruptor.handleEventsWithWorkerPool(new C21EventHandler(), new C21EventHandler());
        disruptor.start();
    }

    /*
     * 串行依次执行,同时C11，C21分别有2个实例
     * <br/>
     * p --> c11 --> c21
     * @param disruptor
     */
    public void serialWithPool(Disruptor<LongEvent> disruptor) {
        disruptor.handleEventsWithWorkerPool(new C11EventHandler(), new C11EventHandler()).thenHandleEventsWithWorkerPool(new C21EventHandler(), new C21EventHandler());
        disruptor.start();
    }
}
